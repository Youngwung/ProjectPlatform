package com.ppp.backend.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ppp.backend.status.JoinProjectStatus;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "JOINPROJECT")
@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class JoinProject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable=false)
	private User user;
	
	@Column(nullable = false)
	private String title;
	
	private String description;

	private int maxPeople;

	// enum 타입 컬럼 저장
	@Enumerated(EnumType.STRING)
	// JPA가 insert문 생성 시 컬럼에서 제외하여 default값이 들어가도록 설정하는 애너테이션
	@Column(insertable = false)
	private JoinProjectStatus status;

	@Column(insertable = false)
	private boolean isPublic;

	@ToString.Exclude
	@CreationTimestamp
	// LocalDateTime, TimeStamp 등에서 사용 가능한 편의 애너테이션
	@Column(updatable = false)
	// 최초 생성 시 default값, 그 이후에 수정 불가
	private LocalDateTime createdAt;

	@ToString.Exclude
	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
