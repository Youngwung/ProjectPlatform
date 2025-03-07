import React, { useCallback, useEffect, useState } from "react";
import { Button, Col, Form, Row } from "react-bootstrap";
import { deleteOne, getOne, putOne } from "../../api/projectApi";
import useCustomMove from "../../hooks/useCustomMove";
import InputSkillComponent from "../skill/InputSkillComponent";
import ModalComponent from "./ModalComponent";

// 기본값 설정
const initState = {
	id: 0,
	userName: 0,
	title: "",
	skills: "",
	description: "",
	type: "",
	maxPeople: 0,
	status: "",
	public: false,
	createdAt: "",
	updatedAt: "",
};

export default function ModifyComponent({ projectId }) {
	const [project, setProject] = useState(initState);

	const [result, setResult] = useState(false);

	const { moveToList, moveToRead } = useCustomMove();

	useEffect(() => {
		const fetchData = async () => {
			const data = await getOne(projectId);
			//console.log(data);
			setProject(data);
		}
			fetchData();
		}, [projectId]);

	const handleChange = (e) => {
		const { name, value, type, checked } = e.target;
		setProject({
			...project,
			[name]: type === "checkbox" ? checked : value,
		});
		//console.log(e.target.value);
	};

	// 모달 및 등록 요청 관련 기능 구현
	const [showModal, setShowModal] = useState(false);

	// 취소 클릭 시
	const handleClose = () => {
		setShowModal(false);
	};

	// 수정 버튼 클릭 시
	const handleClickModify = (e) => {
		e.preventDefault();
		//console.log(project);
		setResult("Modified");
		setShowModal(true);
	};

	// 삭제 버튼 클릭 시
	const handleClickDelete = (e) => {
		e.preventDefault();
		//console.log(project);
		setResult("Deleted");
		setShowModal(true);
	};

	// 수정 모달 "확인" 클릭 시
	const handleModifyConfirm = () => {
		putOne(project).then((data) => {
			//console.log("modify result: " + data.RESULT); // {RESULT: SUCCESS}
			moveToRead(project.id);
		});
	};

	// 삭제 모달 "확인" 클릭 시
	const handleDeleteConfirm = (e) => {
		deleteOne(projectId).then((data) => {
			//console.log("delete result: " + data.RESULT); // {RESULT: SUCCESS}
			moveToList();
		});
	};

	// 통합 유효성 검사 state 선언
	const [validation, setValidation] = useState({
		skill: false
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

	// 수정 버튼 활성화/비활성화 여부를 저장하는 변수 선언
	const [onButton, setOnButton] = useState(false);

	useEffect(() => {
		setOnButton(validation.skill);
	}, [validation]);

	return (
		<div>
			<Form onSubmit={handleClickModify}>
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
					// 스킬 유효성 검사 결과를 부모 컴포넌트로 전달
					onValidationComplete={handleValidationComplete}
					// 스킬 데이터 초기값 설정
					skills={project.skills}
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

				{/* 모든 유효성 검사를 통과해야 수정 버튼 활성화 */}
				<Button variant="primary" type="submit" disabled={!onButton}>
					수정
				</Button>
				<Button variant="danger" onClick={handleClickDelete}>
					삭제
				</Button>
			</Form>

			<ModalComponent
				show={showModal}
				handleClose={() => handleClose()}
				handleConfirm={
					result === "Deleted" ? handleDeleteConfirm : handleModifyConfirm
				}
				// 삭제 버튼 클릭시 삭제 모달 띄워주고 아닌경우 수정 모달 띄워줌
				description={
					result === "Deleted"
						? `"${project.title}"프로젝트를 삭제하시겠습니까?`
						: `"${project.title}"프로젝트를 위 내용으로 수정하시겠습니까?`
				}
			/>
		</div>
	);
}
