package com.ppp.backend.repository;

import com.ppp.backend.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    // 특정 사용자가 작성한 프로젝트 리스트를 조회
    List<Portfolio> findByTitleContainingIgnoreCase(String title);
}
