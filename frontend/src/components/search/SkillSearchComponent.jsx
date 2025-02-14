import React, { useState } from "react";
import { Col, ListGroup, Row } from "react-bootstrap";
import { FaAngleRight } from "react-icons/fa";

export default function SkillSearchComponent({ setSelectedSkill }) {
	const initSkillCategoryData = [{ 
		id: null, 
		name: "" 
	}];
	const [skillCategories, setSkillCategories] = useState(initSkillCategoryData);
	// TODO: api 개발 후 서버 데이터로 skillCategories 초기화 (useEffect로 마운트 시 실행)

	const initSkillData = [
		{
			id: null,
			name: "",
			category: "",
		},
	];
	
	const handleSkillClick = (e) => {
		// 부모컴포넌트에 선택한 스킬 전달
		const value = e.target.value;
		console.log(value);
		setSelectedSkill(value);
	};
	
	const [skills, setSkills] = useState(initSkillData);
	const handleSkillCategoryClick = (e) => {
		// value = categoryId
		const value = e.target.value;
		console.log(value);
		// TODO: api 개발 후 categoryId로 호출 후 서버 데이터로 skills 초기화

	}

	return (
		<div>
			<Row>
				<Col xs={2} className="pr-0">
					<ListGroup>
						{skillCategories.map((category, index) => (
							<ListGroup.Item className="m-0 py-1 px-0">
								<button
									key={index}
									className="btn pl-2 pr-0 flex w-full items-center justify-between"
									onClick={handleSkillCategoryClick}
									value={category.id}
								>
									<span>{category.name}</span>
									<span className="inline-block ml-auto">
										<FaAngleRight />
									</span>
								</button>
							</ListGroup.Item>
						))}
					</ListGroup>
				</Col>
				<Col xs={10} className="p-2 flex items-start flex-wrap gap-2">
					{skills.map((skill, index) => (
						<button
							key={index}
							value={skill}
							className="btn border"
							onClick={handleSkillClick}
						>
							{skill}
						</button>
					))}
				</Col>
			</Row>
		</div>
	);
}
