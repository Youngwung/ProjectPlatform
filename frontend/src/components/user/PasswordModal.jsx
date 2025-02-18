import React, { useState } from "react";
import { Modal, Form, Button, Alert, Spinner } from "react-bootstrap";
import authApi from "../../api/authApi";

/**
 * PasswordModal 컴포넌트
 * @param {boolean} show - 모달 열림 여부
 * @param {function} onHide - 모달 닫기 함수
 * @param {function} onPasswordChangeSuccess - 비밀번호 변경 성공 시 호출할 콜백
 * @param {number} userId - 유저 ID
 */
const PasswordModal = ({ show, onHide, onPasswordChangeSuccess}) => {
  // step: 1 - 현재 비밀번호 확인, 2 - 새 비밀번호 입력
  const [step, setStep] = useState(1);
  const [password, setPassword] = useState("");       // 현재 비밀번호 (검증 후 그대로 유지)
  const [newPassword, setNewPassword] = useState(""); // 새 비밀번호
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const [loading, setLoading] = useState(false);

  /** 🚀 현재 비밀번호 검증 함수 */
  const handleVerifyPassword = async () => {
    if (!password.trim()) {
      setErrorMsg("현재 비밀번호를 입력해주세요.");
      return;
    }

    setLoading(true);
    try {
      // 서버에 현재 비밀번호 검증 요청 (password 값이 있는지 확인)
      const isValid = await authApi.verifyPassword({ password });
      if (isValid) {
        // 검증 성공하면 단계 전환, 현재 비밀번호 값은 그대로 유지됨
        setStep(2);
        setErrorMsg("");
        // 여기서 현재 비밀번호를 그대로 유지하므로 onChange로 입력된 값이 계속 남아있음
      } else {
        setErrorMsg("현재 비밀번호가 올바르지 않습니다.");
      }
    } catch (error) {
      setErrorMsg("비밀번호 확인 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  /** 🚀 새 비밀번호 변경 요청 함수 */
  const handleChangePassword = async () => {
    if (!newPassword.trim() || !confirmPassword.trim()) {
      setErrorMsg("새 비밀번호를 입력해야 합니다.");
      return;
    }
    if (newPassword !== confirmPassword) {
      setErrorMsg("새 비밀번호와 확인이 일치하지 않습니다.");
      return;
    }
    // if (newPassword.length < 6) {
    //   setErrorMsg("비밀번호는 최소 6자 이상이어야 합니다.");
    //   return;
    // }

    setLoading(true);
    try {
      // 변경 요청 시, 검증된 현재 비밀번호와 새 비밀번호 모두 전송
      await authApi.changePassword(password, newPassword);
      onPasswordChangeSuccess("비밀번호가 성공적으로 변경되었습니다.");
      console.log("실행전");
      handleClose();
      console.log("핸들클로즈 실행");
      alert("비밀번호가 성공적으로 변경되었습니다. 수고람쥐");
    } catch (error) {
      console.error("[DEBUG] handleChangePassword catch 블록 진입:", error);
      setErrorMsg("비밀번호 변경에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  /** 🚀 모달 닫기 및 상태 초기화 함수 */
  const handleClose = () => {
    console.log("[DEBUG] PasswordModal: handleClose 호출됨 - 모달을 닫습니다.");
    setStep(1);
    setPassword("");
    setNewPassword("");
    setConfirmPassword("");
    setErrorMsg("");
    setLoading(false);
    onHide(); // 부모 컴포넌트의 onHide 콜백 호출
    console.log("[DEBUG] PasswordModal: handleClose 실행 완료");
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
                  onChange={(e) => {
                    console.log("현재 비밀번호 입력값:", e.target.value);
                    setPassword(e.target.value);
                  }}
                />
              </Form.Group>
              <Button
                variant="primary"
                className="w-100"
                onClick={handleVerifyPassword}
                disabled={loading}
              >
                {loading ? (
                  <Spinner animation="border" size="sm" className="me-2" />
                ) : (
                  "비밀번호 확인"
                )}
              </Button>
            </>
          ) : (
            <>
              <Alert variant="info">
                현재 비밀번호는 이미 확인되었습니다.
              </Alert>
              <Form.Group className="mb-3">
                <Form.Label>새 비밀번호</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="새 비밀번호 입력"
                  value={newPassword}
                  onChange={(e) => {
                    console.log("새 비밀번호 입력값:", e.target.value);
                    setNewPassword(e.target.value);
                  }}
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
              <Button
                variant="primary"
                className="w-100"
                onClick={handleChangePassword}
                disabled={loading}
              >
                {loading ? (
                  <Spinner animation="border" size="sm" className="me-2" />
                ) : (
                  "비밀번호 변경"
                )}
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
