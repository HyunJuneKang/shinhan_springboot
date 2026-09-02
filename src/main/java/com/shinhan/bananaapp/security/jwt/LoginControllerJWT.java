package com.shinhan.bananaapp.security.jwt;

import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginControllerJWT {

    private final AuthServiceLogin authServiceLogin;
    private final RefreshTokenService refreshTokenService;
    private final ModelMapper modelMapper;

    // 로그인 → Access Token + Refresh Token 발급
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody @Valid LoginRequest request) {
        MemberEntity member = modelMapper.map(request, MemberEntity.class);
        return ResponseEntity.ok(authServiceLogin.login(member));
    }

    // Refresh Token으로 Access Token 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestBody @Valid RefreshRequest request) {
        TokenResponse newToken =
                refreshTokenService.reissueAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(
                Map.of("accessToken", newToken.getAccessToken())
        );
    }

    // 현재 로그인 사용자 정보 확인
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        return ResponseEntity.ok(Map.of(
                "mid",  authentication.getName(),
                "role", authentication.getAuthorities()
        ));
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout(Principal principal) {
        authServiceLogin.logout(principal.getName());
        return "OK";
    }
}
