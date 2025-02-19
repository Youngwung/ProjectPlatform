package com.ppp.backend.repository.bookmark;

import java.util.List;
import java.util.Optional;

import com.ppp.backend.domain.bookmark.BookmarkProject;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.bookmark.BookmarkPortfolio;

public interface BookmarkPortfolioRepository extends JpaRepository<BookmarkPortfolio, Long>, BookmarkPortfolioSearch{
	Optional<BookmarkPortfolio> findByPortfolioIdAndUserId(Long portfolioId, Long userId);
	List<BookmarkPortfolio> findByUserId(Long userId);

}
