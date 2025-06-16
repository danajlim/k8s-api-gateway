package com.welab.k8s_api_gateway.security.jwt.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class UserPrincipal implements Principal {

    private final String userId;

    //userId가 null이 아닌지 확인하는 유틸성 메서드
    public boolean hasName(){
        return userId != null;
    }

    public boolean hasMandatory() {
        return userId != null;
    }

    //객체를 문자열로 출력할 때 userId 출력
    @Override
    public String toString() {
        return getName();
    }

    //사용자 ID (이름)
    @Override
    public String getName() {
        return userId;
    }

    //사용자 객체끼리 비교할 수 있도록 equals,hashCode
    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        return result;
    }

    @Override
    public boolean equals(Object another) {
        if (this==another) return true;
        if (another==null) return false;
        if(!getClass().isAssignableFrom(another.getClass())) return false;

        UserPrincipal principal = (UserPrincipal) another;
        if (!Objects.equals(userId, principal.userId)) {
            return false;
        }

        return true;
    }
}
