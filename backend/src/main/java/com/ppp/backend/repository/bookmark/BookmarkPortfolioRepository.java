package com.ppp.backend.repository.bookmark;

import java.util.List;
import java.util.Optional;

import com.ppp.backend.domain.bookmark.BookmarkProject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.bookmark.BookmarkPortfolio;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkPortfolioRepository extends JpaRepository<BookmarkPortfolio, Long>, BookmarkPortfolioSearch{
	Optional<BookmarkPortfolio> findByPortfolioIdAndUserId(Long portfolioId, Long userId);
	List<BookmarkPortfolio> findByUserId(Long userId);

	@Modifying
	@Transactional
	@Query("DELETE FROM BookmarkPortfolio b WHERE b.user.id = :userId")
	void deleteByUserId(@Param("userId") Long userId);
}
