package com.ppp.backend.repository;

import org.springframework.data.domain.Page;

import com.ppp.backend.domain.Portfolio;
import com.ppp.backend.dto.PageRequestDTO;

public interface PortfolioSearch {
	Page<Portfolio> searchString(PageRequestDTO pageRequestDTO);

	Page<Portfolio> searchKeyword(PageRequestDTO pageRequestDTO);
}
