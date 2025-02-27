package com.example.auth;

import com.example.auth.jwt.JwtRequestDto;
import com.example.auth.jwt.JwtTokenDto;
import com.example.auth.jwt.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("token")
public class TokenController {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    public TokenController(JwtTokenUtils jwtTokenUtils, UserDetailsManager manager, PasswordEncoder passwordEncoder) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.manager = manager;
        this.passwordEncoder = passwordEncoder;
    }

    // JWT 발급을 받는 Mapping
    // RequestBody : 인증 받고자 하는 사용자가 ID 비밀번호를 전달 (JwtRequestDto)
    // ResponseBody : 발급이 완료된 JWT 를 전달 (JwtTokenDto)
    @PostMapping("/issue")
    public JwtTokenDto issueJwt(@RequestBody JwtRequestDto dto) {
        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());

        // 암호화되지 않은 비밀번호와 암호화된 비밀번호를 대조하여 일치하는지
        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        JwtTokenDto response = new JwtTokenDto();
        response.setToken(jwtTokenUtils.generateToken(userDetails));
        return response;
    }

    // POST /token/secured
    // 인증이 필요한 URL
    @PostMapping("/secured")
    public String checkSecure() {
        log.info(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        return "success";
    }
}
