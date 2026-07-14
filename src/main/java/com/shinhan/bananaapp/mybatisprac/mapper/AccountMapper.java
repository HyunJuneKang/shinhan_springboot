package com.shinhan.bananaapp.mybatisprac.mapper;

import com.shinhan.bananaapp.mybatisprac.prev.AccountDTO;
import com.shinhan.bananaapp.mybatisprac.prev.AccountSearchDTO;
import com.shinhan.bananaapp.mybatisprac.prev.AccountWithAttachmentDTO;
import com.shinhan.bananaapp.mybatisprac.prev.AttachmentDTO;
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

    void insertAttachment(AttachmentDTO attachment);
    void deleteAttachment(Long id);
    AttachmentDTO findAttachmentById(Long id);
    AttachmentDTO selectAttachment(Long id);
}
