// src/main/java/com/semi/batch/QuartzBootListener.java
package com.semi.batch;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 웹앱 시작/종료 시점에 Quartz 스케줄러를 제어하는 리스너
 * - 서버 기동 시 : 스케줄러 생성/작동, Job+Trigger 등록
 * - 서버 종료 시 : 스케줄러 안전 종료
 */
public class QuartzBootListener implements ServletContextListener {

    private Scheduler scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // 1) 스케줄러 생성
            scheduler = StdSchedulerFactory.getDefaultScheduler();

            // 2) Job 정의
            JobDetail job = JobBuilder.newJob(VolRequestStatusJob.class)
                    .withIdentity("volReqStatusJob", "system")
                    .build();

            // 3) Trigger : 즉시 시작, 10분 간격 무한 반복
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("volReqStatusTrigger", "system")
                    .startNow()
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInMinutes(10)
                                    .repeatForever()
                    )
                    .build();

            // 4) 스케줄 등록 및 시작
            scheduler.scheduleJob(job, trigger);
            scheduler.start();

            System.out.println("[QuartzBootListener] 스케줄러 시작됨");

        } catch (Exception e) {
            throw new RuntimeException("Quartz 초기화 실패", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            try {
                // 기존 실행중인 Job 마무리까지 기다릴지 여부는 상황에 맞게
                scheduler.shutdown(true);
                System.out.println("[QuartzBootListener] 스케줄러 정상 종료");
            } catch (Exception ignore) {}
        }
    }
}
