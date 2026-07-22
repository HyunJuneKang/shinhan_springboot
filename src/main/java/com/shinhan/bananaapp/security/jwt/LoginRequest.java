package com.shinhan.bananaapp.security.jwt;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    private String mid;
    @NotBlank
    private String mpassword;
}
//@NotBlank : 문자열(String)이 null이 아니고, 빈 문자열이 아니며,
//공백만으로 이루어져 있지 않은지 검사
