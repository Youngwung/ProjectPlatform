package com.ppp.backend.dto.bookmark;

import com.ppp.backend.domain.bookmark.BookmarkPortfolio;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkPortfolioDto extends BaseBookmarkDto{
	private Long portfolioId;
	private String portfolioTitle;
	public BookmarkPortfolioDto(Long id, Long userId, LocalDateTime createdAt, LocalDateTime updatedAt,
								 Long portfolioId, String portfolioTitle) {
		super(id, userId, createdAt, updatedAt);
		this.portfolioId = portfolioId;
		this.portfolioTitle = portfolioTitle;
	}
}
