package com.shinhan.bananaapp;

import com.shinhan.bananaapp.dto.AccountDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AccountTest {
    @Test
    void accountDtoBuilderTest() {
        AccountDTO acc1 = new AccountDTO();
        acc1.setOwnerName("Hong");
        AccountDTO acc2 = new AccountDTO(1L,"123","김",100L,"예금", LocalDate.now());
        AccountDTO acc3 = AccountDTO.builder()
                .accountNo("345")
                .ownerName("홍길동")
                .balance(1000_000_000L)
                .accountType("예금")
                .build();
        assertThat(acc3.getOwnerName()).isEqualTo("홍길동");
        assertThat(acc3.getBalance()).isGreaterThan(0L);
        System.out.println(acc3);
    }
}
