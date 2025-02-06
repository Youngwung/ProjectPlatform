package com.ppp.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.dto.bookmark.BookmarkProjectDto;
import com.ppp.backend.service.bookmark.BookmarkProjectService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class BookmarkProjectServiceTest {
	@Autowired
	private BookmarkProjectService bookmarkProjectService;

	// @Test
	public void diTest() {
		log.info("bookmarkProjectService = {}", bookmarkProjectService);
	}

	// @Test
	public void createTest() {
		BookmarkProjectDto dto = BookmarkProjectDto.builder()
			.projectId(44L)
			.userId(1L)
		.build();

		Long result = bookmarkProjectService.create(dto);
		log.info("dto = {}", dto);
		log.info("result = {}", result);
		// 3번 생성
	}

	// @Test
	public void readTest() {
		BookmarkProjectDto dto = bookmarkProjectService.get(3L);
		log.info("dto = {}", dto);
	}

	// @Test
	public void deleteTest() {
		Long result = bookmarkProjectService.delete(3L);
		log.info("result = {}", result);
	}

	// @Test
	// @Transactional
	public void getListTest() {
		PageResponseDTO<BookmarkProjectDto> list = bookmarkProjectService.getList(PageRequestDTO.builder().build());
		System.out.println(list);
	}
}
