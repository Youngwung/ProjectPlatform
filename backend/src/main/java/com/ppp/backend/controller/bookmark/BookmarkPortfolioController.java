package com.ppp.backend.controller.bookmark;

import java.util.List;
import java.util.Map;

import com.ppp.backend.controller.AuthApiController;
import com.ppp.backend.domain.bookmark.BookmarkPortfolio;
import com.ppp.backend.dto.bookmark.BookmarkProjectDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.dto.bookmark.BookmarkPortfolioDto;
import com.ppp.backend.service.bookmark.BookmarkPortfolioService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/bookmark/portfolio")
public class BookmarkPortfolioController {
	private final BookmarkPortfolioService bookmarkPortfolioService;
	private final AuthApiController	authApiController;

	@GetMapping("/{id}")
	public BookmarkPortfolioDto get(@PathVariable("id") Long id) {
		BookmarkPortfolioDto dto = bookmarkPortfolioService.get(id);
		log.info("get() dto = {}", dto);
		return dto;
	}

	@GetMapping("/list")
	public PageResponseDTO<BookmarkPortfolioDto> getList() {
		PageResponseDTO<BookmarkPortfolioDto> list = bookmarkPortfolioService.getList(PageRequestDTO.builder().size(4).build());
		return list;
	}
	@GetMapping("/user/list")
	public ResponseEntity<?> userBookmarkPortfoliosList(HttpServletRequest request) {
		Long userId = authApiController.extractUserIdFromCookie(request);
		if (userId == null) {
			log.error("❌ 쿠키에서 userId를 가져오지 못했습니다.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		}
		log.info("userBookmarkPortfoliosList() userId = {}", userId);

		try {
			List<BookmarkPortfolioDto> bookmarkList = bookmarkPortfolioService.getUserBookmarkList(userId);
			if (bookmarkList.isEmpty()) {
				log.warn("⚠️ 북마크된 포트폴리오가 없습니다.");
				return ResponseEntity.ok(List.of()); // 빈 리스트 반환
			}
			log.info("📌 bookmarkList = {}", bookmarkList);
			return ResponseEntity.ok(bookmarkList);
		} catch (ResponseStatusException e) {
			log.warn("⚠️ 북마크 리스트 없음: {}", e.getReason());
			return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
		} catch (Exception e) {
			log.error("❌ 북마크 리스트 불러오는 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리스트 불러오는 중 오류가 발생하였습니다.");
		}
	}
	@PostMapping("/")
	public Map<String, Long> register(@RequestBody BookmarkPortfolioDto dto) {
		log.info("register() dto = {}", dto);
		Long bookmarkPortfolioId = bookmarkPortfolioService.create(dto);
		return Map.of("bookmarkPortfolioId", bookmarkPortfolioId);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> remove(@PathVariable("id") Long id, HttpServletRequest request) {
		// 요청 헤더에서 userId 추출
		Long userId = authApiController.extractUserIdFromCookie(request);

		if (userId == null) {
			log.error("❌ 쿠키에서 userId를 가져오지 못했습니다.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("ERROR", "로그인이 필요합니다."));
		}

		log.info("remove() - userId: {}, bookmarkId: {}", userId, id);

		try {
			// 북마크 삭제 서비스 호출
			boolean isDeleted = bookmarkPortfolioService.deleteByUser(id, userId);

			if (isDeleted) {
				log.info("✅ 북마크 삭제 성공 - userId: {}, bookmarkId: {}", userId, id);
				return ResponseEntity.ok(Map.of("RESULT", "SUCCESS"));
			} else {
				log.warn("⛔ 삭제 권한 없음 - userId: {}, bookmarkId: {}", userId, id);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("ERROR", "해당 북마크를 삭제할 권한이 없습니다."));
			}
		} catch (Exception e) {
			log.error("❌ 북마크 삭제 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("ERROR", "삭제 중 오류가 발생했습니다."));
		}
	}

	@PostMapping("/check")
	public ResponseEntity<Long> getMethodName(@RequestBody BookmarkPortfolioDto	dto) {
		Long result = bookmarkPortfolioService.checkBookmark(dto);
		return ResponseEntity.ok(result);
	}
	
}
