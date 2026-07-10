package com.shinhan.bananaapp.mapper;

import com.shinhan.bananaapp.dto.AccountDTO;
import com.shinhan.bananaapp.dto.AccountSearchDTO;
import com.shinhan.bananaapp.dto.AccountWithAttachmentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountMapper {
    List<AccountDTO> findAll();
    AccountDTO findById(Long id);
    List<AccountDTO> findByCondition(AccountSearchDTO search);  // 동적 SQL
    int insert(AccountDTO account);
    int update(AccountDTO account);
    int delete(Long id);
    int deposit(AccountDTO account);
    int withdraw(AccountDTO account);
    List<AccountWithAttachmentDTO> findAllWithAttachmentFlat();
    AccountDTO findByIdWithAttachment(Long id);

}
