import React, { useState } from "react";
import { Container, Row, Col, Card, Button, Modal, Form, ListGroup, Badge } from "react-bootstrap";
import { FaUserEdit, FaLock, FaBriefcase, FaBell, FaClipboardList } from "react-icons/fa";

const MyPage = () => {
  const [showPasswordModal, setShowPasswordModal] = useState(false);
  const [showPortfolioModal, setShowPortfolioModal] = useState(false);
  const [showSettingsModal, setShowSettingsModal] = useState(false);
  const [showEditInfoModal, setShowEditInfoModal] = useState(false);

  const [user, setUser] = useState({
    name: "홍길동",
    email: "hong@example.com",
    phoneNumber: "010-1234-5678",
    experience: "3년",
    techStack: ["Java", "Spring Boot", "React", "MySQL", "HTML", "CSS", "JavaScript"],
  });

   // 사용자 정보 수정 핸들러
   const handleChange = (e) => {
    const { name, value } = e.target;
    setUser((prevUser) => ({
      ...prevUser,
      [name]: value,
    }));
  };

  return (
    <Container className="mt-5">
      {/* 메인 유저 정보 카드 */}
      <Row className="justify-content-center">
        <Col md={8} lg={6}>
          <Card className="shadow-lg border-0 rounded-4">
            <Button
                variant="outline-secondary"
                size="sm"
                className="position-absolute top-0 end-0 mt-2 me-2"
                onClick={() => setShowEditInfoModal(true)}
            >
              <FaUserEdit className="me-1" /> 내 정보 수정
            </Button>
            <Card.Body className="p-4">
              <div className="d-flex align-items-center mb-3">
                <FaUserEdit size={28} className="text-primary me-2" />
                <Card.Title className="fs-4 fw-bold text-primary mb-0">내 정보</Card.Title>
              </div>
              <ListGroup variant="flush">
                <ListGroup.Item>
                  <strong>이름:</strong> 홍길동
                </ListGroup.Item>
                <ListGroup.Item>
                  <strong>이메일:</strong> hong@example.com
                </ListGroup.Item>
                <ListGroup.Item>
                  <strong>전화번호:</strong> 010-1234-5678
                </ListGroup.Item>
                <ListGroup.Item>
                  <strong>경력:</strong> 3년
                </ListGroup.Item>
                <ListGroup.Item>
                  <strong>기술 스택:</strong>{" "}
                  <Badge bg="primary" className="me-1">Java</Badge>
                  <Badge bg="secondary" className="me-1">Spring Boot</Badge>
                  <Badge bg="info" className="me-1">React</Badge>
                  <Badge bg="success">MySQL</Badge>
                </ListGroup.Item>
              </ListGroup>
              <div className="d-flex justify-content-between mt-4">
                <Button variant="outline-primary" className="rounded-pill px-4" onClick={() => setShowPasswordModal(true)}>
                  <span><FaLock className="me-2"/> 비밀번호 변경</span>
                </Button>
                <Button variant="outline-success" className="rounded-pill px-4" onClick={() => setShowPortfolioModal(true)}>
                  <span><FaBriefcase className="me-2" /> 포트폴리오 관리</span>
                </Button>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* 대시보드 (신청 프로젝트, 알림 개수) */}
      <Row className="justify-content-center mt-4">
        <Col md={8} lg={6}>
          <Card className="border-0 shadow-sm">
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-center">
                <h5 className="fw-bold text-secondary"><FaClipboardList className="me-2"/> 대시보드</h5>
                <Button variant="outline-dark" size="sm" onClick={() => setShowSettingsModal(true)}>
                  <FaBell className="me-2"/> 설정
                </Button>
              </div>
              <Row className="mt-3">
                <Col className="text-center">
                  <h4 className="fw-bold text-primary">3</h4>
                  <p className="text-muted">신청한 프로젝트</p>
                </Col>
                <Col className="text-center">
                  <h4 className="fw-bold text-danger">2</h4>
                  <p className="text-muted">알림 개수</p>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* 비밀번호 변경 모달 */}
      <Modal show={showPasswordModal} onHide={() => setShowPasswordModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title className="text-primary">비밀번호 변경</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>현재 비밀번호</Form.Label>
              <Form.Control type="password" placeholder="현재 비밀번호 입력" />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>새 비밀번호</Form.Label>
              <Form.Control type="password" placeholder="새 비밀번호 입력" />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>새 비밀번호 확인</Form.Label>
              <Form.Control type="password" placeholder="비밀번호 확인" />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowPasswordModal(false)}>취소</Button>
          <Button variant="primary">저장</Button>
        </Modal.Footer>
      </Modal>
      {/* 정보 수정 모달 */}
      <Modal show={showEditInfoModal} onHide={() => setShowEditInfoModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>내 정보 수정</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>이름</Form.Label>
              <Form.Control
                type="text"
                name="name"
                value={user.name}
                onChange={handleChange}
                placeholder="이름을 입력하세요"
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>이메일</Form.Label>
              <Form.Control
                type="email"
                name="email"
                value={user.email}
                onChange={handleChange}
                placeholder="이메일을 입력하세요"
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>전화번호</Form.Label>
              <Form.Control
                type="text"
                name="phoneNumber"
                value={user.phoneNumber}
                onChange={handleChange}
                placeholder="전화번호를 입력하세요"
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowEditInfoModal(false)}>취소</Button>
          <Button variant="primary" onClick={() => setShowEditInfoModal(false)}>저장</Button>
        </Modal.Footer>
      </Modal>
      {/* 포트폴리오 관리 모달 */}
      <Modal show={showPortfolioModal} onHide={() => setShowPortfolioModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title className="text-success">포트폴리오 관리</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <ListGroup>
            <ListGroup.Item className="d-flex justify-content-between align-items-center">
              프로젝트 A <Button variant="danger" size="sm">삭제</Button>
            </ListGroup.Item>
            <ListGroup.Item className="d-flex justify-content-between align-items-center">
              프로젝트 B <Button variant="danger" size="sm">삭제</Button>
            </ListGroup.Item>
          </ListGroup>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="success">새 포트폴리오 추가</Button>
        </Modal.Footer>
      </Modal>

      {/* 설정 모달 */}
      <Modal show={showSettingsModal} onHide={() => setShowSettingsModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>사용자 설정</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Check type="switch" label="이메일 알림 수신" />
            </Form.Group>
            <Form.Group className="mb-3">
              <Button variant="danger" className="ms-2">계정 삭제</Button>
            </Form.Group>
          </Form>
        </Modal.Body>
      </Modal>
    </Container>
  );
};

export default MyPage;
