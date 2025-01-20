package com.ppp.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.ppp.backend.domain.JoinProject;
import com.ppp.backend.domain.QJoinProject;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class JoinProjectSearchImpl extends QuerydslRepositorySupport implements JoinProjectSearch {

	// 생성자와 오버라이드 메서드가 필요함.

	// 생성자는 자동생성해주는 거 쓰지 말고
	// 도메인 클래스를 명시적으로 아규먼트로 전달해주는 게 좋음.

	public JoinProjectSearchImpl() {
		super(JoinProject.class);
	}

	@Override
	public Page<JoinProject> search1() {
		log.info("search1............");

		QJoinProject jProject = QJoinProject.joinProject;

		// JoinProject 테이블에서
		JPQLQuery<JoinProject> query = from(jProject);

		// title 컬럼에 1이 포함된 조건으로 select 문 작성
		query.where(jProject.title.toLowerCase().contains("t"));

		// pageable 객체 생성
		Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

		// QueryDSL에서 페이징 처리 하는 방법
		this.getQuerydsl().applyPagination(pageable, query);
		// query에 페이징 처리가 적용됨.

		List<JoinProject> fetch = query.fetch(); // 페이징 처리된 목록 데이터
		System.out.println(fetch);

		long fetchCount = query.fetchCount(); // 검색된 전체 데이터 양
		System.out.println(fetchCount);
		
		return null;
	}

}
