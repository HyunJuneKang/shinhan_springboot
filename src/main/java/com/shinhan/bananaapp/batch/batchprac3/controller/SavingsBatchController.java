package com.shinhan.bananaapp.batch.batchprac3.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository
        .JobInstanceAlreadyCompleteException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Slf4j @RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class SavingsBatchController {

    private final JobLauncher jobLauncher;
    private final Job         savingsMaturityJob;

    @GetMapping("/savings")
    public ResponseEntity<String> runSavingsBatch()
            throws Exception {

        JobParameters params = new JobParametersBuilder()
                .addString("date",
                        LocalDate.now().toString())   // 날짜 파라미터
                .addLong("runAt",
                        System.currentTimeMillis())   // 재실행 허용
                .toJobParameters();

        try {
            JobExecution execution =
                    jobLauncher.run(savingsMaturityJob, params);
            return ResponseEntity.ok(
                    "배치 완료 - 상태: " + execution.getStatus());
        } catch (JobInstanceAlreadyCompleteException e) {
            return ResponseEntity.ok("이미 완료된 배치");
        }
    }
}
