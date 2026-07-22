package com.shinhan.bananaapp.batch.batchprac3.config;

import com.shinhan.bananaapp.batch.batchprac3.entity.SavingsEntity;
import com.shinhan.bananaapp.batch.batchprac3.entity.SavingsMaturityEntity;
import com.shinhan.bananaapp.batch.batchprac3.listener.SavingsJobListener;
import com.shinhan.bananaapp.batch.batchprac3.repository.SavingsMaturityRepository;
import com.shinhan.bananaapp.batch.batchprac3.repository.SavingsRepository;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j @Configuration
@RequiredArgsConstructor
public class SavingsMaturityBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SavingsRepository savingsRepository;
    private final SavingsMaturityRepository maturityRepository;
    private final SavingsJobListener jobListener;

    // ── Job ──────────────────────────────────────
    @Bean
    public Job savingsMaturityJob() {
        return new JobBuilder("savingsMaturityJob", jobRepository)
                .listener(jobListener)
                .start(savingsMaturityStep())
                .build();
    }

    // ── Step (chunk=10) ──────────────────────────
    @Bean
    public Step savingsMaturityStep() {
        return new StepBuilder("savingsMaturityStep", jobRepository)
                .<SavingsEntity, SavingsMaturityEntity>chunk(
                        10, transactionManager)
                .reader(savingsReader())
                .processor(savingsProcessor())
                .writer(maturityWriter())
                .build();
    }

    // ── Reader — 오늘 만기 ACTIVE 적금 페이징 조회 ──
    @Bean
    public RepositoryItemReader<SavingsEntity> savingsReader() {
        LocalDate today = LocalDate.now();
        return new RepositoryItemReaderBuilder<SavingsEntity>()
                .name("savingsReader")
                .pageSize(10)
                .methodName("findByMaturityDateAndStatus")
                .arguments(List.of(today, "ACTIVE"))
                .repository(savingsRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    // ── Processor — 이자 계산 ─────────────────────
    @Bean
    public ItemProcessor<SavingsEntity, SavingsMaturityEntity>
    savingsProcessor() {
        return savings -> {
            // 이자 = 원금 × 연이율/100 × (개월/12)
            BigDecimal principal =
                    BigDecimal.valueOf(savings.getPrincipal());
            BigDecimal annualRate =
                    savings.getAnnualRate()
                            .divide(BigDecimal.valueOf(100));
            BigDecimal months =
                    BigDecimal.valueOf(savings.getMonths());

            BigDecimal interest = principal
                    .multiply(annualRate)
                    .multiply(months)
                    .divide(BigDecimal.valueOf(12),
                            0, RoundingMode.DOWN); // 원 미만 절사

            long interestAmt = interest.longValue();
            long totalAmt    =
                    savings.getPrincipal() + interestAmt;

            log.info("[Processor] {} | 원금:{}원 | 이율:{}%" +
                            " | 이자:{}원 | 원리금:{}원",
                    savings.getSavingsNo(), savings.getPrincipal(),
                    savings.getAnnualRate(), interestAmt, totalAmt);

            return SavingsMaturityEntity.builder()
                    .savingsNo(savings.getSavingsNo())
                    .ownerName(savings.getOwnerName())
                    .linkedAccount(savings.getLinkedAccount())
                    .principal(savings.getPrincipal())
                    .interest(interestAmt)
                    .totalAmount(totalAmt)
                    .annualRate(savings.getAnnualRate())
                    .maturityDate(savings.getMaturityDate())
                    .processedAt(LocalDateTime.now())
                    .build();
        };
    }

    // ── Writer — 만기 해지 내역 저장 ─────────────
    @Bean
    public RepositoryItemWriter<SavingsMaturityEntity>
    maturityWriter() {
        return new RepositoryItemWriterBuilder<SavingsMaturityEntity>()
                .repository(maturityRepository)
                .methodName("save")
                .build();
    }
}
