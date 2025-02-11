package com.ppp.backend.service;

import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.dto.ProjectDTO;

public interface ProjectService {

	ProjectDTO get(Long jPno);

	Long register(ProjectDTO dto);

	void modify(ProjectDTO dto);

	void remove(Long jpNo);

	PageResponseDTO<ProjectDTO> getList(PageRequestDTO PageRequestDTO);

}
