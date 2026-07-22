package com.shinhan.bananaapp.exception;

import com.shinhan.bananaapp.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // REST API 예외 → JSON 응답
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException e) {
        log.warn("[BusinessException] {} - {}", e.getErrorCode(), e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
    }
    // 유효성 검사 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(ApiResponse.fail(message));
    }

    // View 기반 404 처리
    @ExceptionHandler(AccountNotFoundException.class)
    public String handleNotFound(AccountNotFoundException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/404";
    }

//    // 최상위 예외 처리
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public ResponseEntity<ApiResponse<Void>> handleAll(Exception e) {
//        log.error("[Unhandled Exception]", e);
//        return ResponseEntity.internalServerError()
//                .body(ApiResponse.fail("서버 오류가 발생했습니다."));
//    }

}
