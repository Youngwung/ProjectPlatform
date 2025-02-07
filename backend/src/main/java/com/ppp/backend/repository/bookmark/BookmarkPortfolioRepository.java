package com.ppp.backend.repository.bookmark;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.bookmark.BookmarkPortfolio;

public interface BookmarkPortfolioRepository extends JpaRepository<BookmarkPortfolio, Long>, BookmarkPortfolioSearch{
	Optional<BookmarkPortfolio> findByPortfolioIdAndUserId(Long portfolioId, Long userId);
}
