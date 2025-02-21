package com.ppp.backend.service.alert;

import com.ppp.backend.controller.AuthApiController;
import com.ppp.backend.domain.alert.AlertProject;
import com.ppp.backend.dto.alert.AlertProjectDto;
import com.ppp.backend.repository.ProjectRepository;
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
@Slf4j
public class AlertProjectService {

    private final AlertProjectRepository alertProjectRepository;
    private final AuthApiController authApiController;
    private final ProjectRepository projectRepository;

    /**
     * 🔹 유저의 모든 프로젝트 알림 조회 (DTO 변환)
     */
    public List<AlertProjectDto> getUserProjectAlerts(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [getUserProjectAlerts] 유저 ID: {}", userId);

        List<AlertProjectDto> alertDtos = alertProjectRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        log.info("✅ [getUserProjectAlerts] 조회된 알림 개수: {}", alertDtos);
        return alertDtos;
    }

    /**
     * 🔹 유저의 읽지 않은 프로젝트 알림 조회 (DTO 변환)
     */
    public List<AlertProjectDto> getUnreadProjectAlerts(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [getUnreadProjectAlerts] 유저 ID: {}", userId);

        List<AlertProjectDto> alertDtos = alertProjectRepository.findByUserIdAndIsRead(userId, false).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        log.info("✅ [getUnreadProjectAlerts] 조회된 읽지 않은 알림 개수: {}", alertDtos);
        return alertDtos;
    }

    /**
     * 🔹 새로운 프로젝트 알림 저장 (DTO 변환 후 저장)
     */
    public AlertProjectDto createProjectAlert(AlertProjectDto alertProjectDto) {
        log.info("✅ [createProjectAlert] 요청 데이터: {}", alertProjectDto);

        var project = projectRepository.findById(alertProjectDto.getProjectId())
                .orElseThrow(() -> {
                    log.error("❌ [createProjectAlert] 프로젝트 ID {} 찾을 수 없음", alertProjectDto.getProjectId());
                    return new EntityNotFoundException("해당 프로젝트를 찾을 수 없습니다. ID: " + alertProjectDto.getProjectId());
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
     * 🔹 프로젝트 알림을 읽음 처리 (Optional 활용)
     */
    @Transactional
    public void markProjectAlertAsRead(Long alertId) {
        log.info("✅ [markProjectAlertAsRead] 알림 ID: {}", alertId);

        Optional<AlertProject> alertOptional = alertProjectRepository.findById(alertId);
        alertOptional.ifPresentOrElse(alert -> {
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
     * 🔹 AlertProject 엔티티 → AlertProjectDto 변환 메서드
     */
    private AlertProjectDto convertToDto(AlertProject alertProject) {
        return AlertProjectDto.builder()
                .id(alertProject.getId())
                .projectId(alertProject.getProject().getId())
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
}
