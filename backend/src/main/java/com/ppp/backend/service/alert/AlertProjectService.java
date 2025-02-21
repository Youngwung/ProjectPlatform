package com.ppp.backend.service.alert;

import com.ppp.backend.controller.AuthApiController;
import com.ppp.backend.domain.User;
import com.ppp.backend.domain.alert.AlertProject;
import com.ppp.backend.dto.ProjectDTO;
import com.ppp.backend.dto.alert.AlertProjectDto;
import com.ppp.backend.repository.ProjectRepository;
import com.ppp.backend.repository.ProjectTypeRepository;
import com.ppp.backend.repository.UserRepository;
import com.ppp.backend.repository.alert.AlertProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AlertProjectService {

    private final AlertProjectRepository alertProjectRepository;
    private final AuthApiController authApiController;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectTypeRepository projectTypeRepository;

    /**
     * 🔹 유저의 모든 프로젝트 알림 조회 (DTO 변환)
     */
    public List<AlertProjectDto> getUserProjectAlerts(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [getUserProjectAlerts] 유저 ID: {}", userId);

        return alertProjectRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 🔹 유저의 읽지 않은 프로젝트 알림 조회 (DTO 변환)
     */
    public List<AlertProjectDto> getUnreadProjectAlerts(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [getUnreadProjectAlerts] 유저 ID: {}", userId);

        return alertProjectRepository.findByUserIdAndIsRead(userId, false).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 🔹 새로운 프로젝트 알림 저장 (DTO 변환 후 저장)
     */
    public AlertProjectDto createProjectAlert(AlertProjectDto alertProjectDto) {
        log.info("✅ [createProjectAlert] 요청 데이터: {}", alertProjectDto);

        var project = projectRepository.findById(alertProjectDto.getProject().getId())
                .orElseThrow(() -> {
                    log.error("❌ [createProjectAlert] 프로젝트 ID {} 찾을 수 없음", alertProjectDto.getProject().getId());
                    return new EntityNotFoundException("해당 프로젝트를 찾을 수 없습니다. ID: " + alertProjectDto.getProject().getId());
                });

        AlertProject alertProject = AlertProject.builder()
                .project(project)
                .status(AlertProject.Status.valueOf(alertProjectDto.getStatus()))
                .content(alertProjectDto.getContent())
                .isRead(false)
                .build();

        AlertProject savedAlert = alertProjectRepository.save(alertProject);
        log.info("✅ [createProjectAlert] 알림 저장 완료 - ID: {}", savedAlert.getId());

        return convertToDto(savedAlert);
    }

    /**
     * 🔹 프로젝트 알림을 읽음 처리
     */
    public void markProjectAlertAsRead(Long alertId) {
        log.info("✅ [markProjectAlertAsRead] 알림 ID: {}", alertId);

        alertProjectRepository.findById(alertId).ifPresentOrElse(alert -> {
            alert.markAsRead();
            log.info("✅ [markProjectAlertAsRead] 알림 ID {} 읽음 처리 완료", alertId);
        }, () -> log.warn("❌ [markProjectAlertAsRead] 알림 ID {} 찾을 수 없음", alertId));
    }

    /**
     * 🔹 특정 프로젝트 알림 삭제
     */
    public void deleteProjectAlert(Long alertId) {
        log.info("✅ [deleteProjectAlert] 삭제 요청 - 알림 ID: {}", alertId);

        if (!alertProjectRepository.existsById(alertId)) {
            log.error("❌ [deleteProjectAlert] 알림 ID {} 찾을 수 없음", alertId);
            throw new EntityNotFoundException("해당 알림을 찾을 수 없습니다. ID: " + alertId);
        }

        alertProjectRepository.deleteById(alertId);
        log.info("✅ [deleteProjectAlert] 알림 ID {} 삭제 완료", alertId);
    }

    /**
     * 🔹 특정 프로젝트 알림 조회
     */
    public AlertProjectDto getProjectAlertById(Long alertId, HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [getProjectAlertById] 유저 ID: {} - 알림 ID: {}", userId, alertId);

        AlertProject alertProject = alertProjectRepository.findById(alertId)
                .orElseThrow(() -> {
                    log.warn("❌ [getProjectAlertById] 프로젝트 알림 ID {} 찾을 수 없음", alertId);
                    return new EntityNotFoundException("해당 프로젝트 알림을 찾을 수 없습니다. ID: " + alertId);
                });

        // 보낸 사람: 프로젝트 생성자
        String senderName = alertProject.getProject().getUser().getName();

        // 받는 사람: 현재 로그인한 사용자
        String receiverName = userRepository.findById(userId)
                .map(User::getName)
                .orElse("알 수 없음");

        AlertProjectDto alertProjectDto = convertToDto(alertProject);
        alertProjectDto.setSenderName(senderName);
        alertProjectDto.setReceiverName(receiverName);

        log.info("✅ [getProjectAlertById] 조회 성공 - 알림 ID: {}", alertProjectDto.getId());

        return alertProjectDto;
    }

    /**
     * 🔹 AlertProject 엔티티 → AlertProjectDto 변환
     */
    private AlertProjectDto convertToDto(AlertProject alertProject) {
        // ✅ 프로젝트 타입 조회
        String projectType = projectTypeRepository.findByProjectId(alertProject.getProject().getId())
                .map(projectTypeEntity -> projectTypeEntity.getType().name()) // Enum → String 변환
                .orElse(null); // ✅ 프로젝트 타입이 없을 경우 null 반환

        ProjectDTO projectDTO = ProjectDTO.builder()
                .id(alertProject.getProject().getId())
                .userId(alertProject.getProject().getUser().getId())
                .title(alertProject.getProject().getTitle())
                .description(alertProject.getProject().getDescription())
                .maxPeople(alertProject.getProject().getMaxPeople())
                .status(alertProject.getProject().getStatus().name())
                .isPublic(alertProject.getProject().isPublic())
                .type(projectType) // ✅ 조회한 프로젝트 타입 설정
                .createdAt(alertProject.getProject().getCreatedAt().toLocalDateTime())
                .updatedAt(alertProject.getProject().getUpdatedAt().toLocalDateTime())
                .build();

        return AlertProjectDto.builder()
                .id(alertProject.getId())
                .senderName(alertProject.getProject().getUser().getName())
                .receiverName("") // ✅ 이후 getProjectAlertById에서 설정
                .project(projectDTO) // ✅ ProjectDTO로 변환하여 저장
                .status(alertProject.getStatus().name())
                .content(alertProject.getContent())
                .createdAt(alertProject.getCreatedAt())
                .isRead(alertProject.isRead())
                .build();
    }



    /**
     * 🔹 유저 ID를 쿠키에서 추출 (없으면 예외 발생)
     */
    private Long extractUserIdOrThrow(HttpServletRequest request) {
        Long userId = authApiController.extractUserIdFromCookie(request);
        if (userId == null) {
            log.warn("🚨 [extractUserIdOrThrow] 유저 인증 실패 - 쿠키 없음");
            throw new IllegalStateException("유저 인증에 실패했습니다.");
        }
        log.info("✅ [extractUserIdOrThrow] 유저 인증 성공 - ID: {}", userId);
        return userId;
    }

    /**
     * 🔹 모든 프로젝트 알림 읽음 처리
     */
    public void markAllProjectAlertsAsRead(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [markAllProjectAlertsAsRead] 유저 ID {}의 모든 알림 읽음 처리 요청", userId);

        int updatedCount = alertProjectRepository.markAllAsReadByUserId(userId);
        log.info("✅ [markAllProjectAlertsAsRead] 총 {}개의 프로젝트 알림 읽음 처리 완료", updatedCount);
    }
}
