package com.shinhan.bananaapp.batch.batchprac2.config;

import com.shinhan.bananaapp.batch.batchprac2.entity.SettlementEntity;
import com.shinhan.bananaapp.batch.batchprac2.entity.TransactionEntity;
import com.shinhan.bananaapp.batch.batchprac2.listener.SettlementJobListener;
import com.shinhan.bananaapp.batch.batchprac2.listener.SettlementStepListener;
import com.shinhan.bananaapp.batch.batchprac2.repository.SettlementRepository;
import com.shinhan.bananaapp.batch.batchprac2.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Slf4j @Configuration @RequiredArgsConstructor
public class SettlementBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final TransactionRepository transactionRepository;
    private final SettlementRepository settlementRepository;
    private final SettlementJobListener jobListener;
    private final SettlementStepListener stepListener;

    private static final BigDecimal DEPOSIT_RATE  = new BigDecimal("0.001");
    private static final BigDecimal WITHDRAW_RATE = new BigDecimal("0.002");

    // ── Job ──────────────────────────────────────
    @Bean
    public Job settlementJob() {
        return new JobBuilder("settlementJob", jobRepository)
// BATCH_JOB_INSTANCE 에 "settlementJob" 으로 등록
                .listener(jobListener)  // beforeJob / afterJob 메서드 실행
                .start(settlementStep())
// Step이 1개이므로 start() 만 사용, Step이 여러 개면 .next() 로 연결
                .build();
    }

    // ── Step (chunk=10) ──────────────────────────
    @Bean
    public Step settlementStep() {
// TransactionEntity는 read반환타입, SettlementEntity는 write반환타입
        return new StepBuilder("settlementStep", jobRepository)
                .<TransactionEntity, SettlementEntity>chunk(10, platformTransactionManager)
                .reader(transactionReader())
                .processor(settlementProcessor())
                .writer(settlementWriter())
                .listener(stepListener)
                .build();
    }

    // ── ItemReader — 전날 미정산 거래 페이징 조회 ──
    @Bean
    public RepositoryItemReader<TransactionEntity> transactionReader() {
        LocalDate     yesterday = LocalDate.now().minusDays(1);
        LocalDateTime start     = yesterday.atStartOfDay();
        LocalDateTime end       = yesterday.atTime(LocalTime.MAX);

        return new RepositoryItemReaderBuilder<TransactionEntity>()
                .name("transactionReader")
                .pageSize(10)
                .methodName("findBySettledFalseAndTxDateBetween")
                .arguments(List.of(start, end))
                .repository(transactionRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    // ── ItemProcessor — 수수료 계산 ──────────────
    @Bean
    public ItemProcessor<TransactionEntity, SettlementEntity> settlementProcessor() {
        return item -> {
            // 금액 0원 → null 반환 = 필터링
            if (item.getAmount() == null || item.getAmount() <= 0)
                return null;

            BigDecimal rate = "DEPOSIT".equals(item.getTxType())
                    ? DEPOSIT_RATE : WITHDRAW_RATE;

            long fee = BigDecimal.valueOf(item.getAmount())
                    .multiply(rate).longValue();

            log.info("[Processor] {} {} → 수수료 {}원",
                    item.getAccountNo(), item.getTxType(), fee);

            return SettlementEntity.builder()
                    .accountNo(item.getAccountNo())
                    .txType(item.getTxType())
                    .amount(item.getAmount())
                    .fee(fee)
                    .settlementDate(item.getTxDate().toLocalDate())
                    .build();
        };
    }

    // ── ItemWriter ───────────────────────────────
    @Bean
    public RepositoryItemWriter<SettlementEntity> settlementWriter() {
        return new RepositoryItemWriterBuilder<SettlementEntity>()
                .repository(settlementRepository)
                .methodName("save")
                .build();
    }
}
