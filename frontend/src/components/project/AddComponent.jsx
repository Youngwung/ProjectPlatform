import React, { useCallback, useEffect, useState } from "react";
import { Button, Col, Form, Row } from "react-bootstrap";
import { postAdd } from "../../api/projectApi";
import useCustomMove from "../../hooks/useCustomMove";
import InputSkillComponent from "../skill/InputSkillComponent";
import ModalComponent from "./ModalComponent";

export default function AddComponent() {
	// 기본값 설정
	const initState = {
		id: null,
		userName: "",
		title: "",
		skills: "",
		description: "",
		type: "all",
		maxPeople: 0,
		status: "모집_중",
		public: true,
	};
	const [project, setProject] = useState({ ...initState });

	const handleChange = (e) => {
		const { name, value, type, checked } = e.target;
		setProject({
			...project,
			[name]: type === "checkbox" ? checked : value,
		});
	};

	// 모달 및 등록 요청 관련 기능 구현
	const [showModal, setShowModal] = useState(false);

	const { moveToList } = useCustomMove();

	const handleSubmit = (e) => {
		e.preventDefault();
		// 등록 버튼 클릭 시 모달창 띄움
		setShowModal(true);
	};

	const handleModalClose = () => {
		setShowModal(false);
	};

	const handleConfirm = () => {
		postAdd(project).then((result) => {
			console.log(result);
			console.log(project);
			// 기존 인풋 입력값 초기화
			setProject({ ...initState });
			moveToList();
		});
	};

	// 통합 유효성 검사 state 선언
	const [validation, setValidation] = useState({
		skill: false,
	});

	// 스킬 Input 컴포넌트로부터 데이터 불러오기
	const handleValidationComplete = useCallback(({ isValid, value }) => {
		setValidation((prev) => ({
			...prev,
			skill: isValid,
		}));

		if (isValid) {
			setProject((prev) => ({
				...prev,
				skills: value,
			}));
		}
	}, []);

	// 콤보박스 변수 추적 로직
	const handleTypeChange = (e) => {
		const newType = e.target.value;

		setValidation((prev) => ({
			...prev,
			skill: newType === "content", // type이 "content"이면 skill 유효성 검사 통과
		}));
		setProject({ ...project, type: newType });
	};

	// 통합 유효성 검사 state 선언
	useEffect(() => {
		setOnButton(validation.skill);
	}, [validation]);

	const [onButton, setOnButton] = useState(false);

	useEffect(() => {
			setOnButton(validation.skill);
		}, [validation]);

	return (
		<div>
			<Form onSubmit={handleSubmit}>
				<Form.Group controlId="formDescription" className="mb-4">
					<Form.Label>프로젝트 유형 설정</Form.Label>
					<Form.Select
						name="type"
						aria-label="Default select example"
						value={project.type}
						onChange={handleTypeChange}
					>
						<option value="all">모두 설정됨 (기본값)</option>
						<option value="content">주제만 설정됨</option>
						<option value="skill">사용 기술 스택만 설정됨</option>
					</Form.Select>
				</Form.Group>

				<Form.Group controlId="formTitle">
					<Form.Label>프로젝트 제목</Form.Label>
					<Form.Control
						type="text"
						name="title"
						value={project.title}
						onChange={handleChange}
						placeholder="프로젝트 제목을 입력하세요"
						required
					/>
				</Form.Group>

				<Form.Group controlId="formDescription">
					<Form.Label>프로젝트 설명</Form.Label>
					<Form.Control
						as="textarea"
						name="description"
						value={project.description}
						onChange={handleChange}
						rows={3}
						placeholder="프로젝트 설명을 입력하세요"
						required
					/>
				</Form.Group>

				{/* 스킬 Input 컴포넌트 불러오기 */}
				<InputSkillComponent
					onValidationComplete={handleValidationComplete}
					disabled={project.type === "content"}
				/>

				<Row>
					<Col>
						<Form.Group controlId="formMaxPeople">
							<Form.Label>최대 인원</Form.Label>
							<Form.Control
								type="number"
								name="maxPeople"
								value={project.maxPeople}
								onChange={handleChange}
								placeholder="최대 인원을 입력하세요"
							/>
						</Form.Group>
					</Col>
					<Col>
						<Form.Group controlId="formStatus">
							<Form.Label>프로젝트 상태</Form.Label>
							<Form.Control
								as="select"
								name="status"
								value={project.status}
								onChange={handleChange}
							>
								<option value="모집_중">모집 중</option>
								<option value="진행_중">진행 중</option>
								<option value="완료">완료</option>
							</Form.Control>
						</Form.Group>
					</Col>
					<Col>
						<Form.Group controlId="formPublic">
							<Form.Label>공개 여부</Form.Label>
							<Form.Check
								type="checkbox"
								name="public"
								label="공개"
								checked={project.public}
								onChange={handleChange}
							/>
						</Form.Group>
					</Col>
				</Row>

				<Button variant="primary" type="submit" disabled={!onButton}>
					등록
				</Button>
			</Form>

			<ModalComponent
				show={showModal}
				handleClose={handleModalClose}
				handleConfirm={handleConfirm}
				description={`"${project.title}"프로젝트를 등록하시겠습니까?`}
			/>
		</div>
	);
}
