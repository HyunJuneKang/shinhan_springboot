package com.shinhan.bananaapp.batch.batchprac2.listener;

import com.shinhan.bananaapp.batch.batchprac2.repository.SettlementRepository;
import com.shinhan.bananaapp.batch.batchprac2.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Component
@Slf4j
public class SettlementJobListener implements JobExecutionListener {

    private final TransactionRepository transactionRepository;
    private final SettlementRepository settlementRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("========================================");
        log.info("[Job 시작] {}", jobExecution.getJobInstance().getJobName());
        log.info("[파라미터] {}", jobExecution.getJobParameters());
        log.info("========================================");
    }
    // /batch/settlement?targetDate=2027-07-12
// 매일 새벽 1시 실행 → 전날 거래 정산..........Transaction Table의 settled = true
    @Override @Transactional
    public void afterJob(JobExecution jobExecution) {
        String dateParam = jobExecution.getJobParameters()
                .getString("targetDate");
        LocalDate targetDate = dateParam != null
                ? LocalDate.parse(dateParam)
                : LocalDate.now().minusDays(1); //파라메터없으면 어제

        LocalDateTime start = targetDate.atStartOfDay();//하루시작
        LocalDateTime end   = targetDate.atTime(LocalTime.MAX);//끝시간

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
// 금액 0원이라 정산 안 되지만 재처리 방지를 위해 settled는 true 처리
            int  updated = transactionRepository.markAsSettled(start, end);
//오늘 정산된 건수 확인
            long count   = settlementRepository.countBySettlementDate(targetDate);
            //오늘 수수료 합계 확인
            Long fee     = settlementRepository.sumFeeBySettlementDate(targetDate);
            log.info("[Job 완료] 정산 기준일: {}", targetDate);
            log.info("[settled 처리] {}건 → settled=true", updated);
            log.info("[정산 결과] {}건 / 총 수수료: {}원", count, fee);
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            jobExecution.getAllFailureExceptions()
                    .forEach(e -> log.error("[실패 원인] {}", e.getMessage()));
        }
    }
}
