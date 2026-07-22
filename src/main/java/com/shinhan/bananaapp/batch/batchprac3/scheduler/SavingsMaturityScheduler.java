//package com.shinhan.bananaapp.batch.batchprac3.scheduler;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.*;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.repository
//        .JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository
//        .JobInstanceAlreadyCompleteException;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import java.time.LocalDate;
//
//@Slf4j @Component @RequiredArgsConstructor
//public class SavingsMaturityScheduler {
//
//    private final JobLauncher jobLauncher;
//    private final Job         savingsMaturityJob;
//
//    // ── 운영: 매일 자정 0시 0분 0초 실행 ────────
//    // @Scheduled(cron = "0 0 0 * * *",
//    //            zone = "Asia/Seoul")
//
//    // ── 테스트: 10초마다 실행 ────────────────────
//    @Scheduled(cron = "0/10 * * * * *",
//            zone = "Asia/Seoul")
//    public void runSavingsMaturityJob() {
//
//        log.info("[스케줄러] 만기 적금 해지 배치 시작");
//        log.info("[실행 날짜] {}", LocalDate.now());
//
//        try {
//            JobParameters params =
//                    new JobParametersBuilder()
//                            // runAt 없음 → 하루 1회 실행 보장
//                            .addString("date",
//                                    LocalDate.now().toString())
//                            .toJobParameters();
//
//            JobExecution execution =
//                    jobLauncher.run(savingsMaturityJob, params);
//
//            log.info("[스케줄러] 완료 — 상태: {}",
//                    execution.getStatus());
//
//        } catch (JobInstanceAlreadyCompleteException e) {
//            // 오늘 이미 완료 → 스킵
//            log.warn("[스케줄러] 오늘 이미 완료 — 스킵: {}",
//                    LocalDate.now());
//
//        } catch (JobExecutionAlreadyRunningException e) {
//            // 현재 실행 중 → 중복 방지
//            log.warn("[스케줄러] 실행 중 — 중복 실행 방지");
//
//        } catch (Exception e) {
//            log.error("[스케줄러] 오류: {}",
//                    e.getMessage(), e);
//        }
//    }
//}
