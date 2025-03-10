package com.ppp.backend.repository.alert;

import java.util.List;
import java.util.Optional;

import com.ppp.backend.domain.Project;
import com.ppp.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ppp.backend.domain.alert.AlertProject;

import jakarta.transaction.Transactional;

@Repository
public interface AlertProjectRepository extends JpaRepository<AlertProject, Long> {

    /**
     * 특정 유저(알람 소유자)의 모든 프로젝트 알람을 최신순으로 조회 (DESC)
     */
    @Query("SELECT a FROM AlertProject a WHERE a.alertOwnerId.id = :userId ORDER BY a.createdAt DESC")
    List<AlertProject> findByAlertOwnerId(@Param("userId") Long userId);

    /**
     * 특정 유저(알람 소유자)의 읽지 않은 프로젝트 알람을 최신순으로 조회 (DESC)
     */
    @Query("SELECT a FROM AlertProject a WHERE a.alertOwnerId.id = :userId AND a.isRead = false ORDER BY a.createdAt DESC")
    List<AlertProject> findByAlertOwnerIdAndIsRead(@Param("userId") Long userId);

    /**
     * 특정 유저(알람 소유자)의 모든 알람을 읽음 처리 (업데이트)
     */
    @Modifying
    @Transactional
    @Query("UPDATE AlertProject a SET a.isRead = true WHERE a.alertOwnerId.id = :userId")
    int markAllAsReadByAlertOwnerId(@Param("userId") Long userId);

    /**
     * 신청자용 알림 조회: alertOwnerId가 신청자 ID와 일치하는 알림을 조회합니다.
     */
    @Query("SELECT a FROM AlertProject a WHERE a.project.id = :projectId AND a.alertOwnerId.id = :applicantId AND a.status = :status")
    Optional<AlertProject> findApplicantAlertByProjectIdAndAlertOwnerIdAndStatus(
            @Param("projectId") Long projectId,
            @Param("applicantId") Long applicantId,
            @Param("status") AlertProject.Status status);

    /**
     * 소유자용 알림 조회: alertOwnerId가 프로젝트 소유자 ID와 일치하는 알림을 조회합니다.
     */
    @Query("SELECT a FROM AlertProject a WHERE a.project.id = :projectId AND a.alertOwnerId.id = :ownerId AND a.status = :status")
    Optional<AlertProject> findOwnerAlertByProjectIdAndAlertOwnerIdAndStatus(
            @Param("projectId") Long projectId,
            @Param("ownerId") Long ownerId,
            @Param("status") AlertProject.Status status);

    @Modifying
    @Query("DELETE FROM AlertProject a WHERE a.senderId.id = :userId OR a.receiverId.id = :userId OR a.alertOwnerId.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
    Optional<AlertProject> findByProjectAndReceiverIdAndTypeAndStep(Project project, User receiver, AlertProject.Type type, Integer step);

    void deleteByProjectId(Long projectId);


}
