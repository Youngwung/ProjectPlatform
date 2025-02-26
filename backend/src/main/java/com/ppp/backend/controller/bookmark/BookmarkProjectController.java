package com.ppp.backend.controller.bookmark;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.dto.bookmark.BookmarkProjectDto;
import com.ppp.backend.service.bookmark.BookmarkProjectService;
import com.ppp.backend.util.AuthUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * BookmarkProjectController는 프로젝트 북마크 관련 API를 제공함.
 *
 * 주요 기능:
 * - 프로젝트 북마크 단건 조회
 * - 프로젝트 북마크 목록 조회(페이징 처리 포함)
 * - 사용자의 프로젝트 북마크 목록 조회
 * - 프로젝트 북마크 등록
 * - 프로젝트 북마크 삭제
 * - 특정 프로젝트의 북마크 여부 체크
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/bookmark/project")
public class BookmarkProjectController {

	private final BookmarkProjectService bookmarkProjectService;
	private final AuthUtil authUtil;

	/**
	 * 프로젝트 북마크 단건 조회 API.
	 *
	 * @param id 조회할 북마크의 고유 ID 전달함
	 * @return 조회된 BookmarkProjectDto 반환함
	 */
	@GetMapping("/{id}")
	public BookmarkProjectDto get(@PathVariable("id") Long id) {
		BookmarkProjectDto dto = bookmarkProjectService.get(id);
		log.info("get() dto = {}", dto);
		return dto;
	}

	/**
	 * 프로젝트 북마크 목록 조회 API (페이징 처리 포함).
	 *
	 * @return PageResponseDTO&lt;BookmarkProjectDto&gt; 반환함
	 */
	@GetMapping("/list")
	public PageResponseDTO<BookmarkProjectDto> getList() {
		PageResponseDTO<BookmarkProjectDto> list = bookmarkProjectService.getList(
				PageRequestDTO.builder().size(4).build());
		return list;
	}

	/**
	 * 사용자별 프로젝트 북마크 목록 조회 API.
	 *
	 * @param request HTTP 요청 객체 전달함 (쿠키에서 userId 추출)
	 * @return 사용자 북마크 목록(List&lt;BookmarkProjectDto&gt;)을 ResponseEntity로 반환함
	 */
	@GetMapping("/user/list")
	public ResponseEntity<?> userBookmarkProjectsList(HttpServletRequest request) {
		Long userId = authUtil.extractUserIdFromCookie(request);
		if (userId == null) {
			log.error("❌ 쿠키에서 userId를 가져오지 못했음");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요함");
		}
		log.info("userBookmarkProjectsList() userId = {}", userId);

		try {
			List<BookmarkProjectDto> bookmarkList = bookmarkProjectService.getUserBookmarkList(userId);
			log.info("📌 bookmarkList = {}", bookmarkList);
			return ResponseEntity.ok(bookmarkList);
		} catch (ResponseStatusException e) {
			log.warn("⚠️ 북마크 리스트 없음: {}", e.getReason());
			return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
		} catch (Exception e) {
			log.error("❌ 북마크 리스트 불러오는 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("리스트 불러오는 중 오류가 발생함");
		}
	}

	/**
	 * 프로젝트 북마크 등록 API.
	 *
	 * @param request HTTP 요청 객체 전달함 (쿠키에서 userId 추출)
	 * @param dto     등록할 프로젝트 북마크 정보를 담은 BookmarkProjectDto 전달함
	 * @return "bookmarkProjectId" 키에 등록된 북마크 ID가 담긴 Map 반환함
	 */
	@PostMapping("/")
	public Map<String, Long> register(HttpServletRequest request, @RequestBody BookmarkProjectDto dto) {
		// 요청 헤더에서 userId 추출함
		Long userId = authUtil.extractUserIdFromCookie(request);
		if (userId == null) {
			log.error("❌ 쿠키에서 userId를 가져오지 못했음");
			return Map.of("ERROR", -1L);
		}
		dto.setUserId(userId);
		log.info("register() dto = {}", dto);
		Long bookmarkProjectId = bookmarkProjectService.create(dto);
		return Map.of("bookmarkProjectId", bookmarkProjectId);
	}

	/**
	 * 프로젝트 북마크 삭제 API.
	 *
	 * @param id      삭제할 북마크의 고유 ID 전달함 (PathVariable)
	 * @param request HTTP 요청 객체 전달함 (쿠키에서 userId 추출)
	 * @return 삭제 결과를 담은 Map을 ResponseEntity로 반환함
	 *         (성공 시 "RESULT": "SUCCESS", 실패 시 에러 메시지 포함)
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> remove(@PathVariable("id") Long id, HttpServletRequest request) {
		// 요청 헤더에서 userId 추출함
		Long userId = authUtil.extractUserIdFromCookie(request);
		if (userId == null) {
			log.error("❌ 쿠키에서 userId를 가져오지 못했음");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("ERROR", "로그인이 필요함"));
		}
		log.info("remove() - userId: {}, bookmarkId: {}", userId, id);

		try {
			// 북마크 삭제 서비스 호출함
			boolean isDeleted = bookmarkProjectService.deleteByUser(id, userId);
			if (isDeleted) {
				log.info("✅ 북마크 삭제 성공 - userId: {}, bookmarkId: {}", userId, id);
				return ResponseEntity.ok(Map.of("RESULT", "SUCCESS"));
			} else {
				log.warn("⛔ 삭제 권한 없음 - userId: {}, bookmarkId: {}", userId, id);
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body(Map.of("ERROR", "해당 북마크를 삭제할 권한이 없음"));
			}
		} catch (Exception e) {
			log.error("❌ 북마크 삭제 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("ERROR", "삭제 중 오류가 발생함"));
		}
	}

	/**
	 * 특정 프로젝트의 북마크 여부를 체크하는 API.
	 *
	 * @param request   HTTP 요청 객체 전달함 (쿠키에서 userId 추출)
	 * @param projectId 체크할 프로젝트의 고유 ID 전달함 (RequestParam)
	 * @return 체크 결과로 얻은 Long 값을 ResponseEntity로 반환함
	 */
	@PostMapping("/check")
	public ResponseEntity<Long> checkPortfolioBookmark(HttpServletRequest request,
													   @RequestParam(name = "projectId") Long projectId) {
		// 요청 헤더에서 userId 추출함
		Long userId = authUtil.extractUserIdFromCookie(request);
		if (userId == null) {
			log.error("❌ 쿠키에서 userId를 가져오지 못했음");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(-1L);
		}
		BookmarkProjectDto dto = BookmarkProjectDto.builder()
				.projectId(projectId)
				.userId(userId)
				.build();
		Long result = bookmarkProjectService.checkBookmark(dto);
		return ResponseEntity.ok(result);
	}
}
