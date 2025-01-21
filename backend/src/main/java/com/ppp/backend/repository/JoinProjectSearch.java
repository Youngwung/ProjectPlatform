package com.ppp.backend.repository;

import org.springframework.data.domain.Page;

import com.ppp.backend.domain.JoinProject;
import com.ppp.backend.dto.PageRequestDTO;

public interface JoinProjectSearch {
	Page<JoinProject> search1(PageRequestDTO pageRequestDTO);
}