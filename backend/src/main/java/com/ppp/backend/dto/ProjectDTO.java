package com.ppp.backend.dto;

import java.time.LocalDateTime;

import com.ppp.backend.dto.skill.BaseSkillDto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO extends BaseSkillDto{
	private Long id;

	private Long userId;

	private String title;

	private String description;

	private int maxPeople;

	// enum 타입 컬럼 저장
	private String status;

	private boolean isPublic;

	@ToString.Exclude
	private LocalDateTime createdAt;

	@ToString.Exclude
	private LocalDateTime updatedAt;

	// 프로젝트 타입을 받아오기 위한 필드
	private String type;
}
