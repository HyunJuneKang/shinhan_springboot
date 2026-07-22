package com.shinhan.bananaapp.batch.batchprac2.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SettlementStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("[Step 시작] {}", stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        long read   = stepExecution.getReadCount();
        long write  = stepExecution.getWriteCount();
        long filter = read - write;  // null 반환 필터 건수

        log.info("[Step 완료] 읽기:{}건 저장:{}건 필터:{}건 스킵:{}건",
                read, write, filter, stepExecution.getSkipCount());

        if (write == 0) {
            log.warn("[Step 경고] 정산된 거래내역이 없습니다.");
            return new ExitStatus("NO_DATA");  // 커스텀 ExitStatus
        }
        return stepExecution.getExitStatus();
    }
}
