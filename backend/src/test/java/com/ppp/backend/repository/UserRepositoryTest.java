package com.ppp.backend.repository;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ppp.backend.domain.Provider;
import com.ppp.backend.domain.User;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("local")
@Slf4j
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ProviderRepository provRepo;

	@Test
	public void diTest() {
		Assertions.assertNotNull(userRepo);
		log.info("userRepo = {} ------------------", userRepo);
	}

	@Test
	public void providerInsertTest() {
		Provider google = Provider.builder()
			.name("google")
			.description("구글 프로바이더")
		.build();

		Provider naver = Provider.builder()
			.name("naver")
			.description("네이버 프로바이더")
		.build();

		Provider kakao = Provider.builder()
			.name("kakao")
			.description("카카오 프로바이더")
		.build();

		provRepo.save(google);
		provRepo.save(naver);
		provRepo.save(kakao);

	}

	@Test
	public void insertTest() {
		Provider provider = provRepo.findById(1L).orElseThrow();
		User user = User.builder()
				.name("Test")
				.password("1234")
				.email("123@123")
				.provider(provider)
				.build();

		userRepo.save(user);
	}

	@Test
	public void testRead() {
		User user = userRepo.findById(1L).orElseThrow();
		log.info("user = {}", user.getProvider());
	}

	@Test
	public void testUpdate() {
		Provider provider = provRepo.findById(1L).orElseThrow();

		log.info("now() = {}", LocalDateTime.now());
		User user = User.builder()
				.id(1L)
				.name("TestUpdate")
				.password("1234")
				.provider(provider)
				.email("Test1")
				// 영국 표준시 저장됨.
				.updatedAt(LocalDateTime.now())
				.build();
		userRepo.save(user);
		log.info("user = {}", user);
	}

}
