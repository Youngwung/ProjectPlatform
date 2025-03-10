// ===========================
// File: components/PortfolioModal.jsx
// ===========================
import React from "react";
import { Modal, ListGroup, Button } from "react-bootstrap";

/**
 * @param {boolean} show - 모달 열림 여부
 * @param {function} onHide - 모달 닫기 함수
 */
const PortfolioModal = ({ show, onHide }) => {
  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton className="border-0">
        <Modal.Title className="text-success">포트폴리오 관리</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <ListGroup>
          <ListGroup.Item className="d-flex justify-content-between align-items-center">
            프로젝트 A{" "}
            <Button variant="danger" size="sm">
              삭제
            </Button>
          </ListGroup.Item>
          <ListGroup.Item className="d-flex justify-content-between align-items-center">
            프로젝트 B{" "}
            <Button variant="danger" size="sm">
              삭제
            </Button>
          </ListGroup.Item>
        </ListGroup>
      </Modal.Body>
      <Modal.Footer className="border-0">
        <Button variant="success">새 포트폴리오 추가</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default PortfolioModal;
