// src/main/java/com/semi/service/volOffer/dao/VolOfferDao.java
package com.semi.service.volOffer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.semi.common.Page;
import com.semi.common.Search;
import com.semi.common.util.DBUtil;
import com.semi.domain.VolOffer;
import com.semi.service.volOffer.dto.VolOfferListItem;
import com.semi.service.volRequest.dto.VolRequestListItem;


public class VolOfferDao {
	
	///Field 
	
	///Constructor
	public VolOfferDao() {
	}

	///Method
	// === 목록 (Map 조립) ===
	public Map<String, Object> getVolOfferList(Search search, String category, int pageUnit, String status,
			String regionLock) throws Exception {
		final int currentPage = Math.max(1, search != null ? search.getCurrentPage() : 1);
		final int pageSize = Math.max(1, search != null ? search.getPageSize() : 10);
		final int beginRow = (currentPage - 1) * pageSize + 1;
		final int endRow = currentPage * pageSize;

		final int totalCount = countVolOffers(search, category, status, regionLock);
		final Page page = new Page(currentPage, totalCount, pageUnit, pageSize);

		final List<VolOfferListItem> list = (totalCount > 0)
				? fetchVolOfferList(search, category, status, regionLock, beginRow, endRow)
				: java.util.Collections.emptyList();

		Map<String, Object> map = new java.util.HashMap<>();
		map.put("totalCount", totalCount);
		map.put("page", page);
		map.put("list", list);
		map.put("search", search);
		return map;
	}

	// === 총건수 ===
	private int countVolOffers(Search search, String category, String status, String regionLock) throws Exception {
		StringBuilder where = new StringBuilder(" WHERE v.flag = 'o' ");
		java.util.List<Object> params = new java.util.ArrayList<>();

		if (notEmpty(status)) {
			where.append(" AND v.status = ? ");
			params.add(status.trim());
		}
		if (notEmpty(category)) {
			where.append(" AND v.category = ? ");
			params.add(category.trim());
		}
		if (notEmpty(regionLock)) {
			where.append(" AND v.region = ? ");
			params.add(regionLock.trim());
		} // ← 고정지역

		applySearchCondition(where, params, search); // title/author/authorId/region/date

		final String sql = "SELECT COUNT(*) FROM volunteer v JOIN users u ON u.userid = v.authorid " + where.toString();

		try (java.sql.Connection con = DBUtil.getConnection();
				java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
			bindParams(ps, params);
			try (java.sql.ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getInt(1) : 0;
			}
		}
	}

	// === 목록 조회 ===
	private java.util.List<VolOfferListItem> fetchVolOfferList(Search search, String category, String status,
			String regionLock, int beginRow, int endRow) throws Exception {

		StringBuilder where = new StringBuilder(" WHERE v.flag = 'o' ");
		java.util.List<Object> params = new java.util.ArrayList<>();

		if (notEmpty(status)) {
			where.append(" AND v.status = ? ");
			params.add(status.trim());
		}
		if (notEmpty(category)) {
			where.append(" AND v.category = ? ");
			params.add(category.trim());
		}
		if (notEmpty(regionLock)) {
			where.append(" AND v.region = ? ");
			params.add(regionLock.trim());
		} // ← 고정지역

		applySearchCondition(where, params, search);

		final StringBuilder inner = new StringBuilder();
		inner.append("SELECT v.volunteerid, v.title, u.name AS author_name, ")
				.append("       v.category, v.starttime, v.status, ")
				.append("       ROW_NUMBER() OVER (ORDER BY v.createdat DESC) AS rn ").append("  FROM volunteer v ")
				.append("  JOIN users u ON u.userid = v.authorid ").append(where.toString());

		final String sql = "SELECT volunteerid, title, author_name, category, starttime, status " + "  FROM (" + inner
				+ ") WHERE rn BETWEEN ? AND ?";

		java.util.List<VolOfferListItem> list = new java.util.ArrayList<>();
		try (java.sql.Connection con = DBUtil.getConnection();
				java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

			int idx = bindParams(ps, params);
			ps.setInt(idx++, beginRow);
			ps.setInt(idx, endRow);

			try (java.sql.ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					VolOfferListItem item = new VolOfferListItem();
					item.setVolunteerId(rs.getLong("volunteerid"));
					item.setTitle(rs.getString("title"));
					item.setAuthorName(rs.getString("author_name"));
					item.setCategory(rs.getString("category"));
					java.sql.Timestamp st = rs.getTimestamp("starttime");
					if (st != null) {
						java.time.LocalDateTime ldt = st.toLocalDateTime();
						item.setDate(ldt.toLocalDate());
						item.setStartTime(ldt.toLocalTime());
					}
					item.setStatus(rs.getString("status"));
					list.add(item);
				}
			}
		}
		return list;
	}

	// ========= 검색 WHERE 동적 조립 =========
	private void applySearchCondition(StringBuilder where, List<Object> params, Search search) {
		if (search == null)
			return;
		String cond = safe(search.getSearchCondition());
		String keyword = safe(search.getSearchKeyword());
		if (keyword.isEmpty() || cond.isEmpty())
			return;

		switch (cond.toLowerCase()) {
		case "title":
			where.append(" AND v.title LIKE ? ");
			params.add("%" + keyword + "%");
			break;
		case "region":
			where.append(" AND v.region LIKE ? ");
			params.add("%" + keyword + "%");
			break;
		case "author": // 작성자 '이름' LIKE
			where.append(" AND u.name LIKE ? ");
			params.add("%" + keyword + "%");
			break;
		case "authorid": // 작성자 '아이디' 정확일치
			where.append(" AND v.authorid = ? ");
			params.add(keyword);
			break;
		case "date":
			String[] parts = keyword.split("~");
			if (parts.length == 2 && notEmpty(parts[0]) && notEmpty(parts[1])) {
				where.append(" AND TRUNC(v.starttime) BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') ");
				params.add(parts[0].trim());
				params.add(parts[1].trim());
			} else {
				where.append(" AND TRUNC(v.starttime) = TO_DATE(?, 'YYYY-MM-DD') ");
				params.add(keyword.trim());
			}
			break;
		default: /* no-op */
		}
	}

	// ========= 공통 유틸 =========
	private int bindParams(PreparedStatement ps, List<Object> params) throws SQLException {
		int idx = 1;
		for (Object p : params)
			ps.setObject(idx++, p);
		return idx;
	}

	private static boolean notEmpty(String s) {
		return s != null && !s.trim().isEmpty();
	}

	private static String safe(String s) {
		return (s == null) ? "" : s.trim();
	}
	
	
	public void insertVolOffer(VolOffer vo) throws Exception {
	    Connection con = null;
	    PreparedStatement pStmt = null;
	    try {
	        con = DBUtil.getConnection();

	        String sql =
	            "INSERT INTO volunteer (" +
	            "  volunteerid, authorid, title, content, phone, region, category," +
	            "  starttime, endtime, status, flag, image, createdat" +
	            ") VALUES (" +
	            "  seq_volunteer.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'o', ?, ?" +
	            ")";

	        pStmt = con.prepareStatement(sql);

	        int idx = 1;
	        pStmt.setString(idx++, vo.getAuthorId());
	        pStmt.setString(idx++, vo.getTitle());
	        pStmt.setString(idx++, vo.getContent());
	        pStmt.setString(idx++, vo.getPhone());
	        pStmt.setString(idx++, vo.getRegion());
	        pStmt.setString(idx++, vo.getCategory());
	        pStmt.setTimestamp(idx++, vo.getStartTime() == null ? null : Timestamp.valueOf(vo.getStartTime()));
	        pStmt.setTimestamp(idx++, vo.getEndTime()   == null ? null : Timestamp.valueOf(vo.getEndTime()));
	        pStmt.setString(idx++, vo.getStatus());
	        pStmt.setString(idx++, vo.getImage()); // ★ NEW: 이미지 파일명
	        pStmt.setTimestamp(idx++, Timestamp.valueOf(java.time.LocalDateTime.now())); // createdat

	        int affected = pStmt.executeUpdate();
	        System.out.println("[VolOfferDao] insert affected=" + affected);
	    } finally {
	        try { if (pStmt != null) pStmt.close(); } catch (Exception ignore) {}
	        try { if (con != null) con.close(); } catch (Exception ignore) {}
	    }
	}


	public VolOffer findVolOffer(Long postId) throws Exception {    // DetailVolOffer
	    System.out.println("VolOfferDao - findVolOffer");
	    try (Connection con = DBUtil.getConnection()) {

	        String sql =
	            "SELECT " +
	            "  volunteerid, authorid, title, content, phone, region, category, " +
	            "  starttime, endtime, status, flag, image, createdat " +          // ★ image, flag 포함
	            "FROM volunteer " +
	            "WHERE flag = 'o' AND volunteerid = ?";

	        System.out.println("VolOfferDao - findVolOffer : " + sql);

	        try (PreparedStatement pStmt = con.prepareStatement(sql)) {
	            pStmt.setLong(1, postId);

	            try (ResultSet rs = pStmt.executeQuery()) {
	                VolOffer volOffer = null;
	                if (rs.next()) {
	                    volOffer = new VolOffer();
	                    volOffer.setPostId(rs.getLong("volunteerid"));
	                    volOffer.setAuthorId(rs.getString("authorid"));
	                    volOffer.setTitle(rs.getString("title"));
	                    volOffer.setContent(rs.getString("content"));
	                    volOffer.setPhone(rs.getString("phone"));
	                    volOffer.setRegion(rs.getString("region"));
	                    volOffer.setCategory(rs.getString("category"));

	                    // null-safe timestamp → LocalDateTime
	                    java.sql.Timestamp st = rs.getTimestamp("starttime");
	                    java.sql.Timestamp et = rs.getTimestamp("endtime");
	                    java.sql.Timestamp ct = rs.getTimestamp("createdat");
	                    if (st != null) volOffer.setStartTime(st.toLocalDateTime());
	                    if (et != null) volOffer.setEndTime(et.toLocalDateTime());
	                    if (ct != null) volOffer.setCreatedAt(ct.toLocalDateTime());

	                    volOffer.setStatus(rs.getString("status"));
	                    volOffer.setOfferFlag(rs.getString("flag"));   // ★ flag 매핑
	                    volOffer.setImage(rs.getString("image"));      // ★ image 매핑
	                }
	                return volOffer;
	            }
	        }
	    }
	}


	public Map<String , Object> getVolOfferList(Search search, String region) throws Exception {
		Map<String , Object> map = new HashMap<String, Object>();
		List<Object> params = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT VOLUNTEERID, AUTHORID, '(' || CATEGORY || ')' || TITLE AS CAT_TITLE, ");
		sql.append("CREATEDAT, STARTTIME, ENDTIME, REGION, STATUS, FLAG ");
		sql.append("FROM VOLUNTEER WHERE flag = 'o' ");

		if (search.getSearchCondition() != null && search.getSearchKeyword() != null
				&& !search.getSearchKeyword().trim().isEmpty()) {
			String condition = search.getSearchCondition();
			String keyword = search.getSearchKeyword().trim();
			if ("0".equals(condition)) {
				sql.append(" AND TITLE LIKE ? ");
				params.add("%" + keyword + "%");
			} else if ("1".equals(condition)) {
				sql.append(" AND AUTHORID LIKE ? ");
				params.add("%" + keyword + "%");
			}
		}

		if (region != null && !region.isEmpty()) {
			sql.append(" AND REGION = ? ");
			params.add(region);
		}

		sql.append(" ORDER BY VOLUNTEERID DESC");

		int totalCount = getTotalCountSafe(sql.toString(), params);

		String pagedSql = makeCurrentPageSqlSafe(sql.toString(), search);

		try (Connection con = DBUtil.getConnection();
			 PreparedStatement pStmt = con.prepareStatement(pagedSql)) {

			int idx = 1;
			for (Object param : params) {
				pStmt.setObject(idx++, param);
			}

			try (ResultSet rs = pStmt.executeQuery()) {
				List<VolOffer> list = new ArrayList<VolOffer>();
				while(rs.next()){
					VolOffer volOffer = new VolOffer();
					volOffer.setPostId(rs.getLong("VOLUNTEERID"));
					volOffer.setAuthorId(rs.getString("AUTHORID"));
					volOffer.setTitle(rs.getString("CAT_TITLE"));
					Timestamp createdAt = rs.getTimestamp("CREATEDAT");
					Timestamp startTime = rs.getTimestamp("STARTTIME");
					Timestamp endTime = rs.getTimestamp("ENDTIME");
					if (createdAt != null) volOffer.setCreatedAt(createdAt.toLocalDateTime());
					if (startTime != null) volOffer.setStartTime(startTime.toLocalDateTime());
					if (endTime != null) volOffer.setEndTime(endTime.toLocalDateTime());
					volOffer.setRegion(rs.getString("REGION"));
					volOffer.setStatus(rs.getString("STATUS"));
					volOffer.setOfferFlag(rs.getString("FLAG"));
					list.add(volOffer);
				}
				map.put("totalCount", Integer.valueOf(totalCount));
				map.put("list", list);
			}
		}
		return map;
	}

	private int getTotalCountSafe(String sql, List<Object> params) throws Exception {
		String countSql = "SELECT COUNT(*) FROM ( " + sql + " ) countTable";
		try (Connection con = DBUtil.getConnection();
			 PreparedStatement pStmt = con.prepareStatement(countSql)) {
			int idx = 1;
			for (Object param : params) {
				pStmt.setObject(idx++, param);
			}
			try (ResultSet rs = pStmt.executeQuery()) {
				return rs.next() ? rs.getInt(1) : 0;
			}
		}
	}

	private String makeCurrentPageSqlSafe(String sql, Search search) {
		int currentPage = search.getCurrentPage();
		int pageSize = search.getPageSize();
		int endRow = currentPage * pageSize;
		int startRow = (currentPage - 1) * pageSize + 1;
		return "SELECT * FROM (SELECT inner_table.*, ROWNUM AS row_seq FROM (" + sql + ") inner_table WHERE ROWNUM <= " + endRow + ") WHERE row_seq BETWEEN " + startRow + " AND " + endRow;
	}

	public void updateVolOffer(VolOffer vo) throws Exception {
	    System.out.println("VolOfferDao - updateVolOffer 진행중");

	    Connection con = null;
	    PreparedStatement pStmt = null;
	    try {
	        con = DBUtil.getConnection();

	        boolean withImage = (vo.getImage() != null && !vo.getImage().isEmpty());

	        StringBuilder sql = new StringBuilder();
	        sql.append("UPDATE volunteer SET ")
	           .append(" title=?, content=?, phone=?, region=?, category=?, ")
	           .append(" starttime=?, endtime=?");

	        if (withImage) {
	            sql.append(", image=?"); // ★ NEW: 새 이미지 선택 시에만 갱신
	        }

	        sql.append(" WHERE volunteerid=? AND UPPER(flag)='O' AND authorid=?");

	        pStmt = con.prepareStatement(sql.toString());

	        int idx = 1;
	        pStmt.setString(idx++, vo.getTitle());
	        pStmt.setString(idx++, vo.getContent());
	        pStmt.setString(idx++, vo.getPhone());
	        pStmt.setString(idx++, vo.getRegion());
	        pStmt.setString(idx++, vo.getCategory());
	        pStmt.setTimestamp(idx++, vo.getStartTime() == null ? null : Timestamp.valueOf(vo.getStartTime()));
	        pStmt.setTimestamp(idx++, vo.getEndTime()   == null ? null : Timestamp.valueOf(vo.getEndTime()));

	        if (withImage) {
	            pStmt.setString(idx++, vo.getImage()); // ★ NEW
	        }

	        pStmt.setLong(idx++, vo.getPostId());       // volunteerid
	        pStmt.setString(idx++, vo.getAuthorId());   // authorid (작성자 보호)

	        System.out.println("[VolOfferDao] SQL = " + sql);
	        int rows = pStmt.executeUpdate();
	        System.out.println("[VolOfferDao] update affected=" + rows);

	    } finally {
	        try { if (pStmt != null) pStmt.close(); } catch (Exception ignore) {}
	        try { if (con != null) con.close(); } catch (Exception ignore) {}
	    }
	}
	

	
	//----------------------
    /** * 작성자 ID 조회
     * - 삭제/권한검증 전에 호출
     */
    public String findAuthorIdById(Long volunteerId) throws Exception {
        System.out.println("[VolOfferDao] findAuthorIdById 호출 :: volunteerId=" + volunteerId);
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = DBUtil.getConnection();
            String sql = "SELECT authorid FROM volunteer WHERE volunteerid = ? AND UPPER(flag) = 'O'";
            pStmt = con.prepareStatement(sql);
            pStmt.setLong(1, volunteerId);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                String authorId = rs.getString(1);
                System.out.println("[VolOfferDao] 작성자 조회 성공 :: authorId=" + authorId);
                return authorId;
            }
            System.out.println("[VolOfferDao] 대상 없음");
            return null;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignore) {}
            try { if (pStmt != null) pStmt.close(); } catch (Exception ignore) {}
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }

    /**
     * 봉사제공 삭제(물리 삭제)
     * - 제공글만 대상(UPPER(flag)='O')
     * 영향 건수(0/1)
     */
    public int deleteVolOffer(Long volunteerId) throws Exception {
        System.out.println("[VolOfferDao] deleteVolOffer 호출 :: volunteerId=" + volunteerId);
        Connection con = null;
        PreparedStatement pStmt = null;
        try {
            con = DBUtil.getConnection();
            String sql = "DELETE FROM volunteer WHERE volunteerid = ? AND UPPER(flag) = 'O'";
            pStmt = con.prepareStatement(sql);
            pStmt.setLong(1, volunteerId);
            int affected = pStmt.executeUpdate();
            System.out.println("[VolOfferDao] 삭제 결과 :: affected=" + affected);
            return affected;
        } finally {
            try { if (pStmt != null) pStmt.close(); } catch (Exception ignore) {}
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }

    public int updateProcessVolOffer(long volunteerId) throws Exception {
		final String sql = "UPDATE volunteer SET status = '모집완료' WHERE volunteerid=? AND flag='o' AND status='모집중'";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, volunteerId);
			return ps.executeUpdate();
		}
	}
    
}