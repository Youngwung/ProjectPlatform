package com.ppp.backend.controller.alert;

import com.ppp.backend.dto.alert.AlertProjectDto;
import com.ppp.backend.service.alert.AlertProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alert/project")
@RequiredArgsConstructor
@Slf4j
public class AlertProjectApiController {

    private final AlertProjectService alertProjectService;

    /**
     * 🔹 유저의 모든 프로젝트 알림 조회 (DTO 반환)
     */
    @GetMapping("/list")
    public ResponseEntity<List<AlertProjectDto>> getUserProjectAlerts(HttpServletRequest request) {
        log.info("✅ [GET] /api/alert/project/list - 프로젝트 알림 조회 요청");
        List<AlertProjectDto> alerts = alertProjectService.getUserProjectAlerts(request);
        return ResponseEntity.ok(alerts);
    }

    /**
     * 🔹 유저의 읽지 않은 프로젝트 알림 조회 (DTO 반환)
     */
    @GetMapping("/unread")
    public ResponseEntity<List<AlertProjectDto>> getUnreadProjectAlerts(HttpServletRequest request) {
        log.info("✅ [GET] /api/alert/project/unread - 읽지 않은 프로젝트 알림 조회 요청");
        List<AlertProjectDto> unreadAlerts = alertProjectService.getUnreadProjectAlerts(request);
        return ResponseEntity.ok(unreadAlerts);
    }
    @GetMapping("/{alertId}")
    public ResponseEntity<AlertProjectDto> getProjectAlertById(@PathVariable Long alertId,HttpServletRequest request) {
        log.info("✅ [GET] /api/alert/project/{} - 특정 프로젝트 알림 조회 요청", alertId);
        AlertProjectDto alert = alertProjectService.getProjectAlertById(alertId,request);

        if (alert == null) {
            log.warn("🚨 [GET] 프로젝트 알림 조회 실패 - ID: {}", alertId);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(alert);
    }
    /**
     * 🔹 새로운 프로젝트 알림 생성 (DTO 기반)
     */
    @PostMapping
    public ResponseEntity<AlertProjectDto> createProjectAlert(@RequestBody AlertProjectDto alertProjectDto) {
        log.info("✅ [POST] /api/alert/project - 새로운 프로젝트 알림 생성 요청: {}", alertProjectDto);
        AlertProjectDto createdAlert = alertProjectService.createProjectAlert(alertProjectDto);
        return ResponseEntity.ok(createdAlert);
    }

    /**
     * 🔹 특정 프로젝트 알림 읽음 처리
     */
    @PutMapping("/{alertId}/read")
    public ResponseEntity<Void> markProjectAlertAsRead(@PathVariable Long alertId) {
        log.info("✅ [PUT] /api/alert/project/{}/read - 알림 읽음 처리 요청", alertId);
        alertProjectService.markProjectAlertAsRead(alertId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/all/read")
    public ResponseEntity<Void> markAllProjectAlertsAsRead(HttpServletRequest request) {
        alertProjectService.markAllProjectAlertsAsRead(request);
        return ResponseEntity.ok().build();
    }

    /**
     * 🔹 특정 프로젝트 알림 삭제
     */
    @DeleteMapping("/{alertId}")
    public ResponseEntity<Void> deleteProjectAlert(@PathVariable Long alertId) {
        log.info("✅ [DELETE] /api/alert/project/{} - 알림 삭제 요청", alertId);
        alertProjectService.deleteProjectAlert(alertId);
        return ResponseEntity.ok().build();
    }
}
