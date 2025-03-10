package com.ppp.backend.repository.bookmark;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.ppp.backend.domain.bookmark.BookmarkPortfolio;
import com.ppp.backend.domain.bookmark.QBookmarkPortfolio;
import com.ppp.backend.dto.PageRequestDTO;
import com.querydsl.jpa.JPQLQuery;

public class BookmarkPortfolioSearchImpl extends QuerydslRepositorySupport implements	BookmarkPortfolioSearch {

	public BookmarkPortfolioSearchImpl() {
		super(BookmarkPortfolio.class);
	}

	@Override
	public Page<BookmarkPortfolio> bookmarkPortfolioSearch(PageRequestDTO pageRequestDTO) {
		QBookmarkPortfolio bookmarkPortfolio = QBookmarkPortfolio.bookmarkPortfolio;

		JPQLQuery<BookmarkPortfolio> query = from(bookmarkPortfolio);

		Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), Sort.by("id").descending());

		this.getQuerydsl().applyPagination(pageable, query);

		List<BookmarkPortfolio> list = query.fetch();

		long total = query.fetchCount();

		return new PageImpl<>(list, pageable, total);
	}
	
}
