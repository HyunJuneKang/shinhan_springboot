package com.shinhan.bananaapp.exception;

import com.shinhan.bananaapp.mybatisprac.prev.AccountDTO;
import com.shinhan.bananaapp.mybatisprac.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountExceptionTestService {
    private final AccountMapper mapper;
    public String f1(Long accId){
        AccountDTO acc = mapper.findById(accId);
        if (acc == null)
            throw new AccountNotFoundException(accId);
        return "Success";
    }
}
