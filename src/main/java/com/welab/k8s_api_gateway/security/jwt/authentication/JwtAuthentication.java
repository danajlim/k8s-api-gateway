package com.welab.k8s_api_gateway.security.jwt.authentication;

import com.welab.k8s_api_gateway.security.jwt.authentication.UserPrincipal;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class JwtAuthentication extends AbstractAuthenticationToken {

    private final String token; //실제 JWT 문자열
    private final UserPrincipal principal; //인증된 사용자 정보

    //검증 후 생성되는 객체
    public JwtAuthentication(UserPrincipal principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities); // 부모 클래스에 권한 리스트 전달
        this.principal = principal;
        this.token = token;
        this.setDetails(principal); // 인증 상세 정보로 사용자 정보 설정
        setAuthenticated(true); // 이미 인증된 상태로 설정
    }

    //항상 인증된 상태를 나타냄
    @Override
    public boolean isAuthenticated() {
        return true;
    }

    //사용자 인증 자격 (자격 증명 정보)
    @Override
    public Object getCredentials() {
        return token;
    }

    // 실제 인증된 사용자 객체를 반환
    @Override
    public UserPrincipal getPrincipal(){
        return principal;
    }




}
