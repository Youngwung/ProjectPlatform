import React, { useEffect, useState } from "react";
import { Badge, Button, Card, Col, Container, Row } from "react-bootstrap";
import { getOne } from "../../api/projectApi";
import useCustomMove from "../../hooks/useCustomMove";
import useCustomString from "../../hooks/useCustomString";
import BookmarkProjectBtn from "../bookmark/BookmarkProjectBtn";
import SkillTagComponent from "../skill/SkillTagComponent";
import SkillTagGuideComponent from "../skill/SkillTagGuideComponent";

// 조회 기능을 구현하기 위한 컴포넌트

// 기본값 설정
const initState = {
	id: 0,
	userId: 0,
	title: "",
	description: "",
	maxPeople: 0,
	status: "",
	public: false,
	createdAt: "",
	updatedAt: "",
};

// 상태에 따른 색상 변경
const getStatusVariant = (status) => {
	switch (status) {
		case "모집_중":
			return "success";
		case "진행_중":
			return "warning";
		case "완료":
			return "secondary";
		default:
			return "light";
	}
};

export default function ReadComponent({ projectId }) {
	const [project, setProject] = useState(initState);

	// 커스텀 훅에서 리스트 페이지로 이동하는 함수 가져옴
	const {moveToList, moveToModify} = useCustomMove();

	// 커스텀 훅에서 스테이터스의 _를 공백으로 바꿔주는 함수 가져옴
	const {statusToString} = useCustomString();

	useEffect(() => {
		getOne(projectId).then((data) => {
			setProject(data);
		});
		
		return () => {};
	}, [projectId]);
	
	console.log(project);
	return (
		<div>
			<Container className="mt-4">
				<Card>
					<Card.Header>
						<h4>{project.title}</h4>
						<Badge bg={project.public ? "primary" : "danger"}>
							{project.public ? "공개" : "비공개"}
						</Badge>
					</Card.Header>
					<Card.Body>
						<Row className="mb-3">
						<Col>
								<BookmarkProjectBtn
									projectId = {project.id}
									userId = {project.userId}
								/>
							</Col>
							<Col>
								<strong>작성자:</strong> {project.userId}
							</Col>
							<Col>
								<strong>최대 인원:</strong> {project.maxPeople}명
							</Col>
							<Col>
								<Badge bg={getStatusVariant(project.status)}>
									{statusToString(project.status)}
								</Badge>
							</Col>
						</Row>
						<Row className="mb-3">
							<Col>
								<SkillTagGuideComponent />
								<SkillTagComponent skills = {project.skills}/>
							</Col>
						</Row>
						<Row className="mb-3">
							<Col>
								<strong>프로젝트 설명:</strong>
							</Col>
						</Row>
						<Row>
							<Col>{project.description}</Col>
						</Row>
						<Row className="mt-4">
							<Col>
								<Button variant="primary" onClick={() => moveToList()}>리스트</Button>
							</Col>
							<Col>
								<Button variant="primary" onClick={() => moveToModify(projectId)}>수정</Button>
							</Col>
						</Row>
						<Row className="mt-2 text-muted">
							<Col>
								생성 시간: {new Date(project.createdAt).toLocaleString()}
							</Col>
							<Col>
								수정 시간: {new Date(project.updatedAt).toLocaleString()}
							</Col>
						</Row>
					</Card.Body>
				</Card>
			</Container>
		</div>
	);
}
