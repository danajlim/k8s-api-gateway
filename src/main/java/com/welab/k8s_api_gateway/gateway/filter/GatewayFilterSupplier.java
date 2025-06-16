package com.welab.k8s_api_gateway.gateway.filter;

import org.springframework.cloud.gateway.server.mvc.filter.SimpleFilterSupplier;
import org.springframework.context.annotation.Configuration;

// Gateway에서 사용할 사용자 정의 필터들을 등록해주는 설정 클래스
@Configuration // Spring Bean으로 등록
public class GatewayFilterSupplier extends SimpleFilterSupplier {

    // 생성자에서 필터 함수 클래스 등록
    public GatewayFilterSupplier() {
        // GatewayFilterFunctions 클래스에 정의된 필터들을 등록
        super(GatewayFilterFunctions.class);
    }
}