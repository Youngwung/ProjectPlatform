package com.ppp.backend.service.alert;

import com.ppp.backend.controller.AuthApiController;
import com.ppp.backend.domain.Project;
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

        //
        return alertProjectRepository.findByUserId(userId).stream()
                .map(alert -> convertToDto(alert, userId))
                .collect(Collectors.toList());
    }

    /**
     * 🔹 유저의 읽지 않은 프로젝트 알림 조회 (DTO 변환)
     */
    public List<AlertProjectDto> getUnreadProjectAlerts(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [getUnreadProjectAlerts] 유저 ID: {}", userId);

        return alertProjectRepository.findByUserIdAndIsRead(userId, false).stream()
                .map(alert -> convertToDto(alert, userId))
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

        return convertToDto(savedAlert, null);
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


        //TODO 보낸사람과 받는사람이 프로젝트 생성자와 로그인한 사용자로 고정되어있음 그래서 isMyProject로 확인해야함
        // 보낸 사람: 프로젝트 생성자
        String senderName = alertProject.getProject().getUser().getName();

        // 받는 사람: 현재 로그인한 사용자
        String receiverName = userRepository.findById(userId)
                .map(User::getName)
                .orElse("알 수 없음");

        AlertProjectDto alertProjectDto = convertToDto(alertProject,userId);
        alertProjectDto.setSenderName(senderName);
        alertProjectDto.setReceiverName(receiverName);

        log.info("✅ [getProjectAlertById] 조회 성공 - 알림 ID: {}", alertProjectDto.getId());

        return alertProjectDto;
    }

    /**
     * 🔹 AlertProject 엔티티 → AlertProjectDto 변환
     */
    private AlertProjectDto convertToDto(AlertProject alertProject, Long loginUserId) {
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
        boolean isMyProject = loginUserId.equals(alertProject.getProject().getUser().getId());
        return AlertProjectDto.builder()
                .id(alertProject.getId())
                .senderName(alertProject.getProject().getUser().getName())
                .receiverName("") // ✅ 이후 getProjectAlertById에서 설정
                .project(projectDTO) // ✅ ProjectDTO로 변환하여 저장
                .status(alertProject.getStatus().name())
                .content(alertProject.getContent())
                .type(alertProject.getType().name())
                .createdAt(alertProject.getCreatedAt())
                .isRead(alertProject.isRead())
                .isMyProject(isMyProject)
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

    public void applyProject(Long projectId, HttpServletRequest request) {
        // 1. 현재 신청자의 ID 추출 (쿠키나 토큰을 통해)
        Long applicantUserId = extractUserIdOrThrow(request);
        log.info("✅ [applyProject] 신청자 ID: {}, 프로젝트 ID: {}", applicantUserId, projectId);

        // 2. 프로젝트 정보 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트가 존재하지 않습니다. ID: " + projectId));

        // 3. 신청자 정보 조회
        User applicant = userRepository.findById(applicantUserId)
                .orElseThrow(() -> new EntityNotFoundException("신청자를 찾을 수 없습니다. ID: " + applicantUserId));

        // 4. 프로젝트 소유자 정보 (프로젝트 생성자)
        User projectOwner = project.getUser();

        // 5. 프로젝트 소유자에게 보내는 알림 생성 (상태: 신청)
        AlertProject alertForOwner = AlertProject.builder()
                .project(project)
                .user(projectOwner) // 알림 수신자가 프로젝트 생성자
                .status(AlertProject.Status.신청)
                .type(AlertProject.Type.참가알림)
                .content(applicant.getName() + " 님이 프로젝트 [" + project.getTitle() + "]에 신청했습니다.")
                .isRead(false)
                .build();
        alertProjectRepository.save(alertForOwner);
        log.info("✅ [applyProject] 프로젝트 소유자(ID: {})에게 신청 알림 생성 완료", projectOwner.getId());

        // 6. 신청자에게 보내는 알림 생성 (상태: 검토중)
        AlertProject alertForApplicant = AlertProject.builder()
                .project(project)
                .user(applicant) // 알림 수신자가 신청자
                .status(AlertProject.Status.검토중)
                .type(AlertProject.Type.참가알림)
                .content("프로젝트 [" + project.getTitle() + "]에 신청하였습니다. 검토 중입니다.")
                .isRead(false)
                .build();
        alertProjectRepository.save(alertForApplicant);
        log.info("✅ [applyProject] 신청자(ID: {})에게 검토중 알림 생성 완료", applicantUserId);
    }

    /**
     * 프로젝트 초대: 프로젝트 소유자가 특정 사용자를 초대하면,
     * 초대받은 사용자에게 초대 알림과 초대자에게 전송 확인 알림 생성
     */
    public void inviteToProject(Long projectId, Long inviteeId, HttpServletRequest request) {
        // 1. 현재 요청 사용자의 ID 추출 (초대 요청자: inviter)
        Long inviterId = extractUserIdOrThrow(request);
        log.info("✅ [inviteToProject] 초대 요청자 ID: {}, 프로젝트 ID: {}", inviterId, projectId);

        // 2. 프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트가 존재하지 않습니다. ID: " + projectId));

        // 3. 초대 요청자가 프로젝트 소유자인지 검증
        if (!project.getUser().getId().equals(inviterId)) {
            log.error("🚨 [inviteToProject] 초대 요청 실패 - 요청자가 프로젝트 소유자가 아닙니다. 요청자 ID: {}", inviterId);
            throw new IllegalStateException("프로젝트의 소유자만 초대를 보낼 수 있습니다.");
        }

        // 4. 초대받는 사용자(invitee) 조회
        User invitee = userRepository.findById(inviteeId)
                .orElseThrow(() -> new EntityNotFoundException("초대받는 사용자를 찾을 수 없습니다. ID: " + inviteeId));
        // 4-1. 초대받는 사용자가 프로젝트 소유자(즉, 자신)인 경우 예외 처리
//        if (inviteeId.equals(project.getUser().getId())) {
//            log.info("해당 프로젝트는 본인이 생성한 프로젝트이므로 초대를 보낼 수 없습니다.");
//            throw new IllegalArgumentException("자신의 프로젝트에는 초대를 보낼 수 없습니다.");
//        }

        // 5. 초대받은 사용자에게 보낼 알림 생성 (상태: 신청)
        String contentForInvitee = project.getUser().getName() + " 님이 프로젝트 [" + project.getTitle() + "]에 초대했습니다.";
        AlertProject alertForInvitee = AlertProject.builder()
                .project(project)
                .user(invitee)
                .status(AlertProject.Status.신청) // 초대의 경우 '신청' 상태 사용
                .type(AlertProject.Type.초대알림)
                .content(contentForInvitee)
                .isRead(false)
                .build();
        alertProjectRepository.save(alertForInvitee);
        log.info("✅ [inviteToProject] 초대 알림 생성 완료 - 초대받은 사용자 ID: {}", inviteeId);

        // 6. 초대를 보낸 사용자(프로젝트 소유자)에게 전송 확인 알림 생성
        String contentForInviter = "프로젝트 [" + project.getTitle() + "] 초대가 " + invitee.getName() + " 님에게 전송되었습니다.";
        AlertProject alertForInviter = AlertProject.builder()
                .project(project)
                .user(project.getUser()) // 초대 요청자, 즉 프로젝트 소유자
                .status(AlertProject.Status.신청)
                .type(AlertProject.Type.초대알림)
                .content(contentForInviter)
                .isRead(false)
                .build();
        alertProjectRepository.save(alertForInviter);
        log.info("✅ [inviteToProject] 초대 전송 확인 알림 생성 완료 - 초대자(ID: {})에게", inviterId);
    }
    /**
     * 초대 응답 처리: 초대받은 사용자가 수락/거절한 결과를
     * 프로젝트 소유자와 초대받은 사용자 모두에게 알림으로 전달
     *
     * @param projectId   프로젝트 ID
     * @param inviteId    초대 알림 ID (초대 시 생성된 알림의 ID)
     * @param accepted    true이면 수락, false이면 거절
     * @param request     HttpServletRequest (쿠키/토큰으로 사용자 ID 추출)
     */
    public void handleInviteResponse(Long projectId, Long inviteId, boolean accepted, HttpServletRequest request) {
        // 1. 현재 요청 사용자의 ID 추출 (응답자: 초대받은 사용자)
        Long inviteeId = extractUserIdOrThrow(request);
        log.info("✅ [handleInviteResponse] 초대 알림 ID: {}, 응답자 ID: {}, 수락 여부: {}", inviteId, inviteeId, accepted);

        // 2. 초대 알림(inviteAlert) 조회
        AlertProject inviteAlert = alertProjectRepository.findById(inviteId)
                .orElseThrow(() -> new EntityNotFoundException("초대 알림을 찾을 수 없습니다. ID: " + inviteId));

        // 3. 프로젝트 ID 일치 검증
        if (!inviteAlert.getProject().getId().equals(projectId)) {
            log.error("🚨 [handleInviteResponse] 프로젝트 정보 불일치");
            throw new IllegalStateException("프로젝트 정보가 일치하지 않습니다.");
        }

        // 4. 초대 응답 권한 검증: 초대 알림에 기록된 사용자가 현재 요청 사용자와 일치하는지 확인
        if (!inviteAlert.getUser().getId().equals(inviteeId)) {
            log.error("🚨 [handleInviteResponse] 초대 응답 권한 없음");
            throw new IllegalStateException("초대 응답 권한이 없습니다.");
        }

        // 5. 프로젝트 소유자 정보 조회 (응답 결과를 받을 대상)
        User projectOwner = inviteAlert.getProject().getUser();

        // 6. 기존 초대 알림을 읽음 처리 (선택 사항)
        inviteAlert.markAsRead();

        // 7. 응답에 따른 새로운 알림 생성 (프로젝트 소유자에게 전달)
        AlertProject.Status newStatus = accepted ? AlertProject.Status.합격 : AlertProject.Status.불합격;
        String contentForOwner = accepted
                ? inviteAlert.getUser().getName() + " 님이 프로젝트 [" + inviteAlert.getProject().getTitle() + "] 초대를 수락했습니다."
                : inviteAlert.getUser().getName() + " 님이 프로젝트 [" + inviteAlert.getProject().getTitle() + "] 초대를 거절했습니다.";
        AlertProject responseAlertForOwner = AlertProject.builder()
                .project(inviteAlert.getProject())
                .user(projectOwner)
                .status(newStatus)
                .content(contentForOwner)
                .isRead(false)
                .build();
        alertProjectRepository.save(responseAlertForOwner);
        log.info("✅ [handleInviteResponse] 프로젝트 소유자에게 응답 알림 생성 완료: {}", newStatus);

        // 8. 응답 결과를 초대받은 사용자에게도 알림 생성 (자신의 응답 확인)
        String contentForInvitee = "프로젝트 [" + inviteAlert.getProject().getTitle() + "] 초대를 " + (accepted ? "수락" : "거절") + "하였습니다.";
        AlertProject responseAlertForInvitee = AlertProject.builder()
                .project(inviteAlert.getProject())
                .user(inviteAlert.getUser())  // 초대받은 사용자
                .status(newStatus)
                .content(contentForInvitee)
                .isRead(false)
                .build();
        alertProjectRepository.save(responseAlertForInvitee);
        log.info("✅ [handleInviteResponse] 초대받은 사용자에게 응답 확인 알림 생성 완료");
    }
    /**
     * 프로젝트 신청 응답 처리: 프로젝트 소유자가 신청자에 대한 응답(수락/거절)을 처리하고,
     * 양쪽(신청자와 소유자) 모두에게 결과 알림을 전송
     *
     * @param projectId    프로젝트 ID
     * @param applicantId  신청자 ID
     * @param accepted     true이면 수락, false이면 거절
     * @param request      HttpServletRequest (쿠키/토큰으로 사용자 ID 추출)
     */
    public void handleApplication(Long projectId, Long applicantId, boolean accepted, HttpServletRequest request) {
        // 1. 현재 요청 사용자의 ID 추출 (응답 처리자: 프로젝트 소유자)
        Long ownerId = extractUserIdOrThrow(request);
        log.info("✅ [handleApplication] 프로젝트 ID: {}, 신청자 ID: {}, 응답자(소유자) ID: {}, 수락 여부: {}",
                projectId, applicantId, ownerId, accepted);

        // 2. 프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트를 찾을 수 없습니다. ID: " + projectId));

        // 3. 요청 사용자가 프로젝트 소유자인지 검증
        if (!project.getUser().getId().equals(ownerId)) {
            log.error("🚨 [handleApplication] 권한 없음 - 프로젝트 소유자만 신청 응답을 할 수 있습니다.");
            throw new IllegalStateException("프로젝트 소유자만 신청 응답을 할 수 있습니다.");
        }

        // 4. 신청자 정보 조회
        User applicant = userRepository.findById(applicantId)
                .orElseThrow(() -> new EntityNotFoundException("신청자를 찾을 수 없습니다. ID: " + applicantId));

        // 5. 기존 신청 알림(상태: 검토중) 조회
        Optional<AlertProject> optionalAlert = alertProjectRepository.findByProjectIdAndUserIdAndStatus(
                projectId, applicantId, AlertProject.Status.검토중);
        if (optionalAlert.isEmpty()) {
            log.error("🚨 [handleApplication] 해당 신청 알림을 찾을 수 없습니다. 프로젝트 ID: {}, 신청자 ID: {}",
                    projectId, applicantId);
            throw new EntityNotFoundException("해당 신청 알림을 찾을 수 없습니다.");
        }
        AlertProject existingAlert = optionalAlert.get();

        // 6. 기존 신청 알림을 읽음 처리 (선택 사항)
        existingAlert.markAsRead();

        // 7. 응답에 따른 새로운 알림 생성 (신청자에게 전달)
        AlertProject.Status newStatus = accepted ? AlertProject.Status.합격 : AlertProject.Status.불합격;
        String contentForApplicant = accepted
                ? "프로젝트 [" + project.getTitle() + "] 참가 신청이 수락되었습니다."
                : "프로젝트 [" + project.getTitle() + "] 참가 신청이 거절되었습니다.";
        AlertProject responseAlertForApplicant = AlertProject.builder()
                .project(project)
                .user(applicant)
                .status(newStatus)
                .type(AlertProject.Type.참가알림)
                .content(contentForApplicant)
                .isRead(false)
                .build();
        alertProjectRepository.save(responseAlertForApplicant);
        log.info("✅ [handleApplication] 신청자(ID: {})에게 응답 알림 생성 완료: {}", applicantId, newStatus);

        // 8. 응답 결과를 프로젝트 소유자(응답 처리자)에게도 알림 생성 (자신의 응답 확인)
        String contentForOwner = "신청자 " + applicant.getName() + " 의 참가 신청에 대해 " + (accepted ? "수락" : "거절") + " 응답을 전송하였습니다.";
        AlertProject responseAlertForOwner = AlertProject.builder()
                .project(project)
                .user(project.getUser()) // 프로젝트 소유자
                .status(newStatus)
                .type(AlertProject.Type.참가알림)
                .content(contentForOwner)
                .isRead(false)
                .build();
        alertProjectRepository.save(responseAlertForOwner);
        log.info("✅ [handleApplication] 프로젝트 소유자에게 응답 확인 알림 생성 완료");
    }
}
