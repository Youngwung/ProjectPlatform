import React, { useEffect, useState } from "react";
import { Button, Col, Form, Row } from "react-bootstrap";
import { deleteOne, getOne, putOne } from "../../api/joinProjectApi";
import useCustomMove from "../../hooks/useCustomMove";
import ModalComponent from "./ModalComponent";

// 기본값 설정
const initState = {
	jpNo: 0,
	userId: 0,
	title: "",
	description: "",
	maxPeople: 0,
	status: "",
	public: false,
	createdAt: "",
	updatedAt: "",
};

export default function ModifyComponent({ jpNo }) {
	const [joinProject, setJoinProject] = useState(initState);

	const [result, setResult] = useState(null);

	const {moveToList, moveToRead} = useCustomMove();

	useEffect(() => {
		getOne(jpNo).then((data) => {
			console.log(data);
			setJoinProject(data);
		});
	}, [jpNo]);

	const handleChange = (e) => {
		const { name, value, type, checked } = e.target;
		setJoinProject({
			...joinProject,
			[name]: type === "checkbox" ? checked : value,
		});
		console.log(e.target.value);
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
		console.log(joinProject);
		setResult("Modified");
		setShowModal(true);
	};

	// 삭제 버튼 클릭 시
	const handleClickDelete = (e) => {
		e.preventDefault();
		console.log(joinProject);
		setResult("Deleted");
		setShowModal(true);
	};

	// 수정 모달 "확인" 클릭 시
	const handleModifyConfirm = () => {
		putOne(joinProject).then((data) => {
			console.log("modify result: " + data.RESULT); // {RESULT: SUCCESS}
			moveToRead(joinProject.id);
		});
	};

	// 삭제 모달 "확인" 클릭 시
	const handleDeleteConfirm = (e) => {
		deleteOne(jpNo).then((data) => {
			console.log("delete result: " + data.RESULT); // {RESULT: SUCCESS}
			moveToList();
		});
	};
	return (
		<div>
			<Form onSubmit={handleClickModify}>
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

				<Button variant="primary" type="submit">
					수정
				</Button>
				<Button variant="danger" onClick={handleClickDelete}>
					삭제
				</Button>
			</Form>

			<ModalComponent
				show={showModal}
				handleClose={handleClose}
				handleConfirm={
					result === "Deleted" ? handleDeleteConfirm : handleModifyConfirm
				}
				// 삭제 버튼 클릭시 삭제 모달 띄워주고 아닌경우 수정 모달 띄워줌
				description={
					result === "Deleted"
						? `"${joinProject.title}"프로젝트를 삭제하시겠습니까?`
						: `"${joinProject.title}"프로젝트를 위 내용으로 수정하시겠습니까?`
				}
			/>
		</div>
	);
}
