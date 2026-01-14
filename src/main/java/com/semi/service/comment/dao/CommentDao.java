// src/main/java/com/semi/service/comment/dao/CommentDao.java
package com.semi.service.comment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.semi.common.util.DBUtil;
import com.semi.domain.Comment;

/**
 * 댓글 DAO (구체 클래스) - commentid: seq_comments.NEXTVAL - createdat: SYSDATE
 */
public class CommentDao {

	// 총 건수 조회 SQL
	private static final String SQL_COUNT = "SELECT COUNT(*) FROM comments WHERE volunteerid = ?";

	// 페이지 목록 조회 SQL(작성자 이름 조인) — 최신순
	private static final String SQL_SELECT_PAGE = "SELECT * FROM ( " + "  SELECT ROWNUM rn, t.* FROM ( "
			+ "    SELECT c.commentid, c.volunteerid, c.authorid, u.name AS authorname, "
			+ "           c.content, c.createdat " + "    FROM comments c "
			+ "    JOIN users u ON u.userid = c.authorid " + "    WHERE c.volunteerid = ? "
			+ "    ORDER BY c.createdat DESC, c.commentid DESC " + // ★ 변경: 최신순
			"  ) t WHERE ROWNUM <= ? " + ") WHERE rn >= ?";

	/** 등록(생성된 commentId 반환) */
	public long addComment(Comment c) throws Exception {
		final String insert = "INSERT INTO comments (commentid, authorid, content, volunteerid, createdat) "
				+ "VALUES (seq_comments.NEXTVAL, ?, ?, ?, SYSDATE)";
		final String fetchId = "SELECT seq_comments.CURRVAL FROM dual";

		try (Connection con = DBUtil.getConnection()) {
			try (PreparedStatement ps = con.prepareStatement(insert)) {
				int idx = 1;
				ps.setString(idx++, c.getAuthorId()); // DB authorid
				ps.setString(idx++, c.getContent());
				ps.setLong(idx, c.getVolunteerId()); // DB volunteerid
				ps.executeUpdate();
			}
			try (PreparedStatement ps2 = con.prepareStatement(fetchId); ResultSet rs = ps2.executeQuery()) {
				return rs.next() ? rs.getLong(1) : 0L;
			}
		}
	}

	/** 내용 수정 */
	public int updateComment(long commentId, String content) throws Exception {
		final String sql = "UPDATE comments SET content = ? WHERE commentid = ?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, content);
			ps.setLong(2, commentId);
			return ps.executeUpdate();
		}
	}

	/** 삭제 */
	public int deleteComment(long commentId) throws Exception {
		final String sql = "DELETE FROM comments WHERE commentid = ?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, commentId);
			return ps.executeUpdate();
		}
	}

	/**
	 * volunteerId 기준 총 건수
	 */
	public int countByVolunteer(long volunteerId) throws Exception {
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_COUNT)) {
			ps.setLong(1, volunteerId);
			try (ResultSet rs = ps.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}
		}
	}

	/**
	 * volunteerId 기준 페이지 목록
	 * 
	 * @param volunteerId 대상 게시글 ID
	 * @param page        현재 페이지(1부터)
	 * @param pageSize    페이지당 행수
	 */
	public List<Comment> selectPageByVolunteer(long volunteerId, int page, int pageSize) throws Exception {
		int endRow = page * pageSize;
		int startRow = endRow - pageSize + 1;

		List<Comment> list = new ArrayList<>();
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_SELECT_PAGE)) {

			ps.setLong(1, volunteerId);
			ps.setInt(2, endRow);
			ps.setInt(3, startRow);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Comment c = new Comment();
					c.setCommentId(rs.getLong("commentid"));
					c.setVolunteerId(rs.getLong("volunteerid"));
					c.setAuthorId(rs.getString("authorid"));
					c.setAuthorName(rs.getString("authorname"));
					c.setContent(rs.getString("content"));
					Timestamp ts = rs.getTimestamp("createdat");
					c.setCreatedAt(ts != null ? new java.util.Date(ts.getTime()) : null);
					list.add(c);
				}
			}
		}
		return list;
	}
}
