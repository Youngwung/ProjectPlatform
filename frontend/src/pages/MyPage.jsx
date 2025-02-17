import React, { useState } from "react";
import { Container, Row, Col, Nav } from "react-bootstrap";
import { useNavigate, Outlet } from "react-router-dom";

import EditInfoModal from "../components/user/EditInfoModal";
import PasswordModal from "../components/user/PasswordModal";
import DeleteConfirmModal from "../components/user/DeleteConfirmModal";

const MyPage = () => {
  const navigate = useNavigate();

  // 모달 상태 관리
  const [showEditInfoModal, setShowEditInfoModal] = useState(false);
  const [showPasswordModal, setShowPasswordModal] = useState(false);
  const [showDeleteConfirmModal, setShowDeleteConfirmModal] = useState(false);

  return (
    <Container fluid className="mt-4">
      <Row>
        {/* 사이드바 */}
        <Col md={3} lg={2} className="bg-light p-3 border-end">
          <h5 className="fw-bold">Quick Controller</h5>
          <Nav className="flex-column">
            <Nav.Link onClick={() => navigate("/mypage")}>마이페이지</Nav.Link>
            <Nav.Link onClick={() => navigate("/mypage/alert")}>알람</Nav.Link>
          </Nav>

          <h5 className="fw-bold mt-3">Quick Modal Controller</h5>
          <Nav className="flex-column">
            <Nav.Link onClick={() => setShowEditInfoModal(true)}>내 정보 수정</Nav.Link>
            <Nav.Link onClick={() => setShowPasswordModal(true)}>비밀번호 변경</Nav.Link>
            <Nav.Link onClick={() => setShowDeleteConfirmModal(true)}>계정 탈퇴</Nav.Link>
          </Nav>
        </Col>

        {/* Outlet을 통해 동적 콘텐츠 변경 */}
        <Col md={9} lg={10}>
          <Outlet />
        </Col>
      </Row>

      {/* 모달들 */}
      <EditInfoModal show={showEditInfoModal} onHide={() => setShowEditInfoModal(false)} />
      <PasswordModal show={showPasswordModal} onHide={() => setShowPasswordModal(false)} />
      <DeleteConfirmModal show={showDeleteConfirmModal} onHide={() => setShowDeleteConfirmModal(false)} />
    </Container>
  );
};

export default MyPage;
