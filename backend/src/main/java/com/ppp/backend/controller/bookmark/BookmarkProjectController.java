package com.ppp.backend.controller.bookmark;

import java.util.Map;

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




@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/bookmark/project")
public class BookmarkProjectController {
	private final BookmarkProjectService bookmarkProjectService;

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
	

	@PostMapping("/")
	public Map<String, Long> register(@RequestBody BookmarkProjectDto dto) {
		log.info("register() dto = ", dto);
		Long bookmarkProjectId = bookmarkProjectService.create(dto);
		return Map.of("bookmarkProjectId", bookmarkProjectId);
	}

	@DeleteMapping("/{id}")
	public Map<String, String> remove(@PathVariable("id") Long id) {
		Long result = bookmarkProjectService.delete(id);
		log.info("remove() delete = {}", result);
		return Map.of("RESULT", "SUCCESS");
	}
	
	
}
