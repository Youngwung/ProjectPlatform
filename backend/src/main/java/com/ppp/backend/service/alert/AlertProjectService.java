package com.ppp.backend.service.alert;

import com.ppp.backend.controller.AuthApiController;
import com.ppp.backend.domain.Project;
import com.ppp.backend.domain.User;
import com.ppp.backend.domain.alert.AlertProject;
import com.ppp.backend.dto.LinkDto;
import com.ppp.backend.dto.ProjectDTO;
import com.ppp.backend.dto.UserDto;
import com.ppp.backend.dto.alert.AlertProjectDto;
import com.ppp.backend.repository.LinkRepository;
import com.ppp.backend.repository.LinkTypeRepository;
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
    private final LinkRepository linkRepository;
    private final LinkTypeRepository linkTypeRepository;

    /**
     * 🔹 유저의 모든 프로젝트 알림 조회 (DTO 변환)
     */
    public List<AlertProjectDto> getUserProjectAlerts(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [getUserProjectAlerts] 유저 ID: {}", userId);

        return alertProjectRepository.findByAlertOwnerId(userId).stream()
                .map(alert -> convertToDto(alert, userId))
                .collect(Collectors.toList());
    }

    /**
     * 🔹 유저의 읽지 않은 프로젝트 알림 조회 (DTO 변환)
     */
    public List<AlertProjectDto> getUnreadProjectAlerts(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [getUnreadProjectAlerts] 유저 ID: {}", userId);

        return alertProjectRepository.findByAlertOwnerIdAndIsRead(userId, false).stream()
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

        AlertProjectDto alertProjectDto = convertToDto(alertProject, userId);
        log.info("✅ [getProjectAlertById] 조회 성공 - 알림 ID: {}", alertProjectDto.getId());
        return alertProjectDto;
    }

    /**
     * 🔹 AlertProject 엔티티 → AlertProjectDto 변환
     * senderUserDto와 receiverUserDto를 분리하여 담습니다.
     */
    private AlertProjectDto convertToDto(AlertProject alertProject, Long loginUserId) {
        // 프로젝트 타입 조회
        String projectType = projectTypeRepository.findByProjectId(alertProject.getProject().getId())
                .map(pt -> pt.getType().name())
                .orElse(null);

        // ProjectDTO 변환
        ProjectDTO projectDTO = ProjectDTO.builder()
                .id(alertProject.getProject().getId())
                .userId(alertProject.getProject().getUser().getId())
                .title(alertProject.getProject().getTitle())
                .description(alertProject.getProject().getDescription())
                .maxPeople(alertProject.getProject().getMaxPeople())
                .status(alertProject.getProject().getStatus().name())
                .isPublic(alertProject.getProject().isPublic())
                .type(projectType)
                .createdAt(alertProject.getProject().getCreatedAt().toLocalDateTime())
                .updatedAt(alertProject.getProject().getUpdatedAt().toLocalDateTime())
                .build();

        // 로그인한 사용자가 프로젝트 소유자인지 확인 (내 프로젝트 여부)
        boolean isMyProject = loginUserId != null && loginUserId.equals(alertProject.getProject().getUser().getId());

        User senderUser = alertProject.getSenderId();
        User receiverUser = alertProject.getReceiverId();
        User alertOwnerUser = alertProject.getAlertOwnerId();
        // 엔티티에서 저장된 sender, receiver, alertOwner 정보를 그대로 사용
        UserDto senderUserDto = convertToUserDto(alertProject.getSenderId());
        UserDto receiverUserDto = convertToUserDto(alertProject.getReceiverId());
        UserDto alertOwnerUserDto = convertToUserDto(alertProject.getAlertOwnerId());
        // 디버깅용 로그 출력
        log.debug("convertToDto - senderUser id: {}, name: {}", senderUser.getId(), senderUser.getName());
        log.debug("convertToDto - receiverUser id: {}, name: {}", receiverUser.getId(), receiverUser.getName());
        log.debug("convertToDto - alertOwnerUser id: {}, name: {}", alertOwnerUser.getId(), alertOwnerUser.getName());
        return AlertProjectDto.builder()
                .id(alertProject.getId())
                .project(projectDTO)
                .status(alertProject.getStatus().name())
                .content(alertProject.getContent())
                .type(alertProject.getType().name())
                .createdAt(alertProject.getCreatedAt())
                .isRead(alertProject.isRead())
                .isMyProject(isMyProject)
                .step(alertProject.getStep())
                .senderUserDto(senderUserDto)
                .receiverUserDto(receiverUserDto)
                .alertOwnerUserDto(alertOwnerUserDto)
                .build();
    }



    /**
     * 🔹 User 엔티티 → UserDto 변환
     * User 엔티티에 links 필드가 없으므로, LinkRepository를 통해 userId로 링크들을 조회하여 LinkDto로 변환합니다.
     */
    private UserDto convertToUserDto(User user) {
        List<LinkDto> linkDtos = linkRepository.findByUserId(user.getId()).stream()
                .map(link -> LinkDto.builder()
                        .id(link.getId())
                        .userId(link.getUser().getId())
                        .linkTypeId(link.getLinkType() != null ? link.getLinkType().getId() : 1L)
                        .url(link.getUrl())
                        .description(link.getDescription())
                        .build())
                .collect(Collectors.toList());

        // TODO: user의 skill 정보 변환 로직 추가 필요 시

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .experience(user.getExperience())
                .links(linkDtos)
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

        int updatedCount = alertProjectRepository.markAllAsReadByAlertOwnerId(userId);
        log.info("✅ [markAllProjectAlertsAsRead] 총 {}개의 프로젝트 알림 읽음 처리 완료", updatedCount);
    }

    /**
     * 🔹 프로젝트 신청 처리
     * 신청 시, 두 개의 원본 알림을 생성합니다.
     * - 프로젝트 소유자 알림: 발신인 = 신청자, 수신인 = 프로젝트 소유자, alertOwner = 프로젝트 소유자
     * - 신청자 알림: 발신인 = 프로젝트 소유자, 수신인 = 신청자, alertOwner = 신청자
     */
    public void applyProject(Long projectId, HttpServletRequest request) {
        Long applicantUserId = extractUserIdOrThrow(request);
        log.info("✅ [applyProject] 신청자 ID: {}, 프로젝트 ID: {}", applicantUserId, projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트가 존재하지 않습니다. ID: " + projectId));
        User applicant = userRepository.findById(applicantUserId)
                .orElseThrow(() -> new EntityNotFoundException("신청자를 찾을 수 없습니다. ID: " + applicantUserId));
        User projectOwner = project.getUser();

        // 프로젝트 소유자 알림: 신청자(발신인) → 프로젝트 소유자(수신인), alertOwnerId = 프로젝트 소유자
        AlertProject alertForOwner = AlertProject.builder()
                .project(project)
                .senderId(applicant)         // 발신인: 신청자
                .receiverId(projectOwner)      // 수신인: 프로젝트 소유자
                .alertOwnerId(projectOwner)    // 알림 소유자: 프로젝트 소유자
                .status(AlertProject.Status.신청)
                .type(AlertProject.Type.참가알림)
                .content(applicant.getName() + " 님이 프로젝트 [" + project.getTitle() + "]에 신청했습니다.")
                .isRead(false)
                .step(1)
                .build();
        alertProjectRepository.save(alertForOwner);
        log.info("✅ [applyProject] 프로젝트 소유자 알림 생성 완료, alert ID: {}", alertForOwner.getId());

        // 신청자 알림: 프로젝트 소유자(발신인) → 신청자(수신인), alertOwnerId = 신청자
        AlertProject alertForApplicant = AlertProject.builder()
                .project(project)
                .senderId(projectOwner)        // 발신인: 프로젝트 소유자
                .receiverId(applicant)         // 수신인: 신청자
                .alertOwnerId(applicant)       // 알림 소유자: 신청자
                .status(AlertProject.Status.검토중)
                .type(AlertProject.Type.참가알림)
                .content("프로젝트 [" + project.getTitle() + "]에 신청하였습니다. 검토 중입니다.")
                .isRead(false)
                .step(1)
                .build();
        alertProjectRepository.save(alertForApplicant);
        log.info("✅ [applyProject] 신청자 알림 생성 완료, alert ID: {}", alertForApplicant.getId());
    }

    /**
     * 🔹 프로젝트 초대 처리
     * 초대 시, 두 개의 원본 알림을 생성합니다.
     * - 초대받는 사용자 알림: 발신인 = 프로젝트 소유자, 수신인 = 초대받는 사용자, alertOwner = 프로젝트 소유자
     * - 초대 전송 확인 알림: 발신인 = 프로젝트 소유자, 수신인 = 프로젝트 소유자, alertOwner = 프로젝트 소유자
     */
    public void inviteToProject(Long projectId, Long inviteeId, HttpServletRequest request) {
        Long inviterId = extractUserIdOrThrow(request);
        log.info("✅ [inviteToProject] 초대 요청자 ID: {}, 프로젝트 ID: {}", inviterId, projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트가 존재하지 않습니다. ID: " + projectId));
        if (!project.getUser().getId().equals(inviterId)) {
            log.error("🚨 [inviteToProject] 초대 요청 실패 - 요청자가 프로젝트 소유자가 아닙니다. 요청자 ID: {}", inviterId);
            throw new IllegalStateException("프로젝트의 소유자만 초대를 보낼 수 있습니다.");
        }
        User invitee = userRepository.findById(inviteeId)
                .orElseThrow(() -> new EntityNotFoundException("초대받는 사용자를 찾을 수 없습니다. ID: " + inviteeId));

        // 초대받는 사용자 알림: 발신인 = 프로젝트 소유자, 수신인 = 초대받는 사용자, alertOwnerId = 프로젝트 소유자
        String contentForInvitee = project.getUser().getName() + " 님이 프로젝트 [" + project.getTitle() + "]에 초대했습니다.";
        AlertProject alertForInvitee = AlertProject.builder()
                .project(project)
                .senderId(project.getUser())   // 발신인: 초대를 보낸 사람 (프로젝트 소유자)
                .receiverId(invitee)           // 수신인: 초대받는 사용자
                .alertOwnerId(project.getUser())// 알림 소유자: 프로젝트 소유자
                .status(AlertProject.Status.신청)
                .type(AlertProject.Type.초대알림)
                .content(contentForInvitee)
                .isRead(false)
                .step(1)
                .build();
        alertProjectRepository.save(alertForInvitee);
        log.info("✅ [inviteToProject] 초대받는 사용자 알림 생성 완료, alert ID: {}", alertForInvitee.getId());

        // 초대 전송 확인 알림: 발신인 = 프로젝트 소유자, 수신인 = 프로젝트 소유자, alertOwnerId = 프로젝트 소유자
        String contentForInviter = "프로젝트 [" + project.getTitle() + "] 초대가 " + invitee.getName() + " 님에게 전송되었습니다.";
        AlertProject alertForInviter = AlertProject.builder()
                .project(project)
                .senderId(project.getUser())   // 발신인: 프로젝트 소유자
                .receiverId(project.getUser()) // 수신인: 본인(초대 보낸 사람)
                .alertOwnerId(project.getUser())// 알림 소유자: 프로젝트 소유자
                .status(AlertProject.Status.신청)
                .type(AlertProject.Type.초대알림)
                .content(contentForInviter)
                .isRead(false)
                .step(1)
                .build();
        alertProjectRepository.save(alertForInviter);
        log.info("✅ [inviteToProject] 초대 전송 확인 알림 생성 완료, alert ID: {}", alertForInviter.getId());
    }


    /**
     * 🔹 초대 응답 처리
     * 초대받은 사용자가 수락/거절한 결과를 처리하며,
     * - 기존 원본 알림(초대받는 사용자 알림)은 업데이트되어 step을 2로 변경
     * - 새 응답 알림을 각각 프로젝트 소유자와 초대받은 사용자에게 생성 (step 3)
     */
    public void handleInviteResponse(Long projectId, Long inviteId, boolean accepted, HttpServletRequest request) {
        Long inviteeId = extractUserIdOrThrow(request);
        log.info("✅ [handleInviteResponse] 초대 알림 ID: {}, 응답자 ID: {}, 수락 여부: {}", inviteId, inviteeId, accepted);

        AlertProject inviteAlert = alertProjectRepository.findById(inviteId)
                .orElseThrow(() -> new EntityNotFoundException("초대 알림을 찾을 수 없습니다. ID: " + inviteId));
        if (!inviteAlert.getProject().getId().equals(projectId)) {
            log.error("🚨 [handleInviteResponse] 프로젝트 정보 불일치");
            throw new IllegalStateException("프로젝트 정보가 일치하지 않습니다.");
        }
        // 초대 응답은 수신인(receiverId)로 설정된 사용자가 응답해야 함
        if (!inviteAlert.getReceiverId().getId().equals(inviteeId)) {
            log.error("🚨 [handleInviteResponse] 초대 응답 권한 없음");
            throw new IllegalStateException("초대 응답 권한이 없습니다.");
        }
        User projectOwner = inviteAlert.getProject().getUser();

        // 기존 원본 알림 업데이트: step 1 -> 2, 상태 업데이트 및 읽음 처리
        AlertProject.Status newStatus = accepted ? AlertProject.Status.초대수락 : AlertProject.Status.초대거절;
        inviteAlert.setStatus(newStatus);
        inviteAlert.markAsRead();
        inviteAlert.setStep(2);
        alertProjectRepository.save(inviteAlert);
        log.info("✅ [handleInviteResponse] 기존 알림 업데이트 완료: {} / step: {}", newStatus, inviteAlert.getStep());

        // 새 응답 알림 생성 (프로젝트 소유자 대상): 발신인 = 프로젝트 소유자, 수신인 = 프로젝트 소유자, alertOwnerId = 프로젝트 소유자, step 3
        String contentForOwner = accepted
                ? inviteAlert.getSenderId().getName() + " 님이 프로젝트 [" + inviteAlert.getProject().getTitle() + "] 초대를 수락했습니다."
                : inviteAlert.getSenderId().getName() + " 님이 프로젝트 [" + inviteAlert.getProject().getTitle() + "] 초대를 거절했습니다.";
        AlertProject responseAlertForOwner = AlertProject.builder()
                .project(inviteAlert.getProject())
                .senderId(projectOwner)
                .receiverId(projectOwner)
                .alertOwnerId(projectOwner)
                .status(newStatus)
                .type(AlertProject.Type.초대알림)
                .content(contentForOwner)
                .isRead(false)
                .step(3)
                .build();
        alertProjectRepository.save(responseAlertForOwner);
        log.info("✅ [handleInviteResponse] 새로운 알림(프로젝트 소유자 대상) 생성 완료, alert ID: {}", responseAlertForOwner.getId());

        // 새 응답 알림 생성 (초대받은 사용자 대상): 발신인 = 프로젝트 소유자, 수신인 = 초대받은 사용자, alertOwnerId = 프로젝트 소유자, step 3
        String contentForInvitee = "프로젝트 [" + inviteAlert.getProject().getTitle() + "] 초대를 " + (accepted ? "수락" : "거절") + "하였습니다.";
        AlertProject responseAlertForInvitee = AlertProject.builder()
                .project(inviteAlert.getProject())
                .senderId(projectOwner)
                .receiverId(inviteAlert.getReceiverId())
                .alertOwnerId(projectOwner)
                .status(newStatus)
                .type(AlertProject.Type.초대알림)
                .content(contentForInvitee)
                .isRead(false)
                .step(3)
                .build();
        alertProjectRepository.save(responseAlertForInvitee);
        log.info("✅ [handleInviteResponse] 새로운 알림(초대받은 사용자 대상) 생성 완료, alert ID: {}", responseAlertForInvitee.getId());
    }


    /**
     * 🔹 프로젝트 신청 응답 처리
     * 프로젝트 소유자가 신청자에 대한 응답을 처리하며,
     * - 기존 신청 알림(원본, step 1)을 업데이트하여 step 2로 변경
     * - 새 응답 알림을 각각 신청자와 프로젝트 소유자에게 생성 (step 3)
     */
    public void handleApplication(Long projectId, Long applicantId, boolean accepted, HttpServletRequest request) {
        Long ownerId = extractUserIdOrThrow(request);
        log.info("✅ [handleApplication] 프로젝트 ID: {}, 신청자 ID: {}, 응답자(소유자) ID: {}, 수락 여부: {}",
                projectId, applicantId, ownerId, accepted);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("프로젝트를 찾을 수 없습니다. ID: " + projectId));
        if (!project.getUser().getId().equals(ownerId)) {
            log.error("🚨 [handleApplication] 권한 없음 - 프로젝트 소유자만 신청 응답을 할 수 있습니다.");
            throw new IllegalStateException("프로젝트 소유자만 신청 응답을 할 수 있습니다.");
        }
        User applicant = userRepository.findById(applicantId)
                .orElseThrow(() -> new EntityNotFoundException("신청자를 찾을 수 없습니다. ID: " + applicantId));

        // 새로운 상태 결정: 수락 → 합격, 거절 → 불합격
        AlertProject.Status newStatus = accepted ? AlertProject.Status.합격 : AlertProject.Status.불합격;


        // 기존 신청 알림(신청자용) 업데이트: alertOwnerId가 신청자 ID인 알림
        Optional<AlertProject> optionalApplicantAlert = alertProjectRepository
                .findApplicantAlertByProjectIdAndAlertOwnerIdAndStatus(projectId, applicantId, AlertProject.Status.검토중);
        if (optionalApplicantAlert.isEmpty()) {
            log.error("🚨 [handleApplication] 신청자용 해당 신청 알림을 찾을 수 없습니다. 프로젝트 ID: {}, 신청자 ID: {}",
                    projectId, applicantId);
            throw new EntityNotFoundException("신청자용 해당 신청 알림을 찾을 수 없습니다.");
        }
        AlertProject applicantAlert = optionalApplicantAlert.get();
        applicantAlert.markAsRead();
        applicantAlert.setStatus(newStatus);
        applicantAlert.setStep(2);
        alertProjectRepository.save(applicantAlert);
        log.info("✅ [handleApplication] 신청자용 기존 알림 업데이트 완료: alert ID: {} / step: {}", applicantAlert.getId(), applicantAlert.getStep());

        // 기존 신청 알림(소유자용) 업데이트: alertOwnerId가 프로젝트 소유자 ID인 알림
        Optional<AlertProject> optionalOwnerAlert = alertProjectRepository
                .findOwnerAlertByProjectIdAndAlertOwnerIdAndStatus(projectId, ownerId, AlertProject.Status.신청);
        if (optionalOwnerAlert.isEmpty()) {
            log.error("🚨 [handleApplication] 소유자용 해당 신청 알림을 찾을 수 없습니다. 프로젝트 ID: {}, 소유자 ID: {}",
                    projectId, ownerId);
            throw new EntityNotFoundException("소유자용 해당 신청 알림을 찾을 수 없습니다.");
        }
        AlertProject ownerAlert = optionalOwnerAlert.get();
        ownerAlert.markAsRead();
        ownerAlert.setStatus(newStatus);
        ownerAlert.setStep(2);
        alertProjectRepository.save(ownerAlert);
        log.info("✅ [handleApplication] 소유자용 기존 알림 업데이트 완료: alert ID: {} / step: {}", ownerAlert.getId(), ownerAlert.getStep());
        log.info("asdfafsdafdsafs");
        // 새 응답 알림 생성 (신청자 대상): 발신인 = 프로젝트 소유자, 수신인 = 신청자, alertOwnerId = 신청자, step 3
        String contentForApplicant = accepted
                ? "프로젝트 [" + project.getTitle() + "] 참가 신청이 수락되었습니다."
                : "프로젝트 [" + project.getTitle() + "] 참가 신청이 거절되었습니다.";
        AlertProject responseAlertForApplicant = AlertProject.builder()
                .project(project)
                .senderId(project.getUser())
                .receiverId(applicant)
                .alertOwnerId(applicant)
                .status(newStatus)
                .type(AlertProject.Type.참가알림)
                .content(contentForApplicant)
                .isRead(false)
                .step(3)
                .build();
        alertProjectRepository.save(responseAlertForApplicant);
        log.info("✅ [handleApplication] 새로운 알림(신청자 대상) 생성 완료, alert ID: {}", responseAlertForApplicant.getId());

        // 새 응답 알림 생성 (프로젝트 소유자 대상): 발신인 = 신청자, 수신인 = 프로젝트 소유자, alertOwnerId = 프로젝트 소유자, step 3
        String contentForOwner = "신청자 " + applicant.getName() + " 의 참가 신청에 대해 " + (accepted ? "수락" : "거절") + " 응답을 전송하였습니다.";
        AlertProject responseAlertForOwner = AlertProject.builder()
                .project(project)
                .senderId(applicant)
                .receiverId(project.getUser())
                .alertOwnerId(project.getUser())
                .status(newStatus)
                .type(AlertProject.Type.참가알림)
                .content(contentForOwner)
                .isRead(false)
                .step(3)
                .build();
        alertProjectRepository.save(responseAlertForOwner);
        log.info("✅ [handleApplication] 새로운 알림(소유자 대상) 생성 완료, alert ID: {}", responseAlertForOwner.getId());
    }

}
