import React, { useState, useEffect, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Container, Card, Badge, Button, Spinner, Row, Col } from "react-bootstrap";
import { HiOutlineMailOpen, HiOutlineMail } from "react-icons/hi";
import alertApi from "../../api/alertApi";
import { AlertContext } from "../../context/AlertContext";

const AlertDetail = ({ isProject }) => {
  const { alertId } = useParams();
  const navigate = useNavigate();
  const [alert, setAlert] = useState(null);
  const [loading, setLoading] = useState(true);
  // 별도의 상태 변수(actionUI)를 이용하여 버튼/뱃지를 동적으로 관리
  const [actionUI, setActionUI] = useState(null);
  const { refreshAlerts } = useContext(AlertContext);
  // 백엔드에서 AlertDetail 정보를 받아옴 (senderUserDto, receiverUserDto, alertOwnerUserDto 포함)
  useEffect(() => {
    const fetchAlertDetail = async () => {
      if (!alertId) return;
      setLoading(true);
      const data = await alertApi.getOneAlert(alertId, isProject);
      console.log("알림 상세 정보:", data);
      setAlert(data);
      setLoading(false);
    };
    fetchAlertDetail();
  }, [alertId, isProject]);

  useEffect(() => {
    if (!alert) return;

    if (alert.step === 1) {
      if (alert.type === "참가알림" && isProject && alert.myProject) {
        // 신청 알림이고 내가 소유한 프로젝트인 경우
        setActionUI(
          <div className="text-center mt-4">
            <Button variant="success" className="px-4" onClick={handleAccept}>
              신청 수락
            </Button>
            <Button variant="danger" className="ms-3 px-4" onClick={handleReject}>
              신청 거절
            </Button>
          </div>
        );
      } else if (alert.type === "초대알림" && isProject && !alert.myProject) {
        // 초대 알림이고 내가 소유하지 않은 경우
        setActionUI(
          <div className="text-center mt-4">
            <Button variant="success" className="px-4" onClick={handleAccept}>
              초대 수락
            </Button>
            <Button variant="danger" className="ms-3 px-4" onClick={handleReject}>
              초대 거절
            </Button>
          </div>
        );
      } else {
        setActionUI(null);
      }
    } else if (alert.step === 2) {
      // 응답 후 업데이트된 기존 알림(step 2): 버튼 대신 상태 Badge 표시
      setActionUI(
        <div className="text-center mt-4">
          <Badge bg="info" className="px-3 py-2">
            {alert.status}
          </Badge>
        </div>
      );
    } else {
      setActionUI(null);
    }
  }, [alert, isProject]);

  const handleMarkAsRead = async () => {
    if (!alert || alert.isRead) return;
    await alertApi.markAlertAsRead(alert.id, isProject);
    setAlert((prev) => ({ ...prev, isRead: true }));
  };

  // 신청/초대 응답 API 호출
  const handleAccept = async () => {
    if (!alert || !alert.project) return;
    if (alert.type === "참가알림") {
      await alertApi.acceptApplication(alert.project.id, alert.senderUserDto.id);
      console.log("신청 수락 처리 완료");
      setTimeout(async () => {
        await refreshAlerts();
        const updatedAlert = await alertApi.getOneAlert(alert.id, isProject);
        setAlert(updatedAlert);
      }, 500);
    } else if (alert.type === "초대알림") {
      await alertApi.acceptInvite(alert.project.id, alert.id);
      console.log("초대 수락 처리 완료");
      setTimeout(async () => {
        await refreshAlerts();
        const updatedAlert = await alertApi.getOneAlert(alert.id, isProject);
        setAlert(updatedAlert);
      }, 500);
    }
  };

  const handleReject = async () => {
    if (!alert || !alert.project) return;
    if (alert.type === "참가알림") {
      await alertApi.rejectApplication(alert.project.id, alert.senderUserDto.id);
      console.log("신청 거절 처리 완료");
    } else if (alert.type === "초대알림") {
      await alertApi.rejectInvite(alert.project.id, alert.id);
      console.log("초대 거절 처리 완료");
    }
  };

  if (loading) {
    return (
      <Container className="text-center mt-5">
        <Spinner animation="border" />
        <p>알림을 불러오는 중...</p>
      </Container>
    );
  }

  if (!alert) {
    return (
      <Container className="text-center mt-5">
        <p className="text-danger">❌ 알림 정보를 찾을 수 없습니다.</p>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <Card className="shadow-sm border-light">
        <Card.Header className="bg-success text-white">
          <h5 className="fw-bold">
            {isProject ? "📁 프로젝트 알림" : "📂 포트폴리오 알림"}
          </h5>
        </Card.Header>

        <Card.Body className="p-4">
          {/* 기본 알림 정보 */}
          <Row className="mb-3">
            <Col md={8}>
              <h6 className="fw-bold">
                보낸 사람: {alert.senderUserDto?.name || "알 수 없음"}
              </h6>
              <h6 className="fw-bold">
                받는 사람: {alert.receiverUserDto?.name || "알 수 없음"}
              </h6>
              <p className="text-muted">
                {new Date(alert.createdAt).toLocaleString()}
              </p>
            </Col>
            <Col md={4} className="text-end">
              <Badge bg="info" className="px-3 py-2">
                {alert.status}
              </Badge>
              <Button
                variant="outline-secondary"
                size="sm"
                onClick={handleMarkAsRead}
                disabled={alert.isRead}
              >
                {alert.isRead ? (
                  <HiOutlineMailOpen size={20} />
                ) : (
                  <HiOutlineMail size={20} />
                )}
              </Button>
            </Col>
          </Row>
          <hr />
          <h6 className="fw-bold py-4 fs-2">{alert.content}</h6>
          <hr />

          {/* 정보 영역: 신청자 정보 또는 프로젝트 정보 */}
          {isProject && alert.project && (
            <>
              {alert.myProject ? (
                alert.type === "참가알림" ? (
                  <>
                    <h6 className="fw-bold">👤 신청자 정보</h6>
                    <p>
                      <strong>이름:</strong> {alert.senderUserDto?.name || "알 수 없음"} <br />
                      <strong>이메일:</strong> {alert.senderUserDto?.email || "알 수 없음"} <br />
                      <strong>경험치:</strong> {alert.senderUserDto?.experience || "알 수 없음"} <br />
                      <strong>링크:</strong>{" "}
                      {alert.senderUserDto?.links && alert.senderUserDto.links.length > 0 ? (
                        alert.senderUserDto.links.map((link, index) => (
                          <span key={link.id}>
                            <a href={link.url} target="_blank" rel="noreferrer">
                              {link.description || link.url}
                            </a>
                            {index < alert.senderUserDto.links.length - 1 ? ", " : ""}
                          </span>
                        ))
                      ) : (
                        "링크 정보 없음"
                      )}
                    </p>
                  </>
                ) : (
                  <p className="text-muted">추가 신청자 정보가 없습니다.</p>
                )
              ) : (
                alert.type === "초대알림" && (
                  <>
                    <h6 className="fw-bold">📂 프로젝트 정보</h6>
                    <p>
                      <strong>프로젝트명:</strong> {alert.project?.title || "알 수 없음"} <br />
                      <strong>설명:</strong> {alert.project?.description || "알 수 없음"} <br />
                      <strong>최대 인원:</strong> {alert.project?.maxPeople || "알 수 없음"} <br />
                      <strong>상태:</strong> {alert.project?.status || "알 수 없음"}
                    </p>
                  </>
                )
              )}
            </>
          )}

          {/* actionUI를 통해 alert.step에 따라 동적으로 버튼/뱃지 영역 렌더링 */}
          {actionUI}

          {/* 내비게이션 버튼: 중복되지 않게 한 번만 표시 */}
          <div className="text-center mt-4">
            <Button variant="success" className="px-4" onClick={() => navigate("/mypage/alert")}>
              🔙 뒤로 가기
            </Button>
            <Button
              variant="primary"
              className="ms-3 px-4"
              onClick={() =>
                navigate(isProject ? `/project/read/${alert.project?.id}` : `/portfolio/${alert.portfolio?.id}`)
              }
            >
              🔍 바로가기
            </Button>
          </div>
        </Card.Body>

        <Card.Footer className="text-muted text-center">
          📧 본 메일은 알림 목적으로 자동 생성되었습니다.
        </Card.Footer>
      </Card>
    </Container>
  );
};

export default AlertDetail;
