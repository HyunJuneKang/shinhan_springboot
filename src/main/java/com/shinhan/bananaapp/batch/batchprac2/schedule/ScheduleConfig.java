package com.shinhan.bananaapp.batch.batchprac2.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ScheduleConfig {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final Job settlementJob;

    // 매일 새벽 1시 (한국 시간)
    // 테스트 시: "0/10 * * * * *" (10초마다)
//Main에 @EnableScheduling
    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul")
    public void runSettlementJob() {
        String targetDate = LocalDate.now().minusDays(1).toString();
        log.info("[Scheduler] 정산 배치 시작 - targetDate: {}", targetDate);
        try {
            // TABLE : batch_job_execution_params
            JobParameters params = new JobParametersBuilder()
                    .addString("targetDate", targetDate)
                    .addLong("runAt", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(settlementJob, params);
        } catch (Exception e) {
            log.error("[Scheduler] 배치 실행 오류", e);
        }
    }
}
