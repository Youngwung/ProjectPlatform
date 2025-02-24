package com.ppp.backend.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ppp.backend.domain.User;
import com.ppp.backend.repository.UserRepository;
import com.ppp.backend.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		if (oAuth2User == null) {
			throw new OAuth2AuthenticationException("OAuth2 사용자 정보를 가져올 수 없습니다.");
	}

		// 소셜 플랫폼 구분 코드
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();

		Map<String, Object> attributes = oAuth2User.getAttributes();

		// email 가져오기
		String email = (String) attributes.get("email");
		String name = (String) attributes.get("name");
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.isPresent()) {
			// 기존 회원 처리
			User user = userOptional.get();
			String token = jwtUtil.generateToken(user.getId(), user.getEmail());
			// 토큰을 쿠키에 저장하기 위해 SecurityContext에 저장
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof OAuth2AuthenticationToken) {
				OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
				oauthToken.setDetails(token); // 토큰을 details에 저장
			}
			return new DefaultOAuth2User(Collections.emptyList(), attributes, userNameAttributeName);
		} else {
			// **임시 토큰 발급**
			String tempToken = jwtUtil.generateTempToken(email, name, registrationId);

			// 임시 토큰을 SecurityContext에 저장
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof OAuth2AuthenticationToken) {
				OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
				oauthToken.setDetails(tempToken); // 임시 토큰을 details에 저장
			}
			return null;
		}
	}

}
