package com.ppp.backend.service.bookmark;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ppp.backend.domain.Portfolio;
import com.ppp.backend.domain.User;
import com.ppp.backend.domain.bookmark.BookmarkPortfolio;
import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.dto.bookmark.BookmarkPortfolioDto;
import com.ppp.backend.repository.PortfolioRepository;
import com.ppp.backend.repository.UserRepository;
import com.ppp.backend.repository.bookmark.BookmarkPortfolioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BookmarkPortfolioService{
	private final BookmarkPortfolioRepository bookmarkPortfolioRepo;
	private final UserRepository userRepo;
	private final PortfolioRepository portfolioRepo;
	
	public Long create(BookmarkPortfolioDto dto) {
		BookmarkPortfolio entity = toEntity(dto);
		BookmarkPortfolio result = bookmarkPortfolioRepo.save(entity);
		
		return result.getId();
	}

	public BookmarkPortfolioDto get(Long id) {
		BookmarkPortfolio result = bookmarkPortfolioRepo.findById(id).orElseThrow();
		BookmarkPortfolioDto dto = fromEntity(result);
		return dto;
	}

	public PageResponseDTO<BookmarkPortfolioDto> getList(PageRequestDTO pageRequestDTO) {
		Page<BookmarkPortfolio> result = bookmarkPortfolioRepo.bookmarkPortfolioSearch(pageRequestDTO);
		List<BookmarkPortfolioDto> dtoList = result.get().map(bookmarkPortfolio -> {
			BookmarkPortfolioDto dto = fromEntity(bookmarkPortfolio);
			return dto;
		}).collect(Collectors.toList());

		PageResponseDTO<BookmarkPortfolioDto> pageResponseDTO = new PageResponseDTO<>(dtoList, pageRequestDTO, result.getTotalElements());

		return pageResponseDTO;
	}

	public Long delete(Long id) {
		BookmarkPortfolio result = bookmarkPortfolioRepo.findById(id).orElseThrow();
		bookmarkPortfolioRepo.deleteById(id);

		// 삭제된 아이디 리턴
		return result.getId();
	}

	/**
	 * 
	 * @param dto
	 * @return 북마크가 되어있으면 북마크 아이디, 북마크가 안되어있으면 -1L 리턴
	 */
	public Long checkBookmark(BookmarkPortfolioDto dto) {
		BookmarkPortfolio result = bookmarkPortfolioRepo.findByPortfolioIdAndUserId(dto.getPortfolioId(), dto.getUserId()).orElse(null);

		if (result == null) {
			// 북마크가 안되어있는 경우
			return null;
		}
		// 북마크가 되어있는 경우
		return result.getId();
	}

	private BookmarkPortfolio toEntity(BookmarkPortfolioDto dto) {
		User user = userRepo.findById(dto.getUserId()).orElseThrow();
		Portfolio portfolio = portfolioRepo.findById(dto.getPortfolioId()).orElseThrow();

		return BookmarkPortfolio.builder()
			.user(user)
			.portfolio(portfolio)
			.createdAt(Timestamp.valueOf(LocalDateTime.now()))
			.updatedAt(Timestamp.valueOf(LocalDateTime.now()))
		.build();
	}

	private BookmarkPortfolioDto fromEntity(BookmarkPortfolio entity){

		return BookmarkPortfolioDto.builder()
			.id(entity.getId())
			.portfolioId(entity.getPortfolio().getId())
			.userId(entity.getUser().getId())
			.createdAt(entity.getCreatedAt().toLocalDateTime())
			.updatedAt(entity.getUpdatedAt().toLocalDateTime())
		.build();
	}

}
