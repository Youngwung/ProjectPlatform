// ===========================
// File: components/PasswordModal.jsx
// ===========================
import React, { useState } from "react";
import { Modal, Form, Button, Alert } from "react-bootstrap";

/**
 * @param {boolean} show - 모달 열림 여부
 * @param {function} onHide - 모달 닫기 함수
 * @param {function} onPasswordChangeSuccess - 비밀번호 변경 성공 시 호출할 콜백
 */
const PasswordModal = ({ show, onHide, onPasswordChangeSuccess, userId, userApi }) => {
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMsg, setErrorMsg] = useState("");

  const handleSubmit = async () => {
    // 간단한 검증
    if (!currentPassword.trim() || !newPassword.trim() || !confirmPassword.trim()) {
      setErrorMsg("모든 필드를 입력해야 합니다.");
      return;
    }
    if (newPassword !== confirmPassword) {
      setErrorMsg("새 비밀번호와 확인이 일치하지 않습니다.");
      return;
    }
    if (newPassword.length < 6) {
      setErrorMsg("비밀번호는 최소 6자 이상이어야 합니다.");
      return;
    }

    // 실제 비밀번호 변경 API 호출 (예시)
    try {
      await userApi.changePassword(userId, {
        currentPassword,
        newPassword
      });
      // 성공 시 부모 콜백 호출 -> Alert 표시, 모달 닫기
      onPasswordChangeSuccess("비밀번호가 성공적으로 변경되었습니다.");
      setErrorMsg("");
      setCurrentPassword("");
      setNewPassword("");
      setConfirmPassword("");
      onHide();
    } catch (error) {
      console.error("비밀번호 변경 오류:", error);
      setErrorMsg("비밀번호 변경에 실패했습니다. 현재 비밀번호가 틀렸거나 오류가 발생했습니다.");
    }
  };

  const handleClose = () => {
    // 닫기 시 입력값 초기화
    setCurrentPassword("");
    setNewPassword("");
    setConfirmPassword("");
    setErrorMsg("");
    onHide();
  };

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton className="border-0">
        <Modal.Title className="text-primary">비밀번호 변경</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {errorMsg && <Alert variant="danger">{errorMsg}</Alert>}
        <Form>
          <Form.Group className="mb-3">
            <Form.Label>현재 비밀번호</Form.Label>
            <Form.Control
              type="password"
              placeholder="현재 비밀번호 입력"
              value={currentPassword}
              onChange={(e) => setCurrentPassword(e.target.value)}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>새 비밀번호</Form.Label>
            <Form.Control
              type="password"
              placeholder="새 비밀번호 입력"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>새 비밀번호 확인</Form.Label>
            <Form.Control
              type="password"
              placeholder="비밀번호 확인"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer className="border-0">
        <Button variant="secondary" onClick={handleClose}>
          취소
        </Button>
        <Button variant="primary" onClick={handleSubmit}>
          저장
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default PasswordModal;
