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
import com.ppp.backend.domain.QProjectSkill;
import com.ppp.backend.domain.QProjectType;
import com.ppp.backend.domain.QSkill;
import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.enums.ProjectTypeEnum;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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
		Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
		Sort.by("id").descending());
		
		// QueryDSL에서 페이징 처리 하는 방법
		this.getQuerydsl().applyPagination(pageable, query);
		// query에 페이징 처리가 적용됨.
		
		List<Project> list = query.fetch(); // 페이징 처리된 목록 데이터
		
		long total = query.fetchCount(); // 검색된 전체 데이터 양
		
		// Collect와 Pageable객체, 전체 데이터 개수로 Page<E>를 리턴해주는 생성자: PageImpl
		return new PageImpl<>(list, pageable, total);
	}
	
	@Override
	public Page<Project> searchKeyword(PageRequestDTO pageRequestDTO) {
		QProject project = QProject.project;
		QProjectType projectType = QProjectType.projectType;
		
		// 공개인 것만 데이터 패칭
		BooleanExpression isPublic = project.isPublic.eq(true);

		// 프로젝트 유형: all. content, skill에 따른 검색 조건 설정
		String requestType = pageRequestDTO.getType();
		
		BooleanExpression type = projectType.type.eq(ProjectTypeEnum.valueOf(requestType));

		// JoinProject 테이블에서
		JPQLQuery<Project> query = from(project);



		// ! 검색 우선순위 로직
		QProjectSkill projectSkill = QProjectSkill.projectSkill;
		QSkill skill = QSkill.skill;

		BooleanExpression titleContains = Expressions.TRUE;
		BooleanExpression descriptionContains = Expressions.TRUE;;
		// 검색 키워드
		if (!pageRequestDTO.getQuery().equals("")) {
			titleContains = project.title.containsIgnoreCase(pageRequestDTO.getQuery());
			descriptionContains = project.description.containsIgnoreCase(pageRequestDTO.getQuery());
		}

		// Project가 가진 스킬 중 querySkills에 해당하는 스킬의 개수를 구함
		NumberExpression<Integer> matchedSkillsCount = new CaseBuilder()
				.when(projectSkill.skill.name.in(pageRequestDTO.getQuerySkills())).then(1)
				.otherwise(0)
				.sum();

		// query 문자열에 대해 제목과 내용이 동시에 포함되면 2점, 둘 중 하나면 1점, 없으면 0점
		NumberExpression<Integer> queryScore = new CaseBuilder()
				.when(titleContains.and(descriptionContains)).then(2)
				.when(titleContains.or(descriptionContains)).then(1)
				.otherwise(0);

		// 스킬의 일치 개수에 따른 우선순위 할당 로직
		CaseBuilder caseBuilder = new CaseBuilder();
		int priorityIndex = 0;
		int totalQuerySkills = pageRequestDTO.getQuerySkills().size();
		for (int i = totalQuerySkills; i >= 0; i--) {
			caseBuilder.when(titleContains.and(descriptionContains).and(matchedSkillsCount.eq(i)))
					.then(priorityIndex++);
			caseBuilder.when(titleContains.and(matchedSkillsCount.eq(i)))
					.then(priorityIndex++);
			caseBuilder.when(descriptionContains.and(matchedSkillsCount.eq(i)))
					.then(priorityIndex++);
		}

		// 위의 조건을 만족하지 못하는 프로젝트는 가장 낮은 우선순위 부여 (높은 숫자)
		NumberExpression<Integer> priority = caseBuilder.when(Expressions.TRUE.isTrue()).then(priorityIndex).otherwise(priorityIndex);

		// 공개 변수가 true인 조건 설정
		query.leftJoin(projectSkill).on(projectSkill.project.eq(project));
		query.leftJoin(projectSkill.skill, skill);
		query.leftJoin(projectType).on(projectType.project.eq(project));
		query.where(isPublic);
		query.where(type);
		query.where(titleContains.or(descriptionContains));
		query.groupBy(project.id);
		query.orderBy(priority.asc(), matchedSkillsCount.desc(), queryScore.desc());


		// pageable 객체 생성
		// DTO의 값으로 수정
		Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
				Sort.by("id").descending());

		// QueryDSL에서 페이징 처리 하는 방법
		this.getQuerydsl().applyPagination(pageable, query);
		// query에 페이징 처리가 적용됨.

		List<Project> list = query.fetch(); // 페이징 처리된 목록 데이터

		long total = query.fetchCount(); // 검색된 전체 데이터 양

		// Collect와 Pageable객체, 전체 데이터 개수로 Page<E>를 리턴해주는 생성자: PageImpl
		return new PageImpl<>(list, pageable, total);

	}

}
