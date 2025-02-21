package com.ppp.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.ppp.backend.domain.Portfolio;
import com.ppp.backend.domain.QPortfolio;
import com.ppp.backend.domain.QSkill;
import com.ppp.backend.domain.QUserSkill;
import com.ppp.backend.dto.PageRequestDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PortfolioSearchImpl extends QuerydslRepositorySupport implements PortfolioSearch {
	public PortfolioSearchImpl() {
		super(Portfolio.class);
	}

	@Override
	public Page<Portfolio> searchString(PageRequestDTO pageRequestDTO) {
		QPortfolio portfolio = QPortfolio.portfolio;

		JPQLQuery<Portfolio> query = from(portfolio);

		Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
				Sort.by("id").descending());

		this.getQuerydsl().applyPagination(pageable, query);

		List<Portfolio> list = query.fetch();

		long total = query.fetchCount();
		return new PageImpl<>(list, pageable, total);
	}

	@Override
	public Page<Portfolio> searchKeyword(PageRequestDTO pageRequestDTO) {
		QPortfolio portfolio = QPortfolio.portfolio;


		// JoinPortfolio 테이블에서
		JPQLQuery<Portfolio> query = from(portfolio);

		// ! 검색 우선순위 로직
		QUserSkill userSkill = QUserSkill.userSkill;
		QSkill skill = QSkill.skill;

		// 검색 키워드
		BooleanExpression titleContains = portfolio.title.containsIgnoreCase(pageRequestDTO.getQuery());
		BooleanExpression descriptionContains = portfolio.description.containsIgnoreCase(pageRequestDTO.getQuery());

		// 스킬이 하나라도 존재하는 포트폴리오만 출력
		BooleanExpression skillExist = userSkill.skill.name.in(pageRequestDTO.getQuerySkills());

		// Portfolio가 가진 스킬 중 querySkills에 해당하는 스킬의 개수를 구함
		NumberExpression<Integer> matchedSkillsCount = new CaseBuilder()
				.when(userSkill.skill.name.in(pageRequestDTO.getQuerySkills())).then(1)
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
		NumberExpression<Integer> priority = caseBuilder.when(Expressions.TRUE.isTrue()).then(priorityIndex)
				.otherwise(priorityIndex);

		query.leftJoin(userSkill).on(userSkill.user.eq(portfolio.user));
		query.leftJoin(userSkill.skill, skill);
		query.where(titleContains.or(descriptionContains));
		query.where(skillExist);
		query.groupBy(portfolio.id);
		query.orderBy(priority.asc(), matchedSkillsCount.desc(), queryScore.desc());

		// pageable 객체 생성
		// DTO의 값으로 수정
		Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
				Sort.by("id").descending());

		// QueryDSL에서 페이징 처리 하는 방법
		this.getQuerydsl().applyPagination(pageable, query);
		// query에 페이징 처리가 적용됨.

		List<Portfolio> list = query.fetch(); // 페이징 처리된 목록 데이터

		long total = query.fetchCount(); // 검색된 전체 데이터 양

		// Collect와 Pageable객체, 전체 데이터 개수로 Page<E>를 리턴해주는 생성자: PageImpl
		return new PageImpl<>(list, pageable, total);
	}

}
