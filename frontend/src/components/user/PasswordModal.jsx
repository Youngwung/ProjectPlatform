import React, { useState } from "react";
import { Modal, Form, Button, Alert, Spinner } from "react-bootstrap";

/**
 * @param {boolean} show - 모달 열림 여부
 * @param {function} onHide - 모달 닫기 함수
 * @param {function} onPasswordChangeSuccess - 비밀번호 변경 성공 시 호출할 콜백
 * @param {number} userId - 유저 ID
 * @param {object} userApi - API 요청 객체
 */
const PasswordModal = ({ show, onHide, onPasswordChangeSuccess, userId, userApi }) => {
  const [step, setStep] = useState(1); // 1: 비밀번호 확인 단계, 2: 새 비밀번호 입력 단계
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const [loading, setLoading] = useState(false);

  /** 🚀 현재 비밀번호 검증 */
  const handleVerifyPassword = async () => {
    if (!currentPassword.trim()) {
      setErrorMsg("현재 비밀번호를 입력해주세요.");
      return;
    }

    setLoading(true);
    try {
      const isValid = await userApi.verifyPassword(userId, { currentPassword });

      if (isValid) {
        setStep(2); // 다음 단계 (새 비밀번호 입력)로 이동
        setErrorMsg("");
      } else {
        setErrorMsg("현재 비밀번호가 올바르지 않습니다.");
      }
    } catch (error) {
      setErrorMsg("비밀번호 확인 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  /** 🚀 새 비밀번호 변경 요청 */
  const handleChangePassword = async () => {
    if (!newPassword.trim() || !confirmPassword.trim()) {
      setErrorMsg("새 비밀번호를 입력해야 합니다.");
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

    setLoading(true);
    try {
      await userApi.changePassword(userId, { currentPassword, newPassword });

      onPasswordChangeSuccess("비밀번호가 성공적으로 변경되었습니다.");
      handleClose();
    } catch (error) {
      setErrorMsg("비밀번호 변경에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  /** 🚀 모달 닫기 */
  const handleClose = () => {
    setStep(1);
    setCurrentPassword("");
    setNewPassword("");
    setConfirmPassword("");
    setErrorMsg("");
    setLoading(false);
    onHide();
  };

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton className="border-0">
        <Modal.Title className="text-primary">
          {step === 1 ? "비밀번호 확인" : "새 비밀번호 입력"}
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {errorMsg && <Alert variant="danger">{errorMsg}</Alert>}
        <Form>
          {step === 1 ? (
            <>
              <Form.Group className="mb-3">
                <Form.Label>현재 비밀번호</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="현재 비밀번호 입력"
                  value={currentPassword}
                  onChange={(e) => setCurrentPassword(e.target.value)}
                />
              </Form.Group>
              <Button variant="primary" className="w-100" onClick={handleVerifyPassword} disabled={loading}>
                {loading ? <Spinner animation="border" size="sm" className="me-2" /> : "비밀번호 확인"}
              </Button>
            </>
          ) : (
            <>
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
              <Button variant="primary" className="w-100" onClick={handleChangePassword} disabled={loading}>
                {loading ? <Spinner animation="border" size="sm" className="me-2" /> : "비밀번호 변경"}
              </Button>
            </>
          )}
        </Form>
      </Modal.Body>
      <Modal.Footer className="border-0">
        <Button variant="secondary" onClick={handleClose}>
          취소
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default PasswordModal;
