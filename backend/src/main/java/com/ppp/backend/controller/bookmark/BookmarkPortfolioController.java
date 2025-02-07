package com.ppp.backend.controller.bookmark;

import java.util.Map;

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





@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/bookmark/portfolio")
public class BookmarkPortfolioController {
	private final BookmarkPortfolioService bookmarkPortfolioService;

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
	

	@PostMapping("/")
	public Map<String, Long> register(@RequestBody BookmarkPortfolioDto dto) {
		log.info("register() dto = {}", dto);
		Long bookmarkPortfolioId = bookmarkPortfolioService.create(dto);
		return Map.of("bookmarkPortfolioId", bookmarkPortfolioId);
	}

	@DeleteMapping("/{id}")
	public Map<String, String> remove(@PathVariable("id") Long id) {
		Long result = bookmarkPortfolioService.delete(id);
		log.info("remove() delete = {}", result);
		return Map.of("RESULT", "SUCCESS");
	}

	@PostMapping("/check")
	public ResponseEntity<Long> getMethodName(@RequestBody BookmarkPortfolioDto	dto) {
		Long result = bookmarkPortfolioService.checkBookmark(dto);
		return ResponseEntity.ok(result);
	}
	
}
