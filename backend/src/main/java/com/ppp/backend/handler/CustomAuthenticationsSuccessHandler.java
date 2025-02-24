package com.ppp.backend.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ppp.backend.util.JwtUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationsSuccessHandler implements AuthenticationSuccessHandler {
	private final JwtUtil jwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		if (authentication instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
			Object details = oauthToken.getDetails();

			if (details != null) {
				String token = details.toString();

				if (jwtUtil.isTemporaryToken(token)) {
					// 임시 토큰 인 경우(신규 회원인 경우) 임시 토큰을 쿠키에 저장
					Cookie tempTokenCookie = new Cookie("TEMP_TOKEN", token);
					tempTokenCookie.setHttpOnly(true);
					tempTokenCookie.setSecure(false); // 개발 환경에서는 false, 운영 환경에서는 true 설정 필요
					tempTokenCookie.setPath("/");
					tempTokenCookie.setMaxAge(60 * 60 * 24); // 1일 유지
					response.addCookie(tempTokenCookie);

					// 회원가입 페이지로 리다이렉트
					response.sendRedirect("/signup");
				} else {
					// 정식 토큰인 경우 (기존 회원)
					// 토큰을 쿠키에 저장
					Cookie tokenCookie = new Cookie("accessToken", token);
					tokenCookie.setPath("/");
					tokenCookie.setHttpOnly(true);
					tokenCookie.setMaxAge(60 * 10); // 10분 유효
					response.addCookie(tokenCookie);

					// 홈 페이지로 리다이렉트
					response.sendRedirect("/home");
				}
			} else {
				// details가 없는 경우
				log.info("details가 없음");
				response.sendRedirect("/login?error");
			}
		} else {
			// OAuth2 인증이 아닌 경우
			log.info("OAuth2 인증이 아님");
			response.sendRedirect("/login?error");
		}
	}
}