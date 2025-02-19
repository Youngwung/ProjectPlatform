package com.ppp.backend.dto.bookmark;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkProjectDto extends BaseBookmarkDto {

	private Long projectId;
	private String projectTitle;

	// 부모 필드까지 초기화하는 생성자 추가
	public BookmarkProjectDto(Long id, Long userId, LocalDateTime createdAt, LocalDateTime updatedAt,
							  Long projectId, String projectTitle) {
		super(id, userId, createdAt, updatedAt);
		this.projectId = projectId;
		this.projectTitle = projectTitle;
	}
}
