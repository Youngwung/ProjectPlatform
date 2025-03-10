package com.ppp.backend.repository.bookmark;

import org.springframework.data.domain.Page;

import com.ppp.backend.domain.bookmark.BookmarkPortfolio;
import com.ppp.backend.dto.PageRequestDTO;

public interface BookmarkPortfolioSearch {
	Page<BookmarkPortfolio> bookmarkPortfolioSearch(PageRequestDTO pageRequestDTO);
}
