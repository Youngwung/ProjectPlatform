package com.ppp.backend.service.bookmark;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ppp.backend.domain.Project;
import com.ppp.backend.domain.User;
import com.ppp.backend.domain.bookmark.BookmarkProject;
import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.dto.bookmark.BookmarkProjectDto;
import com.ppp.backend.repository.ProjectRepository;
import com.ppp.backend.repository.UserRepository;
import com.ppp.backend.repository.bookmark.BookmarkProjectRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BookmarkProjectService {
	private final BookmarkProjectRepository bookmarkProjectRepo;
	private final UserRepository userRepo;
	private final ProjectRepository projectRepo;

	public Long create(BookmarkProjectDto dto) {
		BookmarkProject entity = toEntity(dto);
		BookmarkProject result = bookmarkProjectRepo.save(entity);
		
		return result.getId();
	}

	public BookmarkProjectDto get(Long id) {
		BookmarkProject result = bookmarkProjectRepo.findById(id).orElseThrow();
		BookmarkProjectDto dto = fromEntity(result);
		return dto;
	}

	public PageResponseDTO<BookmarkProjectDto> getList(PageRequestDTO pageRequestDTO) {
		Page<BookmarkProject> result = bookmarkProjectRepo.bookmarkProjectSearch(pageRequestDTO);
		List<BookmarkProjectDto> dtoList = result.get().map(bookmarkProject -> {
			BookmarkProjectDto dto = fromEntity(bookmarkProject);
			return dto;
		}).collect(Collectors.toList());

		PageResponseDTO<BookmarkProjectDto> pageResponseDTO = new PageResponseDTO<>(dtoList, pageRequestDTO, result.getTotalElements());

		return pageResponseDTO;
	}

	public Long delete(Long id) {
		BookmarkProject result = bookmarkProjectRepo.findById(id).orElseThrow();
		bookmarkProjectRepo.deleteById(id);

		// 삭제된 아이디 리턴
		return result.getId();
	}

	private BookmarkProject toEntity(BookmarkProjectDto dto) {
		User user = userRepo.findById(dto.getUserId()).orElseThrow();
		Project project = projectRepo.findById(dto.getProjectId()).orElseThrow();

		return BookmarkProject.builder()
			.user(user)
			.project(project)
			.createdAt(Timestamp.valueOf(LocalDateTime.now()))
			.updatedAt(Timestamp.valueOf(LocalDateTime.now()))
		.build();
	}

	private BookmarkProjectDto fromEntity(BookmarkProject entity){

		return BookmarkProjectDto.builder()
			.id(entity.getId())
			.projectId(entity.getProject().getId())
			.userId(entity.getUser().getId())
			.createAt(entity.getCreatedAt().toLocalDateTime())
			.updatedAt(entity.getUpdatedAt().toLocalDateTime())
		.build();
	}
}
