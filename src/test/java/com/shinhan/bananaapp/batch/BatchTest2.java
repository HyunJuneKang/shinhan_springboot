package com.shinhan.bananaapp.batch;

import com.shinhan.bananaapp.batch.batchprac1.repository.BeforeRepository;
import com.shinhan.bananaapp.batch.batchprac2.entity.TransactionEntity;
import com.shinhan.bananaapp.batch.batchprac2.repository.SettlementRepository;
import com.shinhan.bananaapp.batch.batchprac2.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class BatchTest2 {
    @Autowired
    private BeforeRepository beforeRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private SettlementRepository settlementRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    void insertTransactionData() {


        // 출력 데이터 먼저 제거
        settlementRepository.deleteAll();

        // 입력 데이터 제거
        transactionRepository.deleteAll();

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        List<TransactionEntity> transactions = List.of(

                // DEPOSIT 15건
                transaction("110-001", "DEPOSIT", 1_000_000L, yesterday),
                transaction("110-001", "DEPOSIT", 2_000_000L, yesterday),
                transaction("110-002", "DEPOSIT", 500_000L, yesterday),
                transaction("110-002", "DEPOSIT", 3_000_000L, yesterday),
                transaction("110-003", "DEPOSIT", 750_000L, yesterday),
                transaction("110-003", "DEPOSIT", 1_500_000L, yesterday),
                transaction("110-004", "DEPOSIT", 4_000_000L, yesterday),
                transaction("110-004", "DEPOSIT", 800_000L, yesterday),
                transaction("110-005", "DEPOSIT", 250_000L, yesterday),
                transaction("110-005", "DEPOSIT", 600_000L, yesterday),
                transaction("110-006", "DEPOSIT", 1_200_000L, yesterday),
                transaction("110-006", "DEPOSIT", 900_000L, yesterday),
                transaction("110-007", "DEPOSIT", 350_000L, yesterday),
                transaction("110-007", "DEPOSIT", 2_500_000L, yesterday),
                transaction("110-008", "DEPOSIT", 0L, yesterday),

                // WITHDRAW 15건
                transaction("110-001", "WITHDRAW", 500_000L, yesterday),
                transaction("110-001", "WITHDRAW", 1_000_000L, yesterday),
                transaction("110-002", "WITHDRAW", 300_000L, yesterday),
                transaction("110-002", "WITHDRAW", 200_000L, yesterday),
                transaction("110-003", "WITHDRAW", 750_000L, yesterday),
                transaction("110-003", "WITHDRAW", 1_500_000L, yesterday),
                transaction("110-004", "WITHDRAW", 2_000_000L, yesterday),
                transaction("110-004", "WITHDRAW", 400_000L, yesterday),
                transaction("110-005", "WITHDRAW", 150_000L, yesterday),
                transaction("110-005", "WITHDRAW", 800_000L, yesterday),
                transaction("110-006", "WITHDRAW", 600_000L, yesterday),
                transaction("110-006", "WITHDRAW", 1_100_000L, yesterday),
                transaction("110-007", "WITHDRAW", 250_000L, yesterday),
                transaction("110-007", "WITHDRAW", 900_000L, yesterday),
                transaction("110-008", "WITHDRAW", 0L, yesterday)
        );

        transactionRepository.saveAll(transactions);
    }

    private TransactionEntity transaction(
            String accountNo,
            String txType,
            Long amount,
            LocalDateTime txDate
    ) {
        return TransactionEntity.builder()
                .accountNo(accountNo)
                .txType(txType)
                .amount(amount)
                .txDate(txDate)
                .settled(false)
                .build();
    }
    void initializeBatchMetaTables() {

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        jdbcTemplate.execute("TRUNCATE TABLE batch_step_execution_context");
        jdbcTemplate.execute("TRUNCATE TABLE batch_step_execution");
        jdbcTemplate.execute("TRUNCATE TABLE batch_job_execution_context");
        jdbcTemplate.execute("TRUNCATE TABLE batch_job_execution_params");
        jdbcTemplate.execute("TRUNCATE TABLE batch_job_execution");
        jdbcTemplate.execute("TRUNCATE TABLE batch_job_instance");

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }
}
