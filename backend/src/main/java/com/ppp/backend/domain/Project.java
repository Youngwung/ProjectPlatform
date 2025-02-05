package com.ppp.backend.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ppp.backend.status.ProjectStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "PROJECT")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private String title;

	private String description;

	private int maxPeople;

	// enum 타입 컬럼 저장
	@Enumerated(EnumType.STRING)
	private ProjectStatus status;

	@Column(insertable = false)
	private boolean isPublic;

	@ToString.Exclude
	@CreationTimestamp
	// LocalDateTime, TimeStamp 등에서 사용 가능한 편의 애너테이션
	@Column(updatable = false)
	// 최초 생성 시 default값, 그 이후에 수정 불가
	private Timestamp createdAt;

	@ToString.Exclude
	@UpdateTimestamp
	private Timestamp updatedAt;

	public void update (String title, String description, int maxPeople, String status, boolean isPublic) {
		this.title = title;
		this.description = description;
		this.maxPeople = maxPeople;
		this.status = ProjectStatus.valueOf(status);
		this.isPublic = isPublic;
		this.updatedAt = Timestamp.valueOf(LocalDateTime.now());
	}
}
