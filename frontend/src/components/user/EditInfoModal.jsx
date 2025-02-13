// ===========================
// File: components/EditInfoModal.jsx
// ===========================
import React from "react";
import { Modal, Button, Form } from "react-bootstrap";

/**
 * @param {boolean} show - 모달 열림 여부
 * @param {function} onHide - 모달 닫기 함수
 * @param {object} editUser - 수정 중인 사용자 정보
 * @param {function} handleChange - 수정 정보 입력 시 핸들러
 * @param {function} handleSaveUserInfo - 수정 정보 저장(백엔드 요청) 함수
 */
const EditInfoModal = ({
  show,
  onHide,
  editUser = {},
  handleChange,
  handleSaveUserInfo
}) => {
  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton className="border-0">
        <Modal.Title>내 정보 수정</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          {/* 전화번호: 수정 가능 */}
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
          
          {/* 사용자 링크(깃허브, 블로그 등) */}
          <Form.Group className="mb-3">
            <Form.Label>링크</Form.Label>
            <Form.Control
              type="text"
              name="links"
              value={editUser.links || ""}
              onChange={handleChange}
              placeholder="예: https://github.com/username"
            />
          </Form.Group>

          {/* 기술 스택 입력. 간단 문자열(예: #react:초급, #node:중급) */}
          <Form.Group className="mb-3">
            <Form.Label>기술 스택</Form.Label>
            <Form.Control
              type="text"
              name="techStackStr"
              // 주의: 실제 데이터는 배열이지만 간단히 문자열로 관리할 수도 있음
              value={editUser.techStackStr || ""}
              onChange={handleChange}
              placeholder="#react:초급, #node:중급, ..."
            />
            <Form.Text className="text-muted">
              쉼표 혹은 해시태그로 구분하여 입력해보세요.
            </Form.Text>
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer className="border-0">
        <Button variant="secondary" onClick={onHide}>
          취소
        </Button>
        <Button variant="primary" onClick={handleSaveUserInfo}>
          저장
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default EditInfoModal;
