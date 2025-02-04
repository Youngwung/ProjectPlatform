import React, { useState, useEffect } from "react";
import { Container, Row, Col, Card, Button, Modal, Form, ListGroup, Badge } from "react-bootstrap";
import { FaUserEdit, FaLock, FaBriefcase, FaBell, FaClipboardList } from "react-icons/fa";
import userApi from "../api/userApi"; // userApi 파일 경로에 맞게 수정

const MyPage = () => {
  // 모달 상태들
  const [showPasswordModal, setShowPasswordModal] = useState(false);
  const [showPortfolioModal, setShowPortfolioModal] = useState(false);
  const [showSettingsModal, setShowSettingsModal] = useState(false);
  const [showEditInfoModal, setShowEditInfoModal] = useState(false);

  const [user, setUser] = useState({
    id: null,
    name: "",
    email: "",
    phoneNumber: "",
    experience: "",
    techStack: []
  });
  // 모달에서 편집할 별도의 상태 (원본과 분리)
  const [editUser, setEditUser] = useState({ ...user });

  // 컴포넌트 마운트 시, 백엔드에서 사용자 정보를 가져옵니다.
  useEffect(() => {
    // 예시: id가 1인 사용자 정보를 불러옴 (실제 서비스에서는 인증 정보를 활용)
    userApi.getUserById(1)
      .then(data => {
        setUser(data);
        setEditUser(data); // 모달에 표시할 데이터에도 반영
        console.log("유저 정보:", data);
      })
      .catch(error => {
        console.error("유저 정보를 가져오는 중 오류 발생:", error);
        window.alert("유저 정보를 불러오는데 실패했습니다.");
      });
  }, []);

  // 메인 페이지에서 입력값 변경 시 (모달에서는 별도 처리)
  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditUser(prevUser => ({
      ...prevUser,
      [name]: value,
    }));
  };

  // "내 정보 수정" 모달 열기: 모달에 원본 데이터 복사
  const handleOpenEditModal = () => {
    setEditUser(user);
    setShowEditInfoModal(true);
  };

  // "내 정보 수정" 모달에서 저장 버튼 클릭 시 호출되는 함수  
  // 수정된 데이터를 백엔드로 PUT 요청하여 업데이트합니다.
  const handleSaveUserInfo = async () => {
    // 간단한 입력값 검증: 이름과 이메일은 필수, 이메일 형식 체크
    if (!editUser.name.trim() || !editUser.email.trim()) {
      window.alert("이름과 이메일은 필수 입력 사항입니다.");
      return;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(editUser.email)) {
      window.alert("올바른 이메일 형식을 입력하세요.");
      return;
    }
    
    try {
      const updatedUser = await userApi.updateUser(editUser.id, editUser);
      setUser(updatedUser); // 원본 데이터 갱신
      window.alert("유저 정보 업데이트에 성공했습니다.");
      setShowEditInfoModal(false);
    } catch (error) {
      console.error("유저 업데이트 오류:", error);
      window.alert("유저 정보 업데이트에 실패했습니다.");
    }
  };

  // 로딩 상태 (user.id가 null인 경우)
  if (user.id === null) {
    return <div>Loading...</div>;
  }

  const handleDeleteUser = async () => {
    try {
      await userApi.deleteUser(3);
      window.alert("계정 삭제에 성공했습니다.");
      window.location.href = "/";
    } catch (error) {
      console.error("계정 삭제 오류:", error);
      window.alert("계정 삭제에 실패했습니다.");
    }
  }

  return (
    <Container className="mt-5 px-4">
      {/* 메인 유저 정보 카드 */}
      <Row className="justify-content-center">
        <Col md={8} lg={6}>
          <Card className="shadow-lg border-0 rounded-4 modern-card position-relative">
            <Card.Body className="p-4">
              <div className="d-flex align-items-center mb-3">
                <FaUserEdit size={28} className="text-primary me-2" />
                <Card.Title className="fs-4 fw-bold text-primary text-uppercase">내 정보</Card.Title>
              </div>
              <ListGroup variant="flush" className="mb-3">
                <ListGroup.Item><strong>이름:</strong> {user.name}</ListGroup.Item>
                <ListGroup.Item><strong>이메일:</strong> {user.email}</ListGroup.Item>
                <ListGroup.Item><strong>전화번호:</strong> {user.phoneNumber}</ListGroup.Item>
                <ListGroup.Item><strong>경력:</strong> {user.experience}</ListGroup.Item>
                <ListGroup.Item>
                  <strong>기술 스택:</strong>{" "}
                  {user.techStack && user.techStack.map((tech, index) => (
                    <Badge key={index} bg="primary" className="me-1">{tech}</Badge>
                  ))}
                </ListGroup.Item>
              </ListGroup>
              <div className="d-flex justify-content-between">
                <Button variant="outline-primary" className="rounded-pill px-4" onClick={() => setShowPasswordModal(true)}>
                  <FaLock className="me-2" /> 비밀번호 변경
                </Button>
                <Button variant="outline-success" className="rounded-pill px-4" onClick={() => setShowPortfolioModal(true)}>
                  <FaBriefcase className="me-2" /> 포트폴리오 관리
                </Button>
              </div>
            </Card.Body>
            {/* 카드 우측 상단 수정 버튼 */}
            <Button variant="secondary" className="position-absolute top-0 end-0 m-3" onClick={handleOpenEditModal}>
              <FaUserEdit className="me-1" /> 수정
            </Button>
          </Card>
        </Col>
      </Row>

      {/* 대시보드 카드 */}
      <Row className="justify-content-center mt-4">
        <Col md={8} lg={6}>
          <Card className="shadow-sm border-0 modern-card">
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-center mb-3">
                <h5 className="fw-bold text-secondary">
                  <FaClipboardList className="me-2" /> 대시보드
                </h5>
                <Button variant="outline-dark" size="sm" onClick={() => setShowSettingsModal(true)}>
                  <FaBell className="me-2" /> 설정
                </Button>
              </div>
              <Row>
                <Col className="text-center">
                  <h4 className="fw-bold text-primary">3</h4>
                  <p className="text-muted mb-0">신청한 프로젝트</p>
                </Col>
                <Col className="text-center">
                  <h4 className="fw-bold text-danger">2</h4>
                  <p className="text-muted mb-0">알림 개수</p>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* 비밀번호 변경 모달 */}
      <Modal show={showPasswordModal} onHide={() => setShowPasswordModal(false)} centered>
        <Modal.Header closeButton className="border-0">
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
        <Modal.Footer className="border-0">
          <Button variant="secondary" onClick={() => setShowPasswordModal(false)}>취소</Button>
          <Button variant="primary">저장</Button>
        </Modal.Footer>
      </Modal>

      {/* 내 정보 수정 모달 */}
      <Modal show={showEditInfoModal} onHide={() => setShowEditInfoModal(false)} centered>
        <Modal.Header closeButton className="border-0">
          <Modal.Title>내 정보 수정</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>이름</Form.Label>
              <Form.Control
                type="text"
                name="name"
                value={editUser.name || ""}
                onChange={handleChange}
                placeholder="이름을 입력하세요"
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>이메일</Form.Label>
              <Form.Control
                type="email"
                name="email"
                value={editUser.email || ""}
                onChange={handleChange}
                placeholder="이메일을 입력하세요"
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>전화번호</Form.Label>
              <Form.Control
                type="text"
                name="phoneNumber"
                value={editUser.phoneNumber || ""}
                onChange={handleChange}
                placeholder="전화번호를 입력하세요"
              />
            </Form.Group>
            {/* 추가 정보(예: 경력 등)는 필요에 따라 추가 */}
          </Form>
        </Modal.Body>
        <Modal.Footer className="border-0">
          <Button variant="secondary" onClick={() => setShowEditInfoModal(false)}>취소</Button>
          <Button variant="primary" onClick={handleSaveUserInfo}>저장</Button>
        </Modal.Footer>
      </Modal>

      {/* 포트폴리오 관리 모달 */}
      <Modal show={showPortfolioModal} onHide={() => setShowPortfolioModal(false)} centered>
        <Modal.Header closeButton className="border-0">
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
        <Modal.Footer className="border-0">
          <Button variant="success">새 포트폴리오 추가</Button>
        </Modal.Footer>
      </Modal>

      {/* 설정 모달 */}
      <Modal show={showSettingsModal} onHide={() => setShowSettingsModal(false)} centered>
        <Modal.Header closeButton className="border-0">
          <Modal.Title>사용자 설정</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleDeleteUser}>
            <Form.Group className="mb-3">
              <Form.Check type="switch" label="이메일 알림 수신" />
            </Form.Group>
            <Form.Group className="mb-3">
              <Button variant="danger" className="ms-2" onClick={handleDeleteUser}>계정 삭제</Button>
            </Form.Group>
          </Form>
        </Modal.Body>
      </Modal>
    </Container>
  );
};

export default MyPage;
