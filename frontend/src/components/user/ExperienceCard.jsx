import React, { useState } from "react";
import { Card, Button, Modal, Form } from "react-bootstrap";
import { FaBriefcase, FaEdit, FaTrash } from "react-icons/fa";

/**
 * @param {string} experience - 유저 경력 목록 (DB에서 가져온 문자열)
 * @param {function} onSaveExperience - 경력 업데이트 함수
 */
const ExperienceCard = ({ experience = "", onSaveExperience }) => {
  const [showModal, setShowModal] = useState(false);
  const [newExperience, setNewExperience] = useState(experience);

  // 🚀 입력값 변경 핸들러
  const handleChange = (e) => {
    setNewExperience(e.target.value);
  };

  // 🚀 경력 저장 핸들러
  const handleSave = () => {
    if (!newExperience.trim()) {
      alert("경력을 입력해주세요.");
      return;
    }
    onSaveExperience(newExperience);
    setShowModal(false);
  };

  // 🚀 경력 삭제 핸들러
  const handleDelete = () => {
    if (window.confirm("정말 삭제하시겠습니까?")) {
      onSaveExperience(""); // 빈 문자열로 업데이트
    }
  };

  return (
    <Card className="shadow-lg border-0 rounded-4 modern-card">
      <Card.Body className="p-4">
        <h5 className="fw-bold text-secondary mb-3">
          <FaBriefcase className="me-2" /> 경력
        </h5>

        {/* 🚀 현재 저장된 경력 정보 출력 */}
        {experience ? (
          <p>{experience}</p>
        ) : (
          <p className="text-muted">경력 정보가 없습니다.</p>
        )}

        {/* 🚀 버튼 영역 */}
        <div className="d-flex justify-content-end mt-3">
          <Button variant="outline-secondary" size="sm" className="me-2" onClick={() => setShowModal(true)}>
            <FaEdit className="me-1" /> 수정
          </Button>
          <Button variant="outline-danger" size="sm" onClick={handleDelete}>
            <FaTrash className="me-1" /> 삭제
          </Button>
        </div>
      </Card.Body>

      {/* 🚀 경력 수정 모달 */}
      <Modal show={showModal} onHide={() => setShowModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>경력 수정</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>경력</Form.Label>
              <Form.Control
                as="textarea"
                rows={5}
                value={newExperience}
                onChange={handleChange}
                placeholder="경력을 입력하세요..."
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>취소</Button>
          <Button variant="primary" onClick={handleSave}>저장</Button>
        </Modal.Footer>
      </Modal>
    </Card>
  );
};

export default ExperienceCard;
