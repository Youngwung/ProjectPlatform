import React, { useCallback, useEffect, useState } from "react";
import { Button, Col, Form, InputGroup, Row } from "react-bootstrap";
import { FaAngleDown, FaAngleUp, FaTimes } from "react-icons/fa";
import { useLocation } from "react-router-dom";
import useCustomMove from "../../hooks/useCustomMove";
import useCustomPortfolioMove from "../../hooks/useCustomPortfolioMove";
import SkillSearchComponent from "./SkillSearchComponent";
export default function SearchBar({ queryData }) {
	const { moveToSearch } = useCustomMove();
	const { moveToPortfolioSearch } = useCustomPortfolioMove();
	const [data, setData] = useState(queryData);
	const location = useLocation();
	const inProjectPage = location.pathname.includes("project");

	const handleChange = (e) => {
		const { name, value, type, checked } = e.target;
		setData({
			...data,
			[name]: type === "checkbox" ? checked : value,
		});
		console.log(name);
		console.log(value);
	};

	const handleOnSkillDelete = (skillToDelete) => {
		setData((prevData) => ({
			...prevData,
			querySkills: prevData.querySkills.filter(
				(skill) => skill !== skillToDelete
			),
		}));
	};

	const handleSubmit = (e) => {
		e.preventDefault();
		// 검색어를 이용한 검색 기능 구현
		console.log("submit");
		console.log(data);
		setIsVisible(false);
		if (inProjectPage) {
			moveToSearch({
				...data,
				page: 1,
			});
		} else {
			moveToPortfolioSearch({
				...data,
				page: 1,
			});
		}
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

	useEffect(() => {
		setData((prev) => ({
			...prev,
			...queryData,
		}));
	}, [queryData]);

	const [isVisible, setIsVisible] = useState(false);
	const handleToggle = () => {
		setIsVisible(!isVisible);
	};

	return (
		<div>
			<Form>
				<Row xs={12}>
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
											className="flex flex-wrap items-center px-2 rounded bg-primary text-white"
										>
											<span>{skill}</span>
											<FaTimes
												onClick={() => handleOnSkillDelete(skill)}
												className="ps-1 cursor-pointer"
											/>
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
				</Row>
				{/* 토글 버튼을 클릭했을 때 보이는 기술 스택 선택 창 구현 필요 */}
				{isVisible && (
					<div className="p-1 border">
						<SkillSearchComponent
							setSelectedSkill={selectedSkill}
							querySkills={data.querySkills}
						/>
					</div>
				)}
				<div className="flex flex-wrap gap-2"></div>
				<div className="flex w-full justify-between align-items-center border">
					<Row xs={12} className="w-full">
						{inProjectPage && (
							<Col xs={3}>
								<Form.Select
									name="type"
									aria-label="Default select example"
									value={data.type}
									onChange={handleChange}
								>
									<option value="all">모두 설정됨 (기본값)</option>
									<option value="content">주제만 설정됨</option>
									<option value="skill">사용 기술 스택만 설정됨</option>
								</Form.Select>
							</Col>
						)}
						<Col>
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
					<Button type="submit" onClick={handleSubmit} className="w-16">
						검색
					</Button>
				</div>
			</Form>
		</div>
	);
}
