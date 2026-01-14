package com.semi.service.notice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.semi.common.util.DBUtil;
import com.semi.domain.Notice;

/**
 * Notice JDBC DAO
 * - Oracle ROWNUM 기반 페이징
 * - LOWER/UPPER 없이 LIKE 사용
 */
public class NoticeDao {

    // ====== SELECT COUNT (검색 포함) ======
	public int count(String keyword) throws SQLException {
	    System.out.println("[NoticeDao] count() :: keyword=" + keyword);

	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT COUNT(*) FROM notice ");
	    boolean hasKey = (keyword != null && !keyword.trim().isEmpty());
	    if (hasKey) {
	        sql.append("WHERE title LIKE ? "); //조건 제목으로만 변경
	        //sql.append("WHERE (title LIKE ? OR content LIKE ? OR authorid LIKE ?)"); 여기서 변경
	    }

	    try {
	        conn = DBUtil.getConnection();
	        pstmt = conn.prepareStatement(sql.toString());

	        if (hasKey) {
	            pstmt.setString(1, "%" + keyword.trim() + "%"); // 부분 일치
	        }

	        rs = pstmt.executeQuery();
	        rs.next();
	        return rs.getInt(1);

	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	        DBUtil.close(conn);
	    }
	}


    // ====== 목록 (ROWNUM 페이징) ======
	public List<Notice> findList(int startRow, int endRow, String keyword) throws SQLException {
	    System.out.println("[NoticeDao] findList() :: startRow=" + startRow + ", endRow=" + endRow + ", keyword=" + keyword);

	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    boolean hasKey = (keyword != null && !keyword.trim().isEmpty());

	    // Oracle 10g 전통 페이징 (ROWNUM) + 제목 부분 일치
	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT * FROM ( ")
	       .append("  SELECT ROWNUM rnum, a.* FROM ( ")
	       .append("    SELECT noticeid, authorid, title, content, createdat ")
	       .append("    FROM notice ");
	    if (hasKey) {
	        sql.append("    WHERE title LIKE ? ");
	    }
	    sql.append("    ORDER BY noticeid DESC ")
	       .append("  ) a ")
	       .append("  WHERE ROWNUM <= ? ")
	       .append(") WHERE rnum >= ?");
	    
	    /*이전쿼리
	     * String base = "SELECT noticeid, authorid, title, content, createdat FROM notice";
	     * String where = (keyword != null && !keyword.isEmpty())
	     * ? " WHERE (title LIKE ? OR content LIKE ? OR authorid LIKE ?)" : "";
	     * String order = " ORDER BY noticeid DESC";
	     * 
	     * sql.append("    WHERE (title LIKE ? OR content LIKE ? OR authorid LIKE ?)");
	     * 
	     * String pagingSql =
	     *  "SELECT * FROM ( " +
	     *  "  SELECT t.*, ROWNUM rnum FROM ( " +
	     *  base + where + order +
	     *  "  ) t WHERE ROWNUM <= ? " +
	     *  ") WHERE rnum >= ?";
	     */

	    try {
	        conn = DBUtil.getConnection();
	        pstmt = conn.prepareStatement(sql.toString());

	        int idx = 1;
	        if (hasKey) {
	            pstmt.setString(idx++, "%" + keyword.trim() + "%"); // 부분 일치
	        }
	        pstmt.setInt(idx++, endRow);   // endRow 먼저
	        pstmt.setInt(idx,   startRow); // 그 다음 startRow

	        rs = pstmt.executeQuery();

	        List<Notice> list = new ArrayList<>();
	        while (rs.next()) {
	            Notice n = new Notice();
	            n.setNoticeId(rs.getLong("noticeid"));
	            n.setAuthorId(rs.getString("authorid"));
	            n.setTitle(rs.getString("title"));
	            n.setContent(rs.getString("content"));

	            java.sql.Timestamp ts = rs.getTimestamp("createdat");
	            if (ts != null) {
	                // Notice.createdAt 이 LocalDateTime 이면:
	                n.setCreatedAt(ts.toLocalDateTime());
	                // Date/Timestamp 타입을 쓰면 프로젝트 타입에 맞게 조정
	            }
	            list.add(n);
	        }
	        return list;

	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	        DBUtil.close(conn);
	    }
	}


    // ====== 단건 조회 ======
    public Notice findById(long noticeId) throws SQLException {
        System.out.println("[NoticeDao] findById(" + noticeId + ")");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT noticeid, authorid, title, content, createdat FROM notice WHERE noticeid = ?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, noticeId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            return null;
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }
    }


 // ====== 등록 ======
    public long insert(Notice notice) throws SQLException {
        System.out.println("[NoticeDao] insert() :: authorId=" + notice.getAuthorId());

        Connection conn = null;
        PreparedStatement p1 = null;
        PreparedStatement p2 = null;
        ResultSet rs = null;

        final String INSERT_SQL =
            "INSERT INTO notice (noticeid, authorid, title, content, createdat) " +
            "VALUES (seq_notice.NEXTVAL, ?, ?, ?, SYSDATE)";

        try {
            conn = DBUtil.getConnection();

            // 1) INSERT
            p1 = conn.prepareStatement(INSERT_SQL);
            p1.setString(1, notice.getAuthorId());
            p1.setString(2, notice.getTitle());
            p1.setString(3, notice.getContent());

            int updated = p1.executeUpdate();
            if (updated == 0) {
                throw new SQLException("공지 등록 실패");
            }

            // 2) 필요 시 커밋 (풀/드라이버에 따라 auto-commit=false일 수 있음)
            try {
                if (!conn.getAutoCommit()) {
                    conn.commit();
                    System.out.println("[NoticeDao] commit");
                }
            } catch (Exception ignore) {}

            // 3) 방금 발급된 PK 정확 취득
            DBUtil.close(p1);
            p2 = conn.prepareStatement("SELECT seq_notice.currval AS new_id FROM dual");
            rs = p2.executeQuery();
            if (rs.next()) {
                long newId = rs.getLong("new_id");
                System.out.println("[NoticeDao] newId=" + newId);
                return newId;
            }
            return -1L;

        } finally {
            DBUtil.close(rs);
            DBUtil.close(p2);
            DBUtil.close(p1);
            DBUtil.close(conn);
        }
    }

    // ====== 매핑 유틸 ======
    private Notice map(ResultSet rs) throws SQLException {
        Notice n = new Notice();
        n.setNoticeId(rs.getLong("noticeid"));
        n.setAuthorId(rs.getString("authorid"));
        n.setTitle(rs.getString("title"));
        n.setContent(rs.getString("content"));

        Timestamp ts = rs.getTimestamp("createdat");
        LocalDateTime ldt = (ts != null ? ts.toLocalDateTime() : null);
        n.setCreatedAt(ldt);

        return n;
    }
    
    /**
     * 공지 수정 (제목, 내용)
     */
    public int update(Notice notice) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE NOTICE SET TITLE = ?, CONTENT = ? WHERE NOTICEID = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, notice.getTitle());
            pstmt.setString(2, notice.getContent());
            pstmt.setLong(3, notice.getNoticeId());
            return pstmt.executeUpdate();
        } finally {
            DBUtil.close(pstmt, conn);
        }
    }

 // ====== 삭제 ======
    public int delete(long noticeId) throws SQLException {
        Connection conn = null; PreparedStatement pstmt = null;
        boolean oldAutoCommit = true; int result = 0;
        final String sql = "DELETE FROM notice WHERE noticeid = ?";
        try {
            conn = DBUtil.getConnection();
            oldAutoCommit = conn.getAutoCommit();
            if (!oldAutoCommit) conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, noticeId);
            result = pstmt.executeUpdate();

            if (!oldAutoCommit) conn.commit();
            return result;
        } catch (SQLException e) {
            if (conn!=null && !oldAutoCommit) try{ conn.rollback(); }catch(Exception ignore){}
            throw e;
        } finally {
            DBUtil.close(pstmt); DBUtil.close(conn);
        }
    }


}