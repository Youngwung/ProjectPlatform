package com.ppp.backend.repository;

import org.springframework.data.domain.Page;

import com.ppp.backend.domain.Project;
import com.ppp.backend.dto.PageRequestDTO;

public interface ProjectSearch {
	Page<Project> searchString(PageRequestDTO pageRequestDTO);
}