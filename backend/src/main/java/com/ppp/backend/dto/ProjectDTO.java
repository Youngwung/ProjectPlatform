package com.ppp.backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
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

	// 클라이언트에서 전달받은 정규 표현식 문자열을 저장하는 컬럼.
	private String skills;
}
