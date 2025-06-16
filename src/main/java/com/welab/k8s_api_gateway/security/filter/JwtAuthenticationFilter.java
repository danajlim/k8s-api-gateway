package com.welab.k8s_api_gateway.security.filter;

import com.welab.k8s_api_gateway.security.jwt.JwtTokenValidator;
import com.welab.k8s_api_gateway.security.jwt.authentication.JwtAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenValidator jwtTokenValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 요청에서 Authorization 헤더에 담긴 JWT 토큰 추출
        String jwtToken = jwtTokenValidator.getToken(request);

        if (jwtToken != null) {
            // 토큰 유효성 검사 및 인증 정보 생성
            JwtAuthentication authentication = jwtTokenValidator.validateToken(jwtToken);

            if (authentication != null) {
                // 인증 정보 SecurityContext에 저장 → 이후 컨트롤러에서 @AuthenticationPrincipal 등으로 사용 가능
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
