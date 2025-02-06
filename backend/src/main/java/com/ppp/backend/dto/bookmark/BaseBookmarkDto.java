package com.ppp.backend.dto.bookmark;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseBookmarkDto {
	private Long id;

	private Long userId;

	private LocalDateTime createAt;

	private LocalDateTime updatedAt;
}
