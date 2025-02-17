package com.ppp.backend.domain;

import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String email;

	private String phoneNumber;

	private String experience;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "provider_id")
	@ToString.Exclude
	private Provider provider;

	@ToString.Exclude
	@CreationTimestamp
	// 최초 생성 시 디폴트값, 그 이후에 수정 불가
	@Column(updatable = false)
	private Timestamp createdAt;

	@ToString.Exclude
	@UpdateTimestamp
	private Timestamp updatedAt;

	@Transient
	private String links;
}
