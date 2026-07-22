package com.shinhan.bananaapp.batch.batchprac3.listener;

import com.shinhan.bananaapp.batch.batchprac3.repository.SavingsMaturityRepository;
import com.shinhan.bananaapp.batch.batchprac3.repository.SavingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class SavingsJobListener implements JobExecutionListener {

    private final SavingsRepository savingsRepository;
    private final SavingsMaturityRepository maturityRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LocalDate today = LocalDate.now();
        int count = savingsRepository
                .countByMaturityDateAndStatus(today, "ACTIVE");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("[만기 적금 배치 시작]");
        log.info("[처리 대상] 오늘({}) 만기 적금: {}건", today, count);
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    @Transactional  // ← @Modifying 쿼리를 위해 필수
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() != BatchStatus.COMPLETED) {
            log.error("[배치 실패] 상태: {}",
                    jobExecution.getStatus());
            return;
        }
        LocalDate today = LocalDate.now();

        int  processed    = maturityRepository
                .countByMaturityDate(today);
        Long totalInterest = maturityRepository
                .sumInterestByMaturityDate(today);
        Long totalAmount   = maturityRepository
                .sumTotalAmountByMaturityDate(today);

        // ACTIVE → CLOSED 일괄 변경
        int closed = savingsRepository.closeMaturedSavings(today);

        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("[만기 적금 배치 완료]");
        log.info("[CLOSED 처리]  {}건 → CLOSED", closed);
        log.info("[처리 건수]    {}건", processed);
        log.info("[지급 이자]    총 {}원", totalInterest);
        log.info("[지급 원리금]  총 {}원", totalAmount);
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
}
