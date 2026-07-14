package com.shinhan.bananaapp.exception;

public class AccountNotFoundException extends BusinessException{
    public AccountNotFoundException(Long id){
        super("ACCOUNT NOT FOUND","계좌를 찾을 수 없습니다 id= "+ id);
    }
}
