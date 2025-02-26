//package com.ppp.backend.repository;
//
//import java.util.Optional;
//
//import com.ppp.backend.domain.Portfolio;
//import org.junit.jupiter.api.Assertions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ActiveProfiles;
//
//import com.ppp.backend.domain.User;
//
//import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
//
//@ActiveProfiles("local")
//// @SpringBootTest
//@Slf4j
//public class PortfolioRepositoryTest {
//
//    @Autowired
//    private PortfolioRepository portfolioRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // @Test
//    public void portfolioRepositoryNotNull() {
//        Assertions.assertNotNull(portfolioRepository);
//        log.info("portfolioRepository initialized: {}", portfolioRepository);
//    }
//
//    // @Test
//    @Transactional
//    public void insertportfolio() {
//        // ID가 1인 User 조회
//        User user = userRepository.findById(1L)
//                .orElseThrow(() -> new RuntimeException("User not found with ID 1"));
//
//        // portfolio 생성
//        Portfolio portfolio = Portfolio.builder()
//                .user(user)
//                .title("Find portfolio Title")
//                .description("This is a test portfolio.")
//                .build();
//
//        portfolioRepository.save(portfolio);
//        log.info("Inserted portfolio: {}", portfolio);
//    }
//
//    // @Test
//    @Transactional
//    public void updateportfolio() {
//        // 기존 portfolio 조회
//        Portfolio portfolio = portfolioRepository.findById(1L)
//                .orElseThrow(() -> new RuntimeException("portfolio not found with ID 1"));
//
//        // portfolio 정보 수정
//        portfolio = portfolio.toBuilder()
//                .title("Updated portfolio Title")
//                .description("Updated description for the portfolio.")
//                .build();
//
//        portfolioRepository.save(portfolio);
//        log.info("Updated portfolio: {}", portfolio);
//    }
//
//    // @Test
//    @Transactional
//    public void deleteportfolio() {
//        // 기존 portfolio 삭제
//        Long portfolioId = 1L;
//        portfolioRepository.deleteById(portfolioId);
//        log.info("Deleted portfolio with ID: {}", portfolioId);
//
//        // 삭제 확인
//        Optional<Portfolio> deletedportfolio = portfolioRepository.findById(portfolioId);
//        Assertions.assertTrue(deletedportfolio.isEmpty());
//    }
//}
