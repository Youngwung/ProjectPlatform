package com.ppp.backend.repository;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ppp.backend.domain.Portfolio;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long>, PortfolioSearch {

    // 특정 사용자가 작성한 프로젝트 리스트를 조회
    List<Portfolio> findByTitleContainingIgnoreCase(String title);

    @Modifying
    @Transactional
    @Query("DELETE FROM Portfolio p WHERE p.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    List<Portfolio> findByUserId(Long userId);
}
