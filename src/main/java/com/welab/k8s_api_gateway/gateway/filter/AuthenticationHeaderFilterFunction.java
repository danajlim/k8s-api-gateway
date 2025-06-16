package com.welab.k8s_api_gateway.gateway.filter;

import com.welab.k8s_api_gateway.security.jwt.authentication.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.function.Function;

// 인증된 사용자 정보를 HTTP 요청 헤더에 추가하는 필터 함수
public class AuthenticationHeaderFilterFunction {

    // 이 메서드는 요청 전처리 함수(Function<ServerRequest, ServerRequest>)를 반환함
    public static Function<ServerRequest, ServerRequest> addHeader() {
        return request -> {
            // 기존 요청을 기반으로 새로운 요청 builder 생성
            ServerRequest.Builder requestBuilder = ServerRequest.from(request);

            // 현재 인증된 사용자 정보 가져오기 (Spring Security Context에서)
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // principal이 UserPrincipal 타입인 경우에만 userId를 헤더에 추가
            if (principal instanceof UserPrincipal userPrincipal) {
                requestBuilder.header("X-Auth-UserId", userPrincipal.getUserId()); // 사용자 ID 헤더에 추가
                // requestBuilder.header("X-Auth-Authorities", ...); // 필요하면 권한 정보도 추가 가능
            }

            // 클라이언트 IP 주소를 헤더에 추가 (현재는 하드코딩, 실제 환경에선 동적으로 추출 필요)
            String remoteAddr = "70.1.23.15"; // TODO: 실 IP 추출 로직 필요
            requestBuilder.header("X-Client-Address", remoteAddr);

            // 클라이언트 디바이스 정보를 헤더에 추가 (현재는 고정값 "WEB")
            String device = "WEB"; // TODO: 필요시 동적 처리 가능
            requestBuilder.header("X-Client-Device", device);

            // 수정된 요청 객체 반환
            return requestBuilder.build();
        };
    }
}