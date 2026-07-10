package com.shinhan.bananaapp.service;

import com.shinhan.bananaapp.dto.AccountDTO;
import com.shinhan.bananaapp.dto.AccountSearchDTO;
import com.shinhan.bananaapp.dto.AccountWithAttachmentDTO;
import com.shinhan.bananaapp.mapper.AccountMapper;
import com.shinhan.bananaapp.repository.AccountRepository2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountServiceUsingMyBatis {
    final private AccountRepository2 accRepo;
    /** 1회 이체 한도 */
    private static final long MAX_TRANSFER_AMOUNT = 10_000_000L;
    final AccountMapper accountMapper;
    /**
     * 전체 계좌 조회
     */
    @Transactional(readOnly = true)
    public List<AccountDTO> selectAllService() {
        return accountMapper.findAll();
    }

    /**
     * 계좌번호로 조회
     */
    @Transactional(readOnly = true)
    public AccountDTO selectByIdService(Long id) {
        return accountMapper.findById(id);
    }

    /**
     * 조건 검색
     */
    @Transactional(readOnly = true)
    public List<AccountDTO> findByCondition(AccountSearchDTO search) {
        return accountMapper.findByCondition(search);
    }

    /**
     * 계좌 등록
     */
    public int insertService(AccountDTO account) {
        log.info("계좌 등록 : {}", account.getAccountNo());
        int result = accountMapper.insert(account);
        if (result != 1) {
            throw new RuntimeException("계좌 등록에 실패했습니다.");
        }
        return result;
    }

    /**
     * 계좌 수정
     */
    public int updateService(AccountDTO account) {
        // 존재 여부 확인...없으면 예외를 발생
        getAccount(account.getId());
        int result = accountMapper.update(account);
        log.info("계좌 수정 : {}", account.getId());
        if (result != 1) {
            throw new RuntimeException("계좌 수정에 실패했습니다.");
        }
        return result;
    }

    /**
     * 계좌 삭제
     */
    public int delete(Long id) {

        // 존재 여부 확인
        getAccount(id);
        log.info("계좌 삭제 : {}", id);
        int result = accountMapper.delete(id);
        if (result != 1) {
            throw new RuntimeException("계좌 삭제에 실패했습니다.");
        }
        return result;
    }
    /**
     * 계좌 이체
     */
    public void transfer(Long fromId, Long toId, Long amount) {

        validateTransferRequest(fromId, toId, amount);

        Map<String, Object> map = new HashMap<>();
        map.put("fromId", fromId);
        map.put("toId", toId);
        map.put("amount", amount);

        int withdrawResult = accRepo.withdraw(map);

        if (withdrawResult != 1) {
            throw new RuntimeException("출금 실패: 계좌가 없거나 잔액이 부족합니다.");
        }

        int depositResult = accRepo.deposit(map);

        if (depositResult != 1) {
            throw new RuntimeException("입금 실패: 받는 계좌가 존재하지 않습니다.");
        }

        log.info("이체 완료 : {}원 ({} → {})", amount, fromId, toId);
    }
    private void validateTransferRequest(Long fromId, Long toId, Long amount) {

        if (fromId == null || toId == null) {
            throw new IllegalArgumentException("계좌 ID는 필수입니다.");
        }

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("같은 계좌로는 이체할 수 없습니다.");
        }

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("이체 금액은 0보다 커야 합니다.");
        }

        if (amount > MAX_TRANSFER_AMOUNT) {
            throw new IllegalArgumentException(
                    "1회 이체 한도(" + String.format("%,d", MAX_TRANSFER_AMOUNT) + "원)를 초과했습니다."
            );
        }
    }
    /**
     * 출금
     */
    private void withdraw(AccountDTO account, Long amount) {
        account.setBalance(account.getBalance() - amount);
        if (accountMapper.update(account) != 1) {
            throw new RuntimeException("출금 처리에 실패했습니다.");
        }
    }
    /**
     * 입금
     */
    private void deposit(AccountDTO account, Long amount) {
        account.setBalance(account.getBalance() + amount);
        if (accountMapper.update(account) != 1) {
            throw new RuntimeException("입금 처리에 실패했습니다.");
        }
    }
    /**
     * 계좌 존재 여부 확인
     */
    private AccountDTO getAccount(Long id) {
        AccountDTO account = accountMapper.findById(id);
        if (account == null) {
            throw new IllegalArgumentException("계좌를 찾을 수 없습니다. id=" + id);
        }
        return account;
    }
    // ── 방식 1: Flat ResultMap 조회 ──────────────────
    @Transactional(readOnly = true)
    public List<AccountWithAttachmentDTO> findAllWithAttachmentFlat() {
        return accountMapper.findAllWithAttachmentFlat();
    }
    // ── 방식 2: collection ResultMap 조회 ────────────
    // detail 화면 — 계좌 1건 + 첨부파일 리스트
    @Transactional(readOnly = true)
    public AccountDTO findByIdWithAttachment(Long id) {
        AccountDTO account = accountMapper.findByIdWithAttachment(id);
        if (account == null)
            throw new IllegalArgumentException("계좌를 찾을 수 없습니다. id=" + id);
        return account;
    }
}
