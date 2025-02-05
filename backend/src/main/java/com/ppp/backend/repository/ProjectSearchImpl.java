package com.ppp.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.ppp.backend.domain.Project;
import com.ppp.backend.domain.QProject;
import com.ppp.backend.dto.PageRequestDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ProjectSearchImpl extends QuerydslRepositorySupport implements ProjectSearch {

	// 생성자와 오버라이드 메서드가 필요함.

	// 생성자는 자동생성해주는 거 쓰지 말고
	// 도메인 클래스를 명시적으로 아규먼트로 전달해주는 게 좋음.

	public ProjectSearchImpl() {
		super(Project.class);
	}

	@Override
	public Page<Project> searchString(PageRequestDTO pageRequestDTO) {

		QProject project = QProject.project;

		BooleanExpression isPublic = project.isPublic.eq(true);

		// JoinProject 테이블에서
		JPQLQuery<Project> query = from(project);

		// 공개 변수가 true인 조건 설정
		query.where(isPublic);

		// pageable 객체 생성
		// DTO의 값으로 수정
		Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), Sort.by("id").descending());

		// QueryDSL에서 페이징 처리 하는 방법
		this.getQuerydsl().applyPagination(pageable, query);
		// query에 페이징 처리가 적용됨.

		List<Project> list = query.fetch(); // 페이징 처리된 목록 데이터

		long total = query.fetchCount(); // 검색된 전체 데이터 양
		
		// Collect와 Pageable객체, 전체 데이터 개수로 Page<E>를 리턴해주는 생성자: PageImpl
		return new PageImpl<>(list, pageable, total);
	}

}
