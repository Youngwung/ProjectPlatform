package com.ppp.backend.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("userRequest = {}", userRequest);
		OAuth2User oAuth2User = null;
		try {
			OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
			oAuth2User = delegate.loadUser(userRequest); // 사용자 요청을 OAuth server으로 보내고 redirection한 값을 받아온다.
			// 로그인 진행중인 서비스 구분( google인지 Kakao인지 Naver인지 구분하는 코드 )
		} catch (Exception e) {
			throw new OAuth2AuthenticationException("OAuth2 인증 실패");
		}

		// 소셜 플랫폼 구분 코드
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();

		Map<String, Object> attributes = oAuth2User.getAttributes();

		log.info("attributes = {} ", attributes);

		// email 가져오기
		String email = extractEmail(registrationId, attributes);
		// name 가져오기
		String name = extractName(registrationId, attributes);

		// 사용자 정보 가져오기
		Optional<User> userOptional = userRepository.findByEmail(email);
		// 사용자 정보와 토큰을 저장할 자료구조
		Map<String, Object> customAttributes = new HashMap<>(attributes);
		if (userOptional.isPresent()) {
			// 기존 회원 처리
			log.info("기존 회원: {}, {}, {}", registrationId, email, name);
			User user = userOptional.get();
			String token = jwtUtil.generateToken(user.getId(), email);
			customAttributes.put("isNewUser", false);
			customAttributes.put("accessToken", token);

		} else {
			// **임시 토큰 발급**
			log.info("신규 회원: {}, {}, {}", registrationId, email, name);
			String tempToken = jwtUtil.generateTempToken(email, name, registrationId);
			log.info("tempToken = {}", tempToken);
			customAttributes.put("isNewUser", true);
			customAttributes.put("TEMP_TOKEN", tempToken);
			customAttributes.put("registrationId", registrationId);
		}

		// 사용자 정의 속성을 포함한 OAuth2User 반환
		return new DefaultOAuth2User(oAuth2User.getAuthorities(), customAttributes, userNameAttributeName);
	}

	// 제공자별 이메일 추출 로직
	private String extractEmail(String registrationId, Map<String, Object> attributes) {
		switch (registrationId) {
			case "google":
				return (String) attributes.get("email");
			case "kakao":
				Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
				return (String) kakaoAccount.get("email");
			case "naver":
				Map<String, Object> response = (Map<String, Object>) attributes.get("response");
				return (String) response.get("email");
			default:
				throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
		}
	}

	// 제공자별 이메일 추출 로직
	private String extractName(String registrationId, Map<String, Object> attributes) {
		switch (registrationId) {
			case "google":
				return (String) "";
			case "kakao":
				Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
				Map<String, Object> kakaoProperties = (Map<String, Object>) kakaoAccount.get("profile");
				String kakaoNickname = (String) kakaoProperties.get("nickname");
				return kakaoNickname;
			case "naver":
				Map<String, Object> response = (Map<String, Object>) attributes.get("response");
				return (String) response.get("name");
			default:
				throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
		}
	}

}
