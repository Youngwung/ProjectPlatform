package com.ppp.backend.repository.bookmark;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.bookmark.BookmarkProject;

public interface BookmarkProjectRepository extends JpaRepository<BookmarkProject, Long>, BookmarkProjectSearch {
	Optional<BookmarkProject> findByProjectIdAndUserId(Long projectId, Long userId);
	List<BookmarkProject> findByUserId(Long userId);
}
