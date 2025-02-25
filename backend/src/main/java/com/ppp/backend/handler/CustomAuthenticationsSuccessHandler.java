package com.ppp.backend.handler;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
		if (authentication.getPrincipal() instanceof OAuth2User) {
			OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
			Map<String, Object> attributes = oAuth2User.getAttributes();

			boolean isNewUser = (boolean) attributes.getOrDefault("isNewUser", false);
			// TODO: 호스트 URL 현재 로컬호스트3000
			String hostURL = "http://localhost:3000";

			if (isNewUser) {
				// 신규 회원 처리
				String tempToken = (String) attributes.get("TEMP_TOKEN");
				log.info("tempToken = {}", tempToken);
				if (jwtUtil.isTemporaryToken(tempToken)) {
					// 임시 토큰 인 경우(신규 회원인 경우) 임시 토큰을 쿠키에 저장
					Cookie tempTokenCookie = new Cookie("TEMP_TOKEN", tempToken);
					tempTokenCookie.setHttpOnly(true);
					tempTokenCookie.setSecure(false); // 개발 환경에서는 false, 운영 환경에서는 true 설정 필요
					tempTokenCookie.setPath("/");
					tempTokenCookie.setMaxAge(60 * 10); // 10분 유지
					String tokenQueryString = "?token=" + tempToken;
					String redirectUrl = hostURL + "/signup" + tokenQueryString;

					response.addCookie(tempTokenCookie);
					// 회원가입 페이지로 리다이렉트
					response.sendRedirect(redirectUrl);
				} else {
					// 임시 토큰의 형식이 안맞음
					log.info("임시 토큰의 형식이 안맞음");
					response.sendRedirect("/login?error");
				}
			} else {
				// 정식 토큰인 경우 (기존 회원)
				String accessToken = (String) attributes.get("accessToken");
				// 토큰을 쿠키에 저장
				Cookie tokenCookie = new Cookie("accessToken", accessToken);
				tokenCookie.setPath("/");
				tokenCookie.setHttpOnly(true);
				tokenCookie.setMaxAge(60 * 10); // 10분 유효
				response.addCookie(tokenCookie);

				// 홈 페이지로 리다이렉트
				response.sendRedirect(hostURL + "/");
			}
		} else {
			// OAuth2 인증이 아닌 경우
			log.info("OAuth2 인증이 아님");
			response.sendRedirect("/login?error");
		}
	}
}