import React, { useEffect, useState } from "react";
import { Badge, Button, Card, Col, Container, Row } from "react-bootstrap";
import { getOne } from "../../api/joinProjectApi";
import useCustomMove from "../../hooks/useCustomMove";

// 조회 기능을 구현하기 위한 컴포넌트

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

export default function ReadComponent({ jpNo }) {
	const [joinProject, setJoinProject] = useState(initState);

	// 커스텀 훅에서 리스트 페이지로 이동하는 함수 가져옴
	const {moveToList, moveToModify} = useCustomMove();

	useEffect(() => {
		getOne(jpNo).then((data) => {
			setJoinProject(data);

		});
		
		return () => {};
	}, [jpNo]);
	
	console.log(joinProject);
	return (
		<div>
			<Container className="mt-4">
				<Card>
					<Card.Header>
						<h4>{joinProject.title}</h4>
						<Badge bg={joinProject.public ? "primary" : "danger"}>
							{joinProject.public ? "공개" : "비공개"}
						</Badge>
					</Card.Header>
					<Card.Body>
						<Row className="mb-3">
							<Col>
								<strong>작성자:</strong> {joinProject.userId}
							</Col>
							<Col>
								<strong>최대 인원:</strong> {joinProject.maxPeople}명
							</Col>
							<Col>
								<Badge bg={getStatusVariant(joinProject.status)}>
									{joinProject.status}
								</Badge>
							</Col>
						</Row>
						<Row className="mb-3">
							<Col>
								<strong>프로젝트 설명:</strong>
							</Col>
						</Row>
						<Row>
							<Col>{joinProject.description}</Col>
						</Row>
						<Row className="mt-4">
							<Col>
								<Button variant="primary" onClick={() => moveToList()}>리스트</Button>
							</Col>
							<Col>
								<Button variant="primary" onClick={() => moveToModify(joinProject.id)}>수정</Button>
							</Col>
						</Row>
						<Row className="mt-2 text-muted">
							<Col>
								생성 시간: {new Date(joinProject.createdAt).toLocaleString()}
							</Col>
							<Col>
								수정 시간: {new Date(joinProject.updatedAt).toLocaleString()}
							</Col>
						</Row>
					</Card.Body>
				</Card>
			</Container>
		</div>
	);
}
