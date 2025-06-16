package com.welab.k8s_api_gateway.security.jwt;

import com.welab.k8s_api_gateway.security.jwt.authentication.JwtAuthentication;
import com.welab.k8s_api_gateway.security.jwt.authentication.UserPrincipal;
import com.welab.k8s_api_gateway.security.jwt.props.JwtConfigProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Jwt 토큰 검증 + 유저 정보 꺼냄
@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final JwtConfigProperties configProperties;
    private volatile SecretKey secretKey;

    //Secret key 한번만 생성
    private SecretKey getSecretKey() {
        if (secretKey == null) {
            synchronized (this) {
                if (secretKey == null) {
                    secretKey = Keys.hmacShaKeyFor(
                            Decoders.BASE64.decode(configProperties.getSecretKey())
                    );
                }
            }
        }
        return secretKey;
    }

    //JWT 토큰 유효성 검증, 인증 객체 생성
    public JwtAuthentication validateToken(String token) {
        String userId = null;

        final Claims claims = this.verifyAndGetClaims(token);
        if (claims == null) {
            return null;
        }

        Date expirationDate = claims.getExpiration();
        if (expirationDate == null || expirationDate.before(new Date())) {
            return null;
        }

        userId = claims.get("userId", String.class);
        String tokenType = claims.get("tokenType", String.class);
        if (!"access".equals(tokenType)) {
            return null;
        }

        UserPrincipal principal = new UserPrincipal(userId);
        return new JwtAuthentication(principal, token, getGrantedAuthorities("user"));
    }

    //토큰 서명 검증, claims 추출
    private Claims verifyAndGetClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    // 권한 목록 생성
    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (role != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        return grantedAuthorities;
    }

    // 헤더에서 JWT Bearer 토큰 추출
    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // Authorization 헤더 값 가져오기
    private String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(configProperties.getHeader());
    }


}
