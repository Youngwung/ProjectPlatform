import React, { useState } from "react";
import { Modal, Form, Button, Alert, Spinner } from "react-bootstrap";
import authApi from "../../api/authApi";

const PasswordModal = ({ show, onHide, onPasswordChangeSuccess }) => {
  const [step, setStep] = useState(1);
  const [password, setPassword] = useState(""); // 현재 비밀번호
  const [newPassword, setNewPassword] = useState(""); // 새 비밀번호
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const [loading, setLoading] = useState(false);

  /** ✅ 비밀번호 유효성 검사 */
  const validatePassword = (password) => {
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return passwordRegex.test(password);
  };

  /** ✅ 현재 비밀번호 검증 */
  const handleVerifyPassword = async () => {
    if (!password.trim()) {
      setErrorMsg("현재 비밀번호를 입력해주세요.");
      return;
    }

    setLoading(true);
    try {
      const isValid = await authApi.verifyPassword({ password });
      if (isValid) {
        setStep(2);
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

  /** ✅ 새 비밀번호 변경 */
  const handleChangePassword = async () => {
    if (!newPassword.trim() || !confirmPassword.trim()) {
      setErrorMsg("새 비밀번호를 입력해야 합니다.");
      return;
    }

    if (!validatePassword(newPassword)) {
      setErrorMsg("비밀번호는 최소 8자 이상, 숫자, 문자, 특수문자를 포함해야 합니다.");
      return;
    }

    if (newPassword !== confirmPassword) {
      setErrorMsg("새 비밀번호와 확인이 일치하지 않습니다.");
      return;
    }
    setLoading(true);
    try {
      await authApi.changePassword(password, newPassword);
      onPasswordChangeSuccess("비밀번호가 성공적으로 변경되었습니다.");
      handleClose();
      alert("비밀번호가 성공적으로 변경되었습니다.");
    } catch (error) {
      setErrorMsg("비밀번호 변경에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  /** ✅ 모달 닫기 */
  const handleClose = () => {
    setStep(1);
    setPassword("");
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
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </Form.Group>
              <Button
                variant="primary"
                className="w-100"
                onClick={handleVerifyPassword}
                disabled={loading}
              >
                {loading ? <Spinner animation="border" size="sm" className="me-2" /> : "비밀번호 확인"}
              </Button>
            </>
          ) : (
            <>
              <Alert variant="info">비밀번호 확인 완료</Alert>
              <Form.Group className="mb-3">
                <Form.Label>새 비밀번호</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="새 비밀번호 입력"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                />
                {!validatePassword(newPassword) && newPassword.length > 0 && (
                  <small className="text-danger">
                    최소 8자 이상, 숫자, 문자, 특수문자를 포함해야 합니다.
                  </small>
                )}
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
              <Button
                variant="primary"
                className="w-100"
                onClick={handleChangePassword}
                disabled={loading}
              >
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
