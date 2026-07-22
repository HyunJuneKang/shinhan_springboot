package com.shinhan.bananaapp.batch.batchprac1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FirstApiController {

    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final Job firstJob;   // @Bean 이름으로 주입

    // GET http://localhost:8080/batch/first?value=run-001
    @GetMapping("/batch/first")
    public ResponseEntity<String> firstBatch(
            @RequestParam("value") String value) throws Exception {
        // BATCH_JOB_EXECUTION_PARAMS 테이블에 param이름,값
        JobParameters params = new JobParametersBuilder()
                .addString("date", value)
                .toJobParameters();

        // 같은 파라미터 재실행 → COMPLETED 이면 스킵
        jobLauncher.run(firstJob, params);
        return ResponseEntity.ok("배치 실행 완료 - " + value);
    }
}
