import React, { useCallback, useEffect, useState } from "react";
import { Button, Col, Form, Row } from "react-bootstrap";
import { postAdd } from "../../api/projectApi";
import useCustomMove from "../../hooks/useCustomMove";
import InputSkillComponent from "../skill/InputSkillComponent";
import ModalComponent from "./ModalComponent";

export default function AddComponent() {
	// 기본값 설정
	const initState = {
		projectId: 0,
		userId: 0,
		title: "",
		skills: "",
		description: "",
		maxPeople: 0,
		status: "모집_중",
		public: false,
	};
	const [joinProject, setJoinProject] = useState({ ...initState });

	const handleChange = (e) => {
		const { name, value, type, checked } = e.target;
		setJoinProject({
			...joinProject,
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
		postAdd(joinProject).then((result) => {
			console.log(result);
			console.log(joinProject);
			// 기존 인풋 입력값 초기화
			setJoinProject({ ...initState });
			moveToList();
		});
	};

	// TODO: userId를 가져오는 로직 추가
	const [userId, setUserId] = useState(0);
	useEffect(() => {
		setUserId(localStorage.getItem("userId"));
		if (userId) {
			setJoinProject({ ...initState, userId: userId });
		} else {
			// 원래는 아무 기능을 하지 않아야 하지만 개발 당시 로그인 기능을 구현하지 않았으므로 강제로 userId = 1을 주입
			setJoinProject({ ...initState, userId: 1 });
		}
	}, [userId]);

	// 스킬 입력 컴포넌트 불러오기 위한 변수
	const [validSkill, setValidSkill] = useState(false);

	const handleValidationComplete = useCallback(({ isValid, value }) => {
		if (isValid) {
			// 스킬 유효성 검사 통과
			setValidSkill(true);
			// joinProject 업데이트
			setJoinProject(prev => ({
				...prev,
				skills: value
			}))
		} else {
			// 등록 버튼 비활성화
			setValidSkill(false);
		}
	}, []);
	const [onButton, setOnButton] = useState(false);

	useEffect(() => {
		setOnButton(validSkill);
	}, [validSkill]);

	return (
		<div>
			<Form onSubmit={handleSubmit}>
				<Form.Group controlId="formTitle">
					<Form.Label>프로젝트 제목</Form.Label>
					<Form.Control
						type="text"
						name="title"
						value={joinProject.title}
						onChange={handleChange}
						placeholder="프로젝트 제목을 입력하세요"
					/>
				</Form.Group>

				<Form.Group controlId="formDescription">
					<Form.Label>프로젝트 설명</Form.Label>
					<Form.Control
						as="textarea"
						name="description"
						value={joinProject.description}
						onChange={handleChange}
						rows={3}
						placeholder="프로젝트 설명을 입력하세요"
					/>
				</Form.Group>

				{/* 스킬 Input 컴포넌트 불러오기 */}
				<InputSkillComponent onValidationComplete={handleValidationComplete} />

				<Row>
					<Col>
						<Form.Group controlId="formMaxPeople">
							<Form.Label>최대 인원</Form.Label>
							<Form.Control
								type="number"
								name="maxPeople"
								value={joinProject.maxPeople}
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
								value={joinProject.status}
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
								checked={joinProject.public}
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
				description={`"${joinProject.title}"프로젝트를 등록하시겠습니까?`}
			/>
		</div>
	);
}
