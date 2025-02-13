import React, { useState } from "react";
import { Accordion, Button, Form, InputGroup } from "react-bootstrap";

export default function SearchBar() {
	const initState = {
		query: "",
		skills: [],
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
	}

	const [skill, setSkill] = useState("");
	const handleSkillChange = (e) => {
		setSkill(e.target.value);
	};
	const handleSkillClick = (e) => {
		// 검색 결과 혹은 리스트에서 스킬을 클릭했을 때 실행되는 함수
		const value = e.target.value;
		setSkill(value);
		// 기존에 선택한 스킬이 있는지 확인 후 추가
		if (!data.skills.includes(value)) {
			setData({
				...data,
				skills: [...data.skills, value],
			});
		}
	};
	return (
		<div>
			<Form onSubmit={handleSubmit}>
				<InputGroup className="mb-3 d-flex justify-content-center">
					<Form.Control
						name="query"
						type="text"
						value={data.query}
						onChange={handleChange}
						placeholder="검색어를 입력하세요"
					/>
					<Button>검색</Button>
				</InputGroup>
			</Form>
			<Accordion defaultActiveKey={["0"]} alwaysOpen>
				<Accordion.Item eventKey="0">
					<Accordion.Header className="font-extrabold">
						기술 필터
					</Accordion.Header>
					<Accordion.Body>
						<Form>
							<Form.Group controlId="formTitle" className="mb-3">
								<Form.Control
									readOnly
									type="text"
									name="skills"
									value={data.skills}
									onChange={handleChange}
									placeholder="검색할 기술을 추가하세요"
								/>
							</Form.Group>
							<InputGroup className="d-flex justify-content-center">
								{/* 기술 스택을 검색하는 검색 창 구현
								(개수가 적어서 onChange로 api호출해도 괜찮을듯) */}
								<Form.Control
									name="skill"
									type="text"
									value={skill}
									onChange={handleSkillChange}
									placeholder="검색어를 입력하세요"
								/>
								<Button type="submit">
									검색
								</Button>
							</InputGroup>
							{/* 토글 버튼을 클릭했을 때 보이는 기술 스택 선택 창 구현 필요 */}
						</Form>
					</Accordion.Body>
				</Accordion.Item>
			</Accordion>
		</div>
	);
}
