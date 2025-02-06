package com.ppp.backend.repository.bookmark;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.ppp.backend.domain.bookmark.BookmarkProject;
import com.ppp.backend.domain.bookmark.QBookmarkProject;
import com.ppp.backend.dto.PageRequestDTO;
import com.querydsl.jpa.JPQLQuery;

public class BookmarkProjectSearchImpl extends QuerydslRepositorySupport implements BookmarkProjectSearch{

	public BookmarkProjectSearchImpl() {
		super(BookmarkProject.class);
	}

	@Override
	public Page<BookmarkProject> bookmarkProjectSearch(PageRequestDTO pageRequestDTO) {
		QBookmarkProject bookmarkProject = QBookmarkProject.bookmarkProject;

		JPQLQuery<BookmarkProject> query = from(bookmarkProject);

		Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), Sort.by("id").descending());

		this.getQuerydsl().applyPagination(pageable, query);

		List<BookmarkProject> list = query.fetch();

		long total = query.fetchCount();

		return new PageImpl<>(list, pageable, total);
	}
}
