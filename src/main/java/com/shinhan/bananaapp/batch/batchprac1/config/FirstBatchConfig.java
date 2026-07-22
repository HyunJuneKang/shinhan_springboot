package com.shinhan.bananaapp.batch.batchprac1.config;

import com.shinhan.bananaapp.batch.batchprac1.entity.AfterEntity;
import com.shinhan.bananaapp.batch.batchprac1.entity.BeforeEntity;
import com.shinhan.bananaapp.batch.batchprac1.repository.AfterRepository;
import com.shinhan.bananaapp.batch.batchprac1.repository.BeforeRepository;
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

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FirstBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final BeforeRepository beforeRepository;
    private final AfterRepository afterRepository;

    // ── Job ──────────────────────────────────────
    @Bean
    public Job firstJob() {
        return new JobBuilder("firstJob", jobRepository)
                .start(firstStep())
                .build();
    }

    // ── Step (chunk=10) ──────────────────────────
    @Bean
    public Step firstStep() {
        return new StepBuilder("firstStep", jobRepository)
                .<BeforeEntity, AfterEntity>chunk(10, platformTransactionManager)
                .reader(beforeReader())
                .processor(middleProcessor())
                .writer(afterWriter())
                .build();
    }
    // ── ItemReader ───────────────────────────────
    @Bean
    public RepositoryItemReader<BeforeEntity> beforeReader() {
        return new RepositoryItemReaderBuilder<BeforeEntity>()
                .name("beforeReader")
                .pageSize(10)
                .methodName("findAll")
                .repository(beforeRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
// 정렬 필수!(Paging때문,중복되지않고 항상동일순서)
                .build();
    }

    // ── ItemProcessor ────────────────────────────
    // null 반환 시 Writer로 전달되지 않음 (필터링)
    @Bean
    public ItemProcessor<BeforeEntity, AfterEntity> middleProcessor() {
        return item -> {
            // "test"로 시작하면 skip
            if (item.getName().toLowerCase().startsWith("test"))
                return null;

            log.info("[Processor] {} → {}",
                    item.getName(), item.getName().toUpperCase());

            return AfterEntity.builder()
                    .name(item.getName().toUpperCase())
                    .build();
        };
    }

    // ── ItemWriter ───────────────────────────────
    @Bean
    public RepositoryItemWriter<AfterEntity> afterWriter() {
        return new RepositoryItemWriterBuilder<AfterEntity>()
                .repository(afterRepository)
                .methodName("save")
                .build();
    }
}
