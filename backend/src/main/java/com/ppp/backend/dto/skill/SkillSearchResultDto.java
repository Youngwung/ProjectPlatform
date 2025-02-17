package com.ppp.backend.dto.skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillSearchResultDto {
	private Long id;
	private String skill;
	private String skillCategoryName;	
}
