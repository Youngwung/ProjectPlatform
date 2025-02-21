package com.ppp.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppp.backend.domain.Portfolio;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long>, PortfolioSearch {

    // 특정 사용자가 작성한 프로젝트 리스트를 조회
    List<Portfolio> findByTitleContainingIgnoreCase(String title);
}
