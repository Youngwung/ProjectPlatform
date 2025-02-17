import React, { useCallback, useEffect, useState } from "react";
import { Button, Col, Form, InputGroup, Row } from "react-bootstrap";
import { FaAngleDown, FaAngleUp } from "react-icons/fa";
import { getSkillSearchResult } from "../../api/skillApi";
import SkillSearchComponent from "./SkillSearchComponent";
export default function SearchBar() {
	const initState = {
		query: "",
		querySkills: [],
	};
	const [data, setData] = useState(initState);
	const handleChange = (e) => {
		const { name, value } = e.target;
		setData({
			...data,
			[name]: value,
		});
	};

	const handleSubmit = (e) => {
		e.preventDefault();
		// 검색어를 이용한 검색 기능 구현
	};

	const [skill, setSkill] = useState("");
	const selectedSkill = useCallback((value) => {
		setSkill(value);
	}, []);

	useEffect(() => {
		if (skill && !data.querySkills.includes(skill)) {
			setData({
				...data,
				querySkills: [...data.querySkills, skill],
			});
		}
	}, [skill]);

	const [isVisible, setIsVisible] = useState(false);
	const handleToggle = () => {
		setIsVisible(!isVisible);
	};

	
	return (
		<div>
			<Form onSubmit={handleSubmit}>
				<Row xs={12}>
					<Col xs={6}>
						<InputGroup className="d-flex justify-content-center w-full">
							<span className={`items-start px-2 rounded border w-full`}>
								<Row xs={12} className="max-h-40 overflow-y-auto">
									<Col
										xs={10}
										className="flex flex-wrap py-1 gap-1 items-center justify-items-center "
									>
										{data.querySkills.length === 0 && (
											<span className="text-gray-400">
												기술 스택을 추가하세요
											</span>
										)}
										{data.querySkills.map((skill, index) => (
											<span
												key={index}
												className="flex flex-wrap px-2 rounded bg-primary text-white"
											>
												{skill}
											</span>
										))}
									</Col>
									<Col
										xs={2}
										className="flex justify-content-end align-items-center "
									>
										<span onClick={handleToggle} className="">
											{isVisible ? (
												<FaAngleUp size={24} />
											) : (
												<FaAngleDown size={24} />
											)}
										</span>
									</Col>
								</Row>
							</span>
						</InputGroup>
					</Col>
					<Col xs={6}>
						<InputGroup className="h-10 d-flex justify-content-center">
							<Form.Control
								name="query"
								type="text"
								value={data.query}
								onChange={handleChange}
								placeholder="검색어 입력"
							/>
						</InputGroup>
					</Col>
				</Row>
				{/* 토글 버튼을 클릭했을 때 보이는 기술 스택 선택 창 구현 필요 */}
				{isVisible && (
					<div className="p-1 border">
						<SkillSearchComponent setSelectedSkill={selectedSkill} />
					</div>
				)}
				<div className="flex flex-wrap gap-2">
					
				</div>
				<div className="flex justify-between align-items-center border">
					<span>
						{data.querySkills.map((skill, index) => (
							<span
								key={index}
								className="d-inline-flex px-2 rounded bg-primary text-white inline-block m-1"
							>
								{skill}
							</span>
						))}
					</span>
					<Button type="submit" className="">
						검색
					</Button>
				</div>
			</Form>
		</div>
	);
}
