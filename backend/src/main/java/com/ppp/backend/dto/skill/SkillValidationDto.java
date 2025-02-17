package com.ppp.backend.dto.skill;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillValidationDto {
	private boolean isValid;

	private List<String> wrongString;
}
