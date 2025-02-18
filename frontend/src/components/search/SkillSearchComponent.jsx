import React, { useCallback, useEffect, useState } from "react";
import { Col, Form, InputGroup, ListGroup, Row } from "react-bootstrap";
import { FaAngleRight } from "react-icons/fa";
import { getSkillList, getSkillSearchResult } from "../../api/skillApi";

export default function SkillSearchComponent({ setSelectedSkill }) {
	const initSkillData = [
		{
			id: null,
			name: "",
			skillCategoryId: null,
		},
	];
	const initSkillCategoryData = [
		{
			id: null,
			name: "",
			description: "",
			skills: initSkillData,
		},
	];

	const [skillCategories, setSkillCategories] = useState(initSkillCategoryData);
	// 스킬 카테고리 데이터 가져오기
	const [skills, setSkills] = useState(initSkillData);
	useEffect(() => {
		getSkillList().then((data) => {
			console.log(data);
			setSkillCategories(data);
			console.log(skillCategories);
			// 처음 표시할 스킬 설정
			setSkills(data[0].skills);
			console.log(data[0].skills);
		});
	}, []);

	const handleSkillClick = (e) => {
		// 부모컴포넌트에 선택한 스킬 전달
		const value = e.target.value;
		console.log(value);
		setSelectedSkill(value);
	};

	const handleSkillCategoryClick = (id) => {
		// value = categoryId
		setActiveItem(id - 1);
		console.log(id);
		setSkills(skillCategories[id - 1].skills);
	};

	// 스킬 검색 기능
	const initSkillResult = [
		{
			id: null,
			skill: "",
			skillCategoryName: "",
		},
	];
	const [skillResult, setSkillResult] = useState(initSkillResult);
	const searchResult = useCallback(async (skillQuery) => {
		await getSkillSearchResult({ skillQuery })
			.then((data) => {
				setSkillResult(data);
			})
			.catch((error) => console.log(error));
	}, []);
	// 검색 결과를 표시할 지 카테고리의 스킬리스트를 표시할 지 정하는 변수 선언
	const [isSearch, setIsSearch] = useState(false);
	const handleSkillQueryChange = (e) => {
		const value = e.target.value;
		if (value.trim() === "") {
			// value가 빈 문자열인 경우 API 호출하지 않음
			setIsSearch(false);
			console.log("기술 검색창 빈 문자열");
			return;
		}
		// 검색 결과 표시
		console.log("기술 검색창 문자열 있음");
		setIsSearch(true);
		searchResult(value);
	};
	// 선택된 카테고리 하이라이팅을 위한 변수 선언
	const [activeItem, setActiveItem] = useState(0);

	return (
		<div>
			<InputGroup className="d-flex justify-content-center">
				{/* 기술 스택을 검색하는 검색 창 구현
									(개수가 적어서 onChange로 api호출해도 괜찮을듯) */}
				<Form.Control
					name="skill"
					type="text"
					onChange={handleSkillQueryChange}
					placeholder="검색할 기술을 입력하세요"
				/>
			</InputGroup>
			<div>
				<Row>
					<Col xs={2} className="pr-0">
						<ListGroup className="max-h-64 overflow-y-auto">
							{skillCategories &&
								skillCategories.map((category, index) => (
									<ListGroup.Item
										className="m-0 py-1 px-0"
										key={index}
										active={activeItem === index}
									>
										<button
											className="btn pl-2 pr-0 flex w-full flex-wrap items-start justify-start"
											type="button"
											value={category.id}
											onClick={() => handleSkillCategoryClick(category.id)}
										>
											<span>{category.description}</span>
											<span className="text-gray-400">
												({category.skills.length})
											</span>
											<span className="inline-block ml-auto">
												<FaAngleRight />
											</span>
										</button>
									</ListGroup.Item>
								))}
						</ListGroup>
					</Col>
					<Col xs={10} className="p-2">
						{isSearch && skillResult
							? // 검색 키워드가 있는 경우
							  skillResult.map((skill, index) => (
									<button
										key={index}
										value={skill.name}
										type="button"
										className="border rounded inline-block m-1 p-1"
										onClick={handleSkillClick}
									>
										<span className="text-gray-400">
											{skill.skillCategoryName} {">"}{" "}
										</span>
										{skill.skill}
									</button>
							  ))
							: // 검색 키워드가 없는 경우
							  skills.map((skill, index) => (
									<button
										key={index}
										value={skill.name}
										type="button"
										className="border rounded inline-block m-1 p-1"
										onClick={handleSkillClick}
									>
										{skill.name}
									</button>
							  ))}
					</Col>
				</Row>
			</div>
		</div>
	);
}
