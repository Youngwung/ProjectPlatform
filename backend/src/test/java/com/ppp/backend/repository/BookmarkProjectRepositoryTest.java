package com.ppp.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.ppp.backend.domain.Project;
import com.ppp.backend.domain.User;
import com.ppp.backend.domain.bookmark.BookmarkProject;
import com.ppp.backend.repository.bookmark.BookmarkProjectRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

// @SpringBootTest
@ActiveProfiles("local")
@Slf4j
public class BookmarkProjectRepositoryTest {
	
	@Autowired
	private BookmarkProjectRepository bookmarkProjectRepo;
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ProjectRepository projectRepo;
	
	// @Test
	public void diTest() {
		log.info("bookmarkProjectRepo = {}", bookmarkProjectRepo);
	}

	// @Test
	public void insertTest() {
		User user = userRepo.findById(1L).orElseThrow();
		Project project = projectRepo.findById(44L).orElseThrow();

		BookmarkProject bookmarkProject = BookmarkProject.builder()
			.project(project)
			.user(user)
		.build();

		BookmarkProject result = bookmarkProjectRepo.save(bookmarkProject);

		log.info("result = {}", result);
	}

	// @Test
	@Transactional
	public void readTest() {
		BookmarkProject bookmarkProject = bookmarkProjectRepo.findById(1L).orElseThrow();
		log.info("bookmarkProject = {}", bookmarkProject);
	}

	// @Test
	public void deleteTest() {
		bookmarkProjectRepo.deleteById(1L);
	}
}
