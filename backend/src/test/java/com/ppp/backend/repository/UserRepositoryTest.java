package com.ppp.backend.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.ppp.backend.domain.Provider;
import com.ppp.backend.domain.User;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
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
	public void testCreateAndFindUser() {
		Provider prov = provRepo.findById(1L).orElseThrow();
		User user = User.builder()
				.password("password123")        // 사용자 비밀번호
				.name("홍길동")                  // 사용자 이름
				.email("hong@example.com")       // 사용자 이메일
				.phoneNumber("010-1234-5678")     // 전화번호
				.experience("3년")               // 경력 정보
				.provider(prov)
				.build();
		User savedUser = userRepo.save(user);
		User foundUser = userRepo.findById(savedUser.getId()).orElse(null);
		assertThat(foundUser).isNotNull();
		assertThat(foundUser.getName()).isEqualTo("홍길동");
		assertThat(foundUser.getEmail()).isEqualTo("hong@example.com");
		assertThat(foundUser.getPhoneNumber()).isEqualTo("010-1234-5678");
		assertThat(foundUser.getExperience()).isEqualTo("3년");
	}
	@Test
	public void testUpdateUser() {
		Provider prov = provRepo.findById(1L).orElseThrow();
		Long userid = 1L;

		User user = User.builder()
				.id(userid)
				.password("password123")
				.name("홍길동테스트")
				.email("hong@example11.com")
				.phoneNumber("010-1234-5678")
				.experience("4년")
				.provider(prov)
				.build();
		userRepo.save(user);
		log.info("userRepo = {} ---------------------", userRepo);
	}
	//@Test
	// Todo findproject에  연관된 유저아이디가 있을경우 삭제 에러가남 User 엔티티에 FindProject와의 양방향 연관 관계 추가하기 가 필요할수도?
	public void testDeleteUser() {
		Provider prov = provRepo.findById(1L).orElseThrow();
		Long userid = 1L;
		User user = userRepo.findById(userid).orElseThrow();
		userRepo.delete(user);
	}
	// @Test
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
		Provider local = Provider.builder()
					.name("local")
					.description("로컬")
					.build();

		provRepo.save(google);
		provRepo.save(naver);
		provRepo.save(kakao);
		provRepo.save(local);

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

	// @Test
	public void testRead() {
		User user = userRepo.findById(1L).orElseThrow();
		log.info("user = {}", user.getProvider());
	}

	@Test
	public void testUpdate() {
		Provider provider = provRepo.findById(1L).orElseThrow();

		log.info("now() = {}", LocalDateTime.now());
		log.info("now() = {}", Timestamp.valueOf(LocalDateTime.now()));
		User user = User.builder()
				.id(1L)
				.name("TestUpdate")
				.password("1234")
				.provider(provider)
				.email("Test1")
				.createdAt(Timestamp.valueOf(LocalDateTime.now()))
				// 영국 표준시 저장됨.
				.updatedAt(Timestamp.valueOf(LocalDateTime.now()))
				.build();
		userRepo.save(user);
		log.info("user = {}", user);
	}


	public void findAll() {

	}

}
