package com.shinhan.bananaapp.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppEventListener {
    // 서버 시작 완료 시
    @EventListener(ApplicationReadyEvent.class)
    public void onStart() {
//        log.info("[AppEventListener] 서버 시작 완료");
//        log.info("[AppEventListener] 초기화 작업 수행");
    }
    // 서버 종료 시
    @EventListener(ContextClosedEvent.class)
    public void onStop() {
//        log.info("[AppEventListener] 서버 종료");
//        log.info("[AppEventListener] 자원 해제");
    }
}