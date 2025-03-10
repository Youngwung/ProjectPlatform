import React, { useContext, useEffect, useState } from "react";
import { Badge, Button, Card, Col, Container, Row } from "react-bootstrap";
import alertApi from "../../api/alertApi";
import { checkWriter, getOne } from "../../api/projectApi";
import { AlertContext } from "../../context/AlertContext";
import useCustomMove from "../../hooks/useCustomMove";
import useCustomString from "../../hooks/useCustomString";
import BookmarkProjectBtn from "../bookmark/BookmarkProjectBtn";
import SkillTagComponent from "../skill/SkillTagComponent";
import SkillTagGuideComponent from "../skill/SkillTagGuideComponent";

// 조회 기능을 구현하기 위한 컴포넌트
// 기본값 설정
const initState = {
	id: 0,
	userName: "",
	title: "",
	description: "",
	type: "",
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
	const { refreshAlerts } = useContext(AlertContext);
	const [project, setProject] = useState(initState);
	const [projectAlert, setProjectAlert] = useState(null);
	
	// 커스텀 훅에서 리스트 페이지로 이동하는 함수 가져옴
	const {moveToList, moveToModify} = useCustomMove();
	
	// 커스텀 훅에서 스테이터스의 _를 공백으로 바꿔주는 함수 가져옴
	const {statusToString} = useCustomString();
	
	// 수정버튼 활성화/비활성화 여부 저장 변수 선언
	const [isWriter, setIsWriter] = useState(false);
	
	// 프로젝트 상세 정보 불러오기
	useEffect(() => {
		getOne(projectId).then((data) => {
			setProject(data);
		});

		// 작성자인 지 확인하는 API 호출
		checkWriter(projectId).then((res) => {
			setIsWriter(res)
		}).catch((error) => {
			console.error(error);
		})
	}, [projectId]);

	// 현재 로그인한 사용자가 받은 프로젝트 알림 목록 중, 이 프로젝트에 해당하는 알림을 찾음
	useEffect(() => {
		// alertApi.getProjectAlerts()는 현재 로그인한 사용자의 모든 프로젝트 알림을 반환한다고 가정합니다.
		alertApi.getProjectAlerts()
			.then((alerts) => {
				// 프로젝트 id는 숫자형이므로 비교 시 Number 변환
				const matchingAlert = alerts.find(
					(alert) => Number(alert.project.id) === Number(projectId)
				);
				setProjectAlert(matchingAlert);
			})
			.catch((error) => {
				console.error("알림 조회 실패:", error);
			});
	}, [projectId]);

	// 참가 신청 버튼 클릭 시 처리
	const handleApply = async () => {
		try {
			await alertApi.applyProject(projectId);
			alert("프로젝트 참가 신청이 완료되었습니다.");
			// 신청 후 다시 알림 목록을 갱신하여 버튼이 뱃지로 대체되도록 함
			setTimeout(async () => {
				await refreshAlerts();
				// 선택적으로 전체 알림을 다시 가져와 local 상태 업데이트도 가능
				const alerts = await alertApi.getProjectAlerts();
				const matchingAlert = alerts.find(
				  (alert) => Number(alert.project.id) === Number(projectId)
				);
				setProjectAlert(matchingAlert);
			  }, 500); // 500ms 딜레이
		} catch (error) {
			console.error("참가 신청 처리 중 오류 발생:", error);
			alert("참가 신청에 실패했습니다. 다시 시도해주세요.");
		}
	};

	return (
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
								projectId={project.id}
							/>
						</Col>
						<Col>
							<strong>작성자:</strong> {project.userName}
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
							<SkillTagComponent skills={project.skills} />
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
							<Button variant="primary" onClick={() => moveToList()}>
								리스트
							</Button>
						</Col>
						<Col>
							<Button 
								hidden={!isWriter}
								variant="primary" 
								onClick={() => moveToModify(projectId)}
							>
								수정
							</Button>
						</Col>
						<Col>
							{/* 알림이 이미 존재하면 참가 신청 버튼 대신 뱃지 표시 */}
							{projectAlert ? (
								<>
									프로젝트 신청상태 : 
									<Badge bg={projectAlert.isRead ? "secondary" : "primary"}>
										{projectAlert.status}
									</Badge>
								</>
							) : (
								<Button variant="success" onClick={handleApply}>
									참가 신청
								</Button>
							)}
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
	);
}