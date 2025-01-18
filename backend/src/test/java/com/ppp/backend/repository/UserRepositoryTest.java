package com.ppp.backend.repository;

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

	// @Test
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

	// @Test
	public void testUpdate() {
		Provider provider = provRepo.findById(1L).orElseThrow();
		Long userId = 1L;
			User user = userRepo.findById(userId).orElseThrow();
			user = User.builder()
				.id(userId)
				.name("Test")
				.password("1234")
				.provider(provider)
				.email("Test1")
			.build();
		userRepo.save(user);
		log.info("user = {}", user);
	}

}
