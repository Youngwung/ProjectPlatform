import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Container, Card, Badge, Button, Spinner, Row, Col } from "react-bootstrap";
import { HiOutlineMailOpen, HiOutlineMail } from "react-icons/hi";
import alertApi from "../../api/alertApi";

const AlertDetail = ({ isProject }) => {
  const { alertId } = useParams();
  const navigate = useNavigate();
  const [alert, setAlert] = useState(null);
  const [loading, setLoading] = useState(true);

  // alert 상세 정보만 API로 가져옴 (백엔드에서 isMyProject가 세팅되어 있음)
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

  const handleMarkAsRead = async () => {
    if (!alert || alert.isRead) return;
    await alertApi.markAlertAsRead(alert.id, isProject);
    setAlert((prev) => ({ ...prev, isRead: true }));
  };

  // isMyProject에 따라 신청/초대 API를 분기하여 호출
  const handleAccept = async () => {
    if (!alert || !alert.project) return;
    if (alert.isMyProject) {
      // 내프로젝트인 경우 → 참가 신청 알림 API 호출
      await alertApi.acceptApplication(alert.project.id, alert.applicantId);
      console.log("신청 수락 처리 완료");
    } else {
      // 내프로젝트가 아닌 경우 → 초대 알림 API 호출
      await alertApi.acceptInvite(alert.project.id, alert.inviteId);
      console.log("초대 수락 처리 완료");
    }
  };

  const handleReject = async () => {
    if (!alert || !alert.project) return;
    if (alert.isMyProject) {
      await alertApi.rejectApplication(alert.project.id, alert.applicantId);
      console.log("신청 거절 처리 완료");
    } else {
      await alertApi.rejectInvite(alert.project.id, alert.inviteId);
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
          <Row className="mb-3">
            <Col md={8}>
              <h6 className="fw-bold">보낸 사람: {alert.senderName || "알 수 없음"}</h6>
              <h6 className="fw-bold">받는 사람: {alert.receiverName || "알 수 없음"}</h6>
              <p className="text-muted">{new Date(alert.createdAt).toLocaleString()}</p>
            </Col>
            <Col md={4} className="text-end">
              <Badge bg="info" className="px-3 py-2">{alert.status}</Badge>
              <Button
                variant="outline-secondary"
                size="sm"
                onClick={handleMarkAsRead}
                disabled={alert.isRead}
              >
                {alert.isRead ? <HiOutlineMailOpen size={20} /> : <HiOutlineMail size={20} />}
              </Button>
            </Col>
          </Row>
          <hr />
          <h6 className="fw-bold py-4 fs-2">{alert.content}</h6>
          <hr />

          {/* 조건별 UI 분기 */}
          {isProject && alert.project && (
            <>
              {alert.isMyProject ? (
                // 내프로젝트인 경우 (내 프로젝트에 대한 알림)
                alert.type === "참가알림" ? (
                  // 참가 알림: 다른 유저가 내 프로젝트에 참가 신청 → 신청자 정보 표시
                  <>
                    <h6 className="fw-bold">👤 신청자 정보</h6>
                    <p>
                      <strong>이름:</strong> {alert.applicant?.name || "알 수 없음"} <br />
                      <strong>이메일:</strong> {alert.applicant?.email || "알 수 없음"}
                      {/* TODO 기술스택 등등 */}
                    </p>
                  </>
                ) : (
                  // 초대 알림: 내 프로젝트에 대해 내가 다른 유저를 초대 → 추가 정보 없이 content만 표시
                  <p className="text-muted">추가 신청자 정보가 없습니다.</p>
                )
              ) : (
                // 내프로젝트가 아닌 경우
                alert.type === "초대알림" ? (
                  // 초대 알림: 내가 다른 유저에게 프로젝트 초대를 보낸 경우 → 프로젝트 정보 표시
                  <>
                    <h6 className="fw-bold">📂 프로젝트 정보</h6>
                    <p>
                      <strong>프로젝트명:</strong> {alert.project.title} <br />
                      <strong>설명:</strong> {alert.project.description}
                    </p>
                  </>
                ) : null
              )}

              {(alert.inviteId || alert.applicantId) && (
                <div className="text-center mt-4">
                  <Button variant="success" className="px-4" onClick={handleAccept}>
                    {alert.isMyProject && alert.type === "참가알림" ? "신청 수락" : "초대 수락"}
                  </Button>
                  <Button variant="danger" className="ms-3 px-4" onClick={handleReject}>
                    {alert.isMyProject && alert.type === "참가알림" ? "신청 거절" : "초대 거절"}
                  </Button>
                </div>
              )}
            </>
          )}

          <div className="text-center mt-4">
            <Button variant="success" className="px-4" onClick={() => navigate("/mypage/alert")}>
              🔙 뒤로 가기
            </Button>
            <Button
              variant="primary"
              className="ms-3 px-4"
              onClick={() => navigate(isProject ? `/project/${alert.project?.id}` : `/portfolio/${alert.portfolio?.id}`)}
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
