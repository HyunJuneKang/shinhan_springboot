package com.shinhan.bananaapp.batch.batchprac2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Slf4j @RestController @RequiredArgsConstructor
public class SettlementApiController {

    private final JobLauncher jobLauncher;
    private final Job         settlementJob;

    // GET http://localhost:8000/batch/settlement
    // GET http://localhost:8000/batch/settlement?targetDate=2026-07-11
    @GetMapping("/batch/settlement")
    public ResponseEntity<String> runSettlement(
            @RequestParam(required = false) String targetDate)
            throws Exception {

        String date = targetDate != null ? targetDate
                : LocalDate.now().minusDays(1).toString();

        JobParameters params = new JobParametersBuilder()
                .addString("targetDate", date)
                .addLong("runAt", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(settlementJob, params);
        return ResponseEntity.ok("정산 배치 실행 완료 - targetDate: " + date);
    }
}
