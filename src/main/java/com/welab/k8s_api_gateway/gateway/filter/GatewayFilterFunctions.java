package com.welab.k8s_api_gateway.gateway.filter;

import org.springframework.cloud.gateway.server.mvc.common.Shortcut;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.HandlerFilterFunction.ofRequestProcessor;

// Gateway 필터 함수 정의용 인터페이스
public interface GatewayFilterFunctions {

    // 인증 정보를 헤더에 추가하는 필터 등록
    @Shortcut // spring-cloud-gateway에서 custom filter를 이름으로 참조할 수 있게 해줌 (AddAuthenticationHeader)
    static HandlerFilterFunction<ServerResponse, ServerResponse> addAuthenticationHeader() {
        // AuthenticationHeaderFilterFunction에서 만든 요청 전처리 함수 등록
        return ofRequestProcessor(AuthenticationHeaderFilterFunction.addHeader());
    }
}