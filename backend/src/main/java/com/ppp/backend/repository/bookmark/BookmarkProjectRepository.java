package com.ppp.backend.repository.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.bookmark.BookmarkProject;

public interface BookmarkProjectRepository extends JpaRepository<BookmarkProject, Long>, BookmarkProjectSearch {
	
}
