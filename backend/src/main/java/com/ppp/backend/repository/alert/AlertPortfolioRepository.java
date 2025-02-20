package com.ppp.backend.repository.alert;

import com.ppp.backend.domain.alert.AlertPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertPortfolioRepository extends JpaRepository<AlertPortfolio, Long> {

    /**
     * 특정 유저의 모든 포트폴리오 알림을 최신순으로 조회 (DESC)
     */
    @Query("SELECT a FROM AlertPortfolio a WHERE a.user.id = :userId ORDER BY a.createdAt DESC")
    List<AlertPortfolio> findByUserId(Long userId);

    /**
     * 특정 유저의 읽지 않은 포트폴리오 알림을 최신순으로 조회 (DESC)
     */
    @Query("SELECT a FROM AlertPortfolio a WHERE a.user.id = :userId AND a.isRead = false ORDER BY a.createdAt DESC")
    List<AlertPortfolio> findByUserIdAndIsRead(Long userId, boolean isRead);
}
