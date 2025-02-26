package com.ppp.backend.controller.alert;

import com.ppp.backend.dto.alert.AlertPortfolioDto;
import com.ppp.backend.service.alert.AlertPortfolioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alert/portfolio")
@RequiredArgsConstructor
@Slf4j
public class AlertPortfolioApiController {

    private final AlertPortfolioService alertPortfolioService;

    /**
     * 유저의 모든 포트폴리오 알림 조회 (DTO 반환)
     */
    @GetMapping("/list")
    public ResponseEntity<List<AlertPortfolioDto>> getUserPortfolioAlerts(HttpServletRequest request) {
        List<AlertPortfolioDto> alerts = alertPortfolioService.getUserPortfolioAlerts(request);
        return ResponseEntity.ok(alerts);
    }

    /**
     * 유저의 읽지 않은 포트폴리오 알림 조회 (DTO 반환)
     */
    @GetMapping("/unread")
    public ResponseEntity<List<AlertPortfolioDto>> getUnreadPortfolioAlerts(HttpServletRequest request) {
        List<AlertPortfolioDto> unreadAlerts = alertPortfolioService.getUnreadPortfolioAlerts(request);
        return ResponseEntity.ok(unreadAlerts);
    }

    /**
     * 새로운 포트폴리오 알림 생성 (DTO로 요청 받고, DTO 반환)
     */
    @PostMapping
    public ResponseEntity<AlertPortfolioDto> createPortfolioAlert(@RequestBody AlertPortfolioDto alertPortfolioDto) {
        AlertPortfolioDto createdAlert = alertPortfolioService.createPortfolioAlert(alertPortfolioDto);
        return ResponseEntity.ok(createdAlert);
    }

    /**
     * 특정 포트폴리오 알림 읽음 처리
     */
    @PutMapping("/{alertId}/read")
    public ResponseEntity<Void> markPortfolioAlertAsRead(@PathVariable Long alertId) {
        alertPortfolioService.markPortfolioAlertAsRead(alertId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/all/read")
    public ResponseEntity<Void> markAllPortfolioAlertsAsRead(HttpServletRequest request) {
        alertPortfolioService.markAllPortfolioAlertsAsRead(request);
        return ResponseEntity.ok().build();
    }
    /**
     * 특정 포트폴리오 알림 삭제
     */
    @DeleteMapping("/{alertId}")
    public ResponseEntity<Void> deletePortfolioAlert(@PathVariable Long alertId) {
        alertPortfolioService.deletePortfolioAlert(alertId);
        return ResponseEntity.ok().build();
    }
}
