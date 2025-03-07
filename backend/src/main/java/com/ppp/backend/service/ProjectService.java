package com.ppp.backend.service;

import java.util.List;

import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.dto.ProjectDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface ProjectService {

	ProjectDTO get(Long jPno);

	Long register(ProjectDTO dto, Long userId);

	void modify(ProjectDTO dto);

	void remove(Long jpNo);

	PageResponseDTO<ProjectDTO> getList(PageRequestDTO pageRequestDTO);

	PageResponseDTO<ProjectDTO> getSearchResult(PageRequestDTO pageRequestDTO);

	List<ProjectDTO> getMyProjects(HttpServletRequest request);

	boolean checkWriter(Long userId, Long projectId);
}
