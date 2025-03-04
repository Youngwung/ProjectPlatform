package com.ppp.backend.repository.bookmark;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.bookmark.BookmarkProject;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkProjectRepository extends JpaRepository<BookmarkProject, Long>, BookmarkProjectSearch {
	Optional<BookmarkProject> findByProjectIdAndUserId(Long projectId, Long userId);
	List<BookmarkProject> findByUserId(Long userId);

	@Modifying
	@Transactional
	@Query("DELETE FROM BookmarkProject b WHERE b.user.id = :userId")
	void deleteByUserId(@Param("userId") Long userId);
}
