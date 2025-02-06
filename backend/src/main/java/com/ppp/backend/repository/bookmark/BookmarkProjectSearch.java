package com.ppp.backend.repository.bookmark;

import org.springframework.data.domain.Page;

import com.ppp.backend.domain.bookmark.BookmarkProject;
import com.ppp.backend.dto.PageRequestDTO;

public interface BookmarkProjectSearch {
	Page<BookmarkProject> bookmarkProjectSearch(PageRequestDTO pageRequestDTO);
}
