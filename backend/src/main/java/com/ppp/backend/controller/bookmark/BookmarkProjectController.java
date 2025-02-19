package com.ppp.backend.controller.bookmark;

import java.util.List;
import java.util.Map;

import com.ppp.backend.controller.AuthApiController;
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
import com.ppp.backend.dto.bookmark.BookmarkProjectDto;
import com.ppp.backend.service.bookmark.BookmarkProjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/bookmark/project")
public class BookmarkProjectController {
	private final BookmarkProjectService bookmarkProjectService;
	private final AuthApiController authApiController;

	@GetMapping("/{id}")
	public BookmarkProjectDto get(@PathVariable("id") Long id) {
		BookmarkProjectDto dto = bookmarkProjectService.get(id);
		log.info("get() dto = {}", dto);
		return dto;
	}

	@GetMapping("/list")
	public PageResponseDTO<BookmarkProjectDto> getList() {
		PageResponseDTO<BookmarkProjectDto> list = bookmarkProjectService.getList(PageRequestDTO.builder().size(4).build());
		return list;
	}

	@GetMapping("/user/list")
	public ResponseEntity<?> userBookmarkProjectsList(HttpServletRequest request) {
		Long userId = authApiController.extractUserIdFromCookie(request);
		if (userId == null) {
			log.error("❌ 쿠키에서 userId를 가져오지 못했습니다.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리스트 불러오는 중 오류가 발생하였습니다.");
		}
	}




	@PostMapping("/")
	public Map<String, Long> register(@RequestBody BookmarkProjectDto dto) {
		log.info("register() dto = {}", dto);
		Long bookmarkProjectId = bookmarkProjectService.create(dto);
		return Map.of("bookmarkProjectId", bookmarkProjectId);
	}

	@DeleteMapping("/{id}")
	public Map<String, String> remove(@PathVariable("id") Long id) {
		Long result = bookmarkProjectService.delete(id);
		log.info("remove() delete = {}", result);
		return Map.of("RESULT", "SUCCESS");
	}

	@PostMapping("/check")
	public ResponseEntity<Long> getMethodName(@RequestBody BookmarkProjectDto	dto) {
		Long result = bookmarkProjectService.checkBookmark(dto);
		return ResponseEntity.ok(result);
	}
	
}
