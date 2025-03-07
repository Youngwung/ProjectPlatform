package com.ppp.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
	@Builder.Default
	private int page = 1;

	@Builder.Default
	private int size = 12;

	private String query;

	private List<String> querySkills;

	@Builder.Default
	private String type = "all";

	@Builder.Default
	// 관련도순: relevance, 인기순: popularity
	private String sortOption = "relevance";

}
