package com.ppp.backend.service.bookmark;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

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
		return null;
	}

	/**
	 * 
	 * @param dto
	 * @return 북마크가 되어있으면 북마크 아이디, 북마크가 안되어있으면 -1L 리턴
	 */
	public Long checkBookmark(BookmarkProjectDto dto) {
		BookmarkProject result = bookmarkProjectRepo.findByProjectIdAndUserId(dto.getProjectId(), dto.getUserId()).orElse(null);

		if (result == null) {
			// 북마크가 안되어있는 경우
			return null;
		}
		// 북마크가 되어있는 경우
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
			.createdAt(entity.getCreatedAt().toLocalDateTime())
			.updatedAt(entity.getUpdatedAt().toLocalDateTime())
		.build();
	}
	public List<BookmarkProjectDto> getUserBookmarkList(Long userId) {
		List<BookmarkProject> bookmarkProjects = bookmarkProjectRepo.findByUserId(userId);

		if (bookmarkProjects.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자의 북마크 프로젝트가 없습니다. userId=" + userId);
		}

		return bookmarkProjects.stream()
				.map(bookmark -> new BookmarkProjectDto(
						bookmark.getId(),                        // 북마크 ID
						bookmark.getUser().getId(),              // 사용자 ID
						bookmark.getCreatedAt().toLocalDateTime(),                 // 생성일
						bookmark.getUpdatedAt().toLocalDateTime(),                  // 수정일
						bookmark.getProject().getId(),           // 프로젝트 ID
						bookmark.getProject().getTitle()        // 프로젝트 제목
				))
				.collect(Collectors.toList());
	}



}
