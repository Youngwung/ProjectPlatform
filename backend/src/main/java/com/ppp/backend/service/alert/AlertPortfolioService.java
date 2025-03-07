package com.ppp.backend.service.alert;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppp.backend.domain.alert.AlertPortfolio;
import com.ppp.backend.dto.alert.AlertPortfolioDto;
import com.ppp.backend.repository.PortfolioRepository;
import com.ppp.backend.repository.alert.AlertPortfolioRepository;
import com.ppp.backend.util.AuthUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertPortfolioService {

    // ==================== 필드 ====================
    private final AlertPortfolioRepository alertPortfolioRepository;
    private final PortfolioRepository portfolioRepository;
    private final AuthUtil authUtil;

    // ==================== 조회 관련 메서드 ====================

    /**
     * 유저의 모든 포트폴리오 알림을 조회하여 AlertPortfolioDto 리스트로 반환합니다.
     *
     * @param request HTTP 요청 객체로, JWT 쿠키에서 사용자 ID를 추출합니다.
     * @return 해당 사용자의 모든 포트폴리오 알림을 담은 List&lt;AlertPortfolioDto&gt; 반환
     */
    public List<AlertPortfolioDto> getUserPortfolioAlerts(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [getUserPortfolioAlerts] 유저 ID: {}", userId);

        List<AlertPortfolioDto> alertDtos = alertPortfolioRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        log.info("✅ [getUserPortfolioAlerts] 조회된 알림 개수: {}", alertDtos.size());
        return alertDtos;
    }

    /**
     * 유저의 읽지 않은 포트폴리오 알림을 조회하여 AlertPortfolioDto 리스트로 반환합니다.
     *
     * @param request HTTP 요청 객체로, JWT 쿠키에서 사용자 ID를 추출합니다.
     * @return 읽지 않은 알림만을 포함한 List&lt;AlertPortfolioDto&gt; 반환
     */
    public List<AlertPortfolioDto> getUnreadPortfolioAlerts(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [getUnreadPortfolioAlerts] 유저 ID: {}", userId);

        List<AlertPortfolioDto> alertDtos = alertPortfolioRepository.findByUserIdAndIsRead(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        log.info("✅ [getUnreadPortfolioAlerts] 조회된 읽지 않은 알림 개수: {}", alertDtos.size());
        return alertDtos;
    }

    // ==================== 생성/수정/삭제 관련 메서드 ====================

    /**
     * 새로운 포트폴리오 알림을 생성하여 저장하고, 저장된 알림을 AlertPortfolioDto로 반환합니다.
     *
     * @param alertPortfolioDto 생성할 알림의 정보를 담은 DTO 객체
     * @return 저장된 알림을 변환한 AlertPortfolioDto 반환
     * @throws EntityNotFoundException 해당 포트폴리오가 존재하지 않을 경우 예외 발생
     */
    public AlertPortfolioDto createPortfolioAlert(AlertPortfolioDto alertPortfolioDto) {
        log.info("✅ [createPortfolioAlert] 요청 데이터: {}", alertPortfolioDto);

        // Portfolio 엔티티 조회
        var portfolio = portfolioRepository.findById(alertPortfolioDto.getPortfolioId())
                .orElseThrow(() -> {
                    log.error("❌ [createPortfolioAlert] 포트폴리오 ID {} 찾을 수 없음", alertPortfolioDto.getPortfolioId());
                    return new EntityNotFoundException("해당 포트폴리오를 찾을 수 없습니다. ID: " + alertPortfolioDto.getPortfolioId());
                });

        // DTO → 엔티티 변환 (생성 시간은 Hibernate가 자동 관리)
        AlertPortfolio alertPortfolio = AlertPortfolio.builder()
                .portfolio(portfolio)
                .status(AlertPortfolio.Status.valueOf(alertPortfolioDto.getStatus()))
                .content(alertPortfolioDto.getContent())
                .isRead(false) // 새 알림은 기본적으로 읽지 않은 상태
                .build();

        // 엔티티 저장 후 DTO 반환
        AlertPortfolio savedAlert = alertPortfolioRepository.save(alertPortfolio);
        log.info("✅ [createPortfolioAlert] 알림 저장 완료 - ID: {}", savedAlert.getId());

        return convertToDto(savedAlert);
    }

    /**
     * 요청한 사용자의 모든 포트폴리오 알림을 읽음 처리합니다.
     *
     * @param request HTTP 요청 객체로, JWT 쿠키에서 사용자 ID를 추출합니다.
     */
    public void markAllPortfolioAlertsAsRead(HttpServletRequest request) {
        Long userId = extractUserIdOrThrow(request);
        log.info("✅ [markAllPortfolioAlertsAsRead] 유저 ID {}의 모든 알림 읽음 처리 요청", userId);

        int updatedCount = alertPortfolioRepository.markAllAsReadByUserId(userId);
        log.info("✅ [markAllPortfolioAlertsAsRead] 총 {}개의 알림 읽음 처리 완료", updatedCount);
    }

    /**
     * 특정 포트폴리오 알림을 읽음 처리합니다.
     *
     * @param alertId 읽음 처리할 알림의 고유 ID
     * @throws EntityNotFoundException 해당 알림이 존재하지 않을 경우 예외 발생
     */
    @Transactional
    public void markPortfolioAlertAsRead(Long alertId) {
        log.info("✅ [markPortfolioAlertAsRead] 알림 ID: {}", alertId);

        AlertPortfolio alert = alertPortfolioRepository.findById(alertId)
                .orElseThrow(() -> {
                    log.error("❌ [markPortfolioAlertAsRead] 알림 ID {} 찾을 수 없음", alertId);
                    return new EntityNotFoundException("해당 알림을 찾을 수 없습니다. ID: " + alertId);
                });

        alert.markAsRead(); // 변경 감지(Dirty Checking)로 자동 저장됨
        log.info("✅ [markPortfolioAlertAsRead] 알림 ID {} 읽음 처리 완료", alertId);
    }

    /**
     * 특정 포트폴리오 알림을 삭제합니다.
     *
     * @param alertId 삭제할 알림의 고유 ID
     * @throws EntityNotFoundException 해당 알림이 존재하지 않을 경우 예외 발생
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

    // ==================== Private Helper 메서드 ====================

    /**
     * AlertPortfolio 엔티티를 AlertPortfolioDto로 변환합니다.
     *
     * @param alertPortfolio 변환할 AlertPortfolio 엔티티
     * @return 변환된 AlertPortfolioDto 객체 반환
     */
    private AlertPortfolioDto convertToDto(AlertPortfolio alertPortfolio) {
        log.debug("🔍 [convertToDto] 변환 중 - 알림 ID: {}", alertPortfolio.getId());
        return AlertPortfolioDto.builder()
                .id(alertPortfolio.getId())
                .portfolioId(alertPortfolio.getPortfolio().getId())
                .status(alertPortfolio.getStatus().name())
                .content(alertPortfolio.getContent())
                .createdAt(alertPortfolio.getCreatedAt())
                .isRead(alertPortfolio.isRead())
                .build();
    }

    /**
     * HTTP 요청의 쿠키에서 사용자 ID를 추출합니다.
     * 인증 실패 시 예외를 발생시킵니다.
     *
     * @param request HTTP 요청 객체
     * @return 추출된 사용자 ID 반환
     * @throws IllegalStateException 인증 실패 시 예외 발생
     */
    private Long extractUserIdOrThrow(HttpServletRequest request) {
        Long userId = authUtil.extractUserIdFromCookie(request);
        if (userId == null) {
            log.warn("🚨 [extractUserIdOrThrow] 유저 인증 실패 - 쿠키 없음");
            throw new IllegalStateException("유저 인증에 실패했습니다.");
        }
        log.info("✅ [extractUserIdOrThrow] 유저 인증 성공 - ID: {}", userId);
        return userId;
    }
}
