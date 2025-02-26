package com.ppp.backend.service.alert;

import com.ppp.backend.controller.AuthApiController;
import com.ppp.backend.domain.alert.AlertPortfolio;
import com.ppp.backend.dto.alert.AlertPortfolioDto;
import com.ppp.backend.repository.PortfolioRepository;
import com.ppp.backend.repository.alert.AlertPortfolioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j // 🔥 로그 추가
public class AlertPortfolioService {

    private final AlertPortfolioRepository alertPortfolioRepository;
    private final AuthApiController authApiController; // 유저 ID 추출을 위한 컨트롤러
    private final PortfolioRepository portfolioRepository;

    /**
     * 🔹 유저의 모든 포트폴리오 알림 조회 (DTO 변환)
     */
    public List<AlertPortfolioDto> getUserPortfolioAlerts(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [getUserPortfolioAlerts] 유저 ID: {}", userId);

        List<AlertPortfolioDto> alertDtos = alertPortfolioRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        log.info("✅ [getUserPortfolioAlerts] 조회된 알림 개수: {}", alertDtos);
        return alertDtos;
    }

    /**
     * 🔹 유저의 읽지 않은 포트폴리오 알림 조회 (DTO 변환)
     */
    public List<AlertPortfolioDto> getUnreadPortfolioAlerts(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [getUnreadPortfolioAlerts] 유저 ID: {}", userId);

        List<AlertPortfolioDto> alertDtos = alertPortfolioRepository.findByUserIdAndIsRead(userId, false).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        log.info("✅ [getUnreadPortfolioAlerts] 조회된 읽지 않은 알림 개수: {}", alertDtos);
        return alertDtos;
    }

    /**
     * 🔹 새로운 포트폴리오 알림 저장 (DTO 변환 후 저장)
     */
    public AlertPortfolioDto createPortfolioAlert(AlertPortfolioDto alertPortfolioDto) {
        log.info("✅ [createPortfolioAlert] 요청 데이터: {}", alertPortfolioDto);

        // ✅ Portfolio 엔티티 조회 (orElseThrow 예외 메시지 추가)
        var portfolio = portfolioRepository.findById(alertPortfolioDto.getPortfolioId())
                .orElseThrow(() -> {
                    log.error("❌ [createPortfolioAlert] 포트폴리오 ID {} 찾을 수 없음", alertPortfolioDto.getPortfolioId());
                    return new EntityNotFoundException("해당 포트폴리오를 찾을 수 없습니다. ID: " + alertPortfolioDto.getPortfolioId());
                });

        // ✅ DTO → 엔티티 변환 (createdAt 자동 생성)
        AlertPortfolio alertPortfolio = AlertPortfolio.builder()
                .portfolio(portfolio)
                .status(AlertPortfolio.Status.valueOf(alertPortfolioDto.getStatus()))
                .content(alertPortfolioDto.getContent())
                .isRead(false) // 새 알림은 기본적으로 읽지 않은 상태
                .build();

        // ✅ 엔티티 저장 후 DTO 반환
        AlertPortfolio savedAlert = alertPortfolioRepository.save(alertPortfolio);
        log.info("✅ [createPortfolioAlert] 알림 저장 완료 - ID: {}", savedAlert.getId());

        return convertToDto(savedAlert);
    }
    public void markAllPortfolioAlertsAsRead(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [markAllPortfolioAlertsAsRead] 유저 ID {}의 모든 알림 읽음 처리 요청", userId);

        int updatedCount = alertPortfolioRepository.markAllAsReadByUserId(userId);
        log.info("✅ [markAllPortfolioAlertsAsRead] 총 {}개의 알림 읽음 처리 완료", updatedCount);
    }
    /**
     * 🔹 포트폴리오 알림을 읽음 처리
     */
    @Transactional
    public void markPortfolioAlertAsRead(Long alertId) {
        log.info("✅ [markPortfolioAlertAsRead] 알림 ID: {}", alertId);

        AlertPortfolio alert = alertPortfolioRepository.findById(alertId)
                .orElseThrow(() -> {
                    log.error("❌ [markPortfolioAlertAsRead] 알림 ID {} 찾을 수 없음", alertId);
                    return new EntityNotFoundException("해당 알림을 찾을 수 없습니다. ID: " + alertId);
                });

        alert.markAsRead(); // ✅ 변경 감지(Dirty Checking)로 자동 저장됨
        log.info("✅ [markPortfolioAlertAsRead] 알림 ID {} 읽음 처리 완료", alertId);
    }

    /**
     * 🔹 특정 포트폴리오 알림 삭제
     */
    public void deletePortfolioAlert(Long alertId) {
        log.info("✅ [deletePortfolioAlert] 삭제 요청 - 알림 ID: {}", alertId);

        if (!alertPortfolioRepository.existsById(alertId)) {
            log.error("❌ [deletePortfolioAlert] 알림 ID {} 찾을 수 없음", alertId);
            throw new EntityNotFoundException("해당 알림을 찾을 수 없습니다. ID: " + alertId);
        }

        alertPortfolioRepository.deleteById(alertId);
        log.info("✅ [deletePortfolioAlert] 알림 ID {} 삭제 완료", alertId);
    }

    /**
     * 🔹 AlertPortfolio 엔티티 → AlertPortfolioDto 변환 메서드
     */
    private AlertPortfolioDto convertToDto(AlertPortfolio alertPortfolio) {
        log.debug("🔍 [convertToDto] 변환 중 - 알림 ID: {}", alertPortfolio.getId());
        return AlertPortfolioDto.builder()
                .id(alertPortfolio.getId())
                .portfolioId(alertPortfolio.getPortfolio().getId()) // Portfolio ID만 저장
                .status(alertPortfolio.getStatus().name()) // Enum → String 변환
                .content(alertPortfolio.getContent())
                .createdAt(alertPortfolio.getCreatedAt()) // ✅ Hibernate가 자동 관리
                .isRead(alertPortfolio.isRead())
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
