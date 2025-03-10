package com.ppp.backend.service.bookmark;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ppp.backend.domain.bookmark.BookmarkProject;
import com.ppp.backend.dto.bookmark.BookmarkProjectDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

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

	public Boolean deleteByUser(Long id, Long userId) {
		log.info("deleteByUser() - userId: {}, bookmarkId: {}", userId, id);
		Optional<BookmarkPortfolio> bookmarkOptional = bookmarkPortfolioRepo.findById(id);
		if (bookmarkOptional.isEmpty()) {
			log.warn("⚠️ 삭제할 북마크(ID: {})가 존재하지 않습니다.", id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제할 북마크가 존재하지 않습니다.");
		}
		BookmarkPortfolio bookmark = bookmarkOptional.get();
		// ✅ BookmarkPortfolio 엔티티에 getUserId() 메서드가 있는지 확인 후 추가 필요
		if (!bookmark.getUser().getId().equals(userId)) {
			log.warn("⛔ 삭제 권한 없음 - userId: {}, bookmarkUserId: {}", userId, bookmark.getUser().getId());
			return false; // 403 Forbidden 응답을 위해 false 반환
		}
		bookmarkPortfolioRepo.deleteById(id);
		log.info("✅ 북마크 삭제 완료 - bookmarkId: {}", id);
		return true;
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

	public List<BookmarkPortfolioDto> getUserBookmarkList(Long userId) {
		List<BookmarkPortfolio> bookmarkPortfolios = bookmarkPortfolioRepo.findByUserId(userId);

		if (bookmarkPortfolios.isEmpty()) {
			log.warn("⚠️ 북마크된 포트폴리오가 없습니다. userId={}", userId);
			return List.of(); // 빈 리스트 반환
		}

		return bookmarkPortfolios.stream()
				.map(bookmark -> {
					// ✅ Portfolio가 없는 경우 기본값 반환
					if (bookmark.getPortfolio() == null) {
						log.warn("⚠️ Portfolio 정보 없음 - bookmarkId: {}", bookmark.getId());
						return new BookmarkPortfolioDto(
								bookmark.getId(),                     // 북마크 ID
								bookmark.getUser().getId(),           // 사용자 ID
								bookmark.getCreatedAt().toLocalDateTime(), // 생성일
								bookmark.getUpdatedAt().toLocalDateTime(), // 수정일
								null,                                 // 프로젝트 ID (없음)
								"삭제된 포트폴리오"                      // 기본 제목 설정
						);
					}

					return new BookmarkPortfolioDto(
							bookmark.getId(),                     // 북마크 ID
							bookmark.getUser().getId(),           // 사용자 ID
							bookmark.getCreatedAt().toLocalDateTime(), // 생성일
							bookmark.getUpdatedAt().toLocalDateTime(), // 수정일
							bookmark.getPortfolio().getId(),      // 프로젝트 ID
							bookmark.getPortfolio().getTitle()    // 프로젝트 제목
					);
				})
				.collect(Collectors.toList());
	}

}
