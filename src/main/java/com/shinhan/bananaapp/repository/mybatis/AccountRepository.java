package com.shinhan.bananaapp.repository.mybatis;

import com.shinhan.bananaapp.dto.prev.AccountDTO;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository("accRepo")
public class AccountRepository {

    List<AccountDTO> accountList = new ArrayList<>();
    AccountRepository(){
        AccountDTO acc1 = new AccountDTO();
        acc1.setOwnerName("Hong");
        acc1.setAccountNo("1234");
        acc1.setId(2L);
        acc1.setAccountType("예금");
        acc1.setBalance(300L);
        acc1.setCreatedAt(LocalDate.now());
        AccountDTO acc2 = new AccountDTO(1L, "123", "김", 100L, "예금", LocalDate.now(),null);
        AccountDTO acc3 = AccountDTO.builder()
                .id(3L)
                .accountNo("345")
                .ownerName("홍길동")
                .balance(1000_000_000L)
                .accountType("예금")
                .createdAt(LocalDate.now())
                .build();
        accountList.add(acc1);
        accountList.add(acc2);
        accountList.add(acc3);
    }

    public List<AccountDTO> getDataList(){
        return accountList;
    }

    public AccountDTO getData(Long accId) {
        AccountDTO acc = null;
        List<AccountDTO> result = accountList.stream().filter(data-> Objects.equals(data.getId(), accId)).toList();
        if(!result.isEmpty()){
            acc = result.get(0);
        }
        return acc;
    }

    public int saveData(AccountDTO acc) {
        if(acc == null || acc.getId() == null || acc.getAccountNo() == null)
            return 0;

        AccountDTO curAcc = getData(acc.getId());

        if(curAcc == null){
            accountList.add(acc);
            return 1;
        }else{
            return 2;
        }
    }

    public int putData(AccountDTO acc) {
        if(acc == null || acc.getId() == null || acc.getAccountNo() == null)
            return 0;

        AccountDTO curAcc = getData(acc.getId());

        if(curAcc == null){
            return -1;
        }else{
            curAcc.setAccountNo(acc.getAccountNo());
            curAcc.setOwnerName(acc.getOwnerName());
            curAcc.setBalance(acc.getBalance());
            curAcc.setAccountType(acc.getAccountType());
            curAcc.setCreatedAt(acc.getCreatedAt());
            return 1;
        }
    }

    public int deleteData(Long accId) {
        if (accId == null) {
            return 0;
        }

        AccountDTO curAcc = getData(accId);

        if(curAcc == null){
            return -1;
        }else{
            accountList.remove(curAcc);
            return 1;
        }
    }
}
