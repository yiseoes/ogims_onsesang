// src/main/java/com/semi/common/util/DBUtil.java
package com.semi.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {
	
	///Field
	private final static String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private final static String JDBC_URL = "jdbc:oracle:thin:ogims/ogims@localhost:1521:xe";
	
	///Constructor
	private DBUtil(){
	}
	
	///Method : 연결 획득
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(JDBC_URL);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	// ====== 자원 해제 유틸(조용히 닫기) ======

	/** ResultSet 닫기 */
	public static void close(ResultSet rs) {
		if (rs != null) {
			try { rs.close(); } catch (SQLException ignore) {}
		}
	}

	/** Statement 닫기 (PreparedStatement 포함) */
	public static void close(Statement stmt) {
		if (stmt != null) {
			try { stmt.close(); } catch (SQLException ignore) {}
		}
	}

	/** PreparedStatement 닫기 (가독성용 오버로드) */
	public static void close(PreparedStatement pstmt) {
		if (pstmt != null) {
			try { pstmt.close(); } catch (SQLException ignore) {}
		}
	}

	/** Connection 닫기 */
	public static void close(Connection conn) {
		if (conn != null) {
			try { conn.close(); } catch (SQLException ignore) {}
		}
	}

	/** ResultSet, Statement, Connection 순서로 닫기 */
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		close(rs);
		close(stmt);
		close(conn);
	}

	/** Statement, Connection 순서로 닫기 */
	public static void close(Statement stmt, Connection conn) {
		close(stmt);
		close(conn);
	}

	/** PreparedStatement, Connection 순서로 닫기 */
	public static void close(PreparedStatement pstmt, Connection conn) {
		close(pstmt);
		close(conn);
	}
}
