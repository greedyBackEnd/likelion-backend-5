package com.example.auth.jwt;

import com.example.auth.entity.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
// JWT 생성, 인증 등의 기능을 가지고 있을 컴포넌트
public class JwtTokenUtils {
    // JWT 는 암호화를 거쳐서 만들어짐, 이를 위해 암호키 필요
    private final Key signingKey;
    private final JwtParser jwtParser;

    public JwtTokenUtils(
            @Value("${jwt.secret}")
            String jwtSecret
    ) {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        // JWT 번역기 만들기
        this.jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(this.signingKey)
                .build();

    }

    // 1. JWT가 유효한지 판단하는 메소드
    //    jjwt 라이브러리에서는 JWT를 해석하는 과정에서
    //    유효하지 않으면 예외가 발생
    public boolean validate(String token) {
        //  정당한 JWT면 true,
        try {
            jwtParser.parseClaimsJwt(token);
            return true;
        } catch (Exception e) {
            //  정당하지 않은 JWT면 false
            log.warn("invalid jwt: {}", e.getClass());
            return false;
        }
    }

    // JWT 를 인자로 받고, 그 JWT 를 해석해서 사용자 정보를 회수하는 메소드
    public Claims parseClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }

    // 새로운 JWT를 발급하는 용도
    public String generateToken(UserDetails userDetails) {
        // Claim : JWT 에 담길 데이터의 키 (맵의 키와 비슷한 용도)
        // 이 부분은 JWT 에 담고싶은 사용자 정보를 담음
        Claims jwtClaims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(3600)));
//        추가 정보를 담을 수도 있음
//        jwtClaims.put("eml",((CustomUserDetails) userDetails).getEmail());
        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(signingKey)
                .compact();

    }
}
