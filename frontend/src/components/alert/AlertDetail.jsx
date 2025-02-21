import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Container, Card, Badge, Button, Spinner, Row, Col } from "react-bootstrap";
import { HiOutlineMailOpen,HiOutlineMail } from "react-icons/hi";
import alertApi from "../../api/alertApi";

const AlertDetail = ({ isProject }) => {
  const { alertId } = useParams();
  const navigate = useNavigate();
  const [alert, setAlert] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAlertDetail = async () => {
      if (!alertId) return;
      setLoading(true);
      const data = await alertApi.getOneAlert(alertId, isProject);
      console.log(data);
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
                <h6 className="fw-bold"> 보낸 사람 : {alert.senderName || "알 수 없음"}</h6>
                <h6 className="fw-bold"> 받는 사람 : {alert.receiverName || "알 수 없음"}</h6>
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
                {alert.isRead ? <HiOutlineMailOpen size={20} /> : "📨 안 읽음"}
                </Button>
            </Col>
        </Row>
          <hr />
            <h6 className="fw-bold">{alert.content}</h6>
          <hr />
          {isProject && alert.project ? (
            <>
              <h6 className="fw-bold">📂 프로젝트 정보</h6>
              <p>
                <strong>프로젝트명:</strong> {alert.project.title} <br />
                <strong>설명:</strong> {alert.project.description}
              </p>
            </>
          ) : !isProject && alert.portfolio ? (
            <>
              <h6 className="fw-bold">📁 포트폴리오 정보</h6>
              <p>
                <strong>포트폴리오명:</strong> {alert.portfolio.name} <br />
                <strong>설명:</strong> {alert.portfolio.description}
              </p>
            </>
          ) : null}

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
