// src/main/java/com/semi/batch/VolRequestStatusJob.java
package com.semi.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.semi.common.util.DBUtil;

/**
 * 봉사요청 상태 자동 갱신 작업
 * - 목적 : 종료 시각(endtime)이 지난 요청글의 상태를 일괄 처리
 * - 규칙 :
 *   1) status='모집중'  AND endtime<SYSDATE  -> '만료'
 *   2) status='모집완료' AND endtime<SYSDATE -> '봉사완료'
 * - 대상 : volunteer 테이블 중 flag='r' (요청글)
 */
public class VolRequestStatusJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Connection con = null;
        PreparedStatement psExpire = null;
        PreparedStatement psComplete = null;

        // 각각 몇 건 변경됐는지 로깅용
        int expired = 0;
        int completed = 0;

        // 상태 변경용 SQL
        final String SQL_EXPIRE = 
            "UPDATE volunteer " +
            "   SET status = '만료' " +
            " WHERE endtime < SYSDATE " +
            "   AND status IN ('모집중')";

        final String SQL_COMPLETE = 
            "UPDATE volunteer " +
            "   SET status = '봉사완료' " +
            " WHERE endtime < SYSDATE " +
            "   AND status IN ('모집완료')";

        try {
            // 1) 공통모듈로 DB 접속
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            // 2) 종료 지난 모집중 -> 만료
            psExpire = con.prepareStatement(SQL_EXPIRE);
            expired = psExpire.executeUpdate();

            // 3) 종료 지난 모집완료 -> 봉사완료
            psComplete = con.prepareStatement(SQL_COMPLETE);
            completed = psComplete.executeUpdate();

            // 4) 커밋
            con.commit();

            // 로깅(필요 시 SLF4J 사용)
            System.out.println("[VolRequestStatusJob] 만료 처리 : " + expired + "건, 봉사완료 처리 : " + completed + "건");

        } catch (Exception e) {
            // 롤백 안전 처리
            if (con != null) {
                try { con.rollback(); } catch (SQLException ignore) {}
            }
            throw new JobExecutionException("VolRequestStatusJob 실패", e);

        } finally {
            // 5) DB 연결 안전 종료
            DBUtil.close(psComplete);
            DBUtil.close(psExpire);
            DBUtil.close(con);
        }
    }
}
