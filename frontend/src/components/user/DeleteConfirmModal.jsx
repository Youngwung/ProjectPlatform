import React from "react";
import { Modal, Button } from "react-bootstrap";

/**
 * @param {boolean} show - 모달 열림 여부
 * @param {function} onHide - 모달 닫기 함수
 * @param {function} handleDeleteAccount - 계정 삭제 실행 함수
 */
const DeleteConfirmModal = ({ show, onHide, handleDeleteAccount }) => {
  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>계정 삭제</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p className="text-danger fw-bold">이 작업은 되돌릴 수 없습니다.</p>
        <p>정말로 계정을 삭제하시겠습니까?</p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          취소
        </Button>
        <Button variant="danger" onClick={handleDeleteAccount}>
          삭제
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default DeleteConfirmModal;
