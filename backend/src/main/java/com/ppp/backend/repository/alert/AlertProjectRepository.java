package com.ppp.backend.repository.alert;

import com.ppp.backend.domain.alert.AlertProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertProjectRepository extends JpaRepository<AlertProject, Long> {

    /**
     * 특정 유저의 모든 프로젝트 알림을 최신순으로 조회 (DESC)
     */
    @Query("SELECT a FROM AlertProject a WHERE a.user.id = :userId ORDER BY a.createdAt DESC")
    List<AlertProject> findByUserId(Long userId);

    /**
     * 특정 유저의 읽지 않은 프로젝트 알림을 최신순으로 조회 (DESC)
     */
    @Query("SELECT a FROM AlertProject a WHERE a.user.id = :userId AND a.isRead = false ORDER BY a.createdAt DESC")
    List<AlertProject> findByUserIdAndIsRead(Long userId, boolean isRead);
}
