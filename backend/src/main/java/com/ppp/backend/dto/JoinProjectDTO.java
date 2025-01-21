package com.ppp.backend.dto;

import java.time.LocalDateTime;

import com.ppp.backend.status.JoinProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinProjectDTO {
	private Long id;

	private Long userId;

	private String title;

	private String description;

	private int maxPeople;

	// enum 타입 컬럼 저장
	private JoinProjectStatus status;

	private boolean isPublic;

	@ToString.Exclude
	private LocalDateTime createdAt;

	@ToString.Exclude
	private LocalDateTime updatedAt;

}
