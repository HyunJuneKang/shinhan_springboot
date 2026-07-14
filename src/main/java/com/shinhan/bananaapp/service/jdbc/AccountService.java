package com.shinhan.bananaapp.service.jdbc;

import com.shinhan.bananaapp.dto.prev.AccountDTO;
import com.shinhan.bananaapp.repository.mybatis.AccountRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("shinhanAccount")
public class AccountService {

    private final AccountRepository accountRepository;
    AccountService(@Qualifier("accRepo") AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    public List<AccountDTO> selectAllService() {
        return accountRepository.getDataList();
    }

    public AccountDTO selectById(Long accId) {
        return accountRepository.getData(accId);
    }

    public int insertService(AccountDTO acc) {
        return accountRepository.saveData(acc);
    }

    public int updateService(AccountDTO acc) {
        return accountRepository.putData(acc);
    }

    public int deleteService(Long accId) {
        return accountRepository.deleteData(accId);
    }
}
