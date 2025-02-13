import React from "react";
import { Card, Button, ListGroup, Badge } from "react-bootstrap";
import { FaUserEdit, FaLock, FaBriefcase } from "react-icons/fa";

/**
 * @param {object} user - 사용자 정보
 * @param {function} onOpenPasswordModal - 비밀번호 변경 모달 열기
 * @param {function} onOpenPortfolioModal - 포트폴리오 관리 모달 열기
 * @param {function} onOpenEditInfoModal - 내 정보 수정 모달 열기
 */
const UserInfoCard = ({
  user,
  onOpenPasswordModal,
  onOpenPortfolioModal,
  onOpenEditInfoModal
}) => {
  return (
    <Card className="shadow-lg border-0 rounded-4 modern-card position-relative">
      <Card.Body className="p-4">
        {/* 헤더 */}
        <div className="d-flex align-items-center mb-3">
          <FaUserEdit size={28} className="text-primary me-2" />
          <Card.Title className="fs-4 fw-bold text-primary text-uppercase">
            내 정보
          </Card.Title>
        </div>

        {/* 사용자 정보 표시 */}
        <ListGroup variant="flush" className="mb-3">
          <ListGroup.Item>
            <strong>이름:</strong> {user.name}
          </ListGroup.Item>
          <ListGroup.Item>
            <strong>아이디(이메일):</strong> {user.email}
          </ListGroup.Item>
          <ListGroup.Item>
            <strong>프로바이더:</strong>{" "}
            {user.providerName ? user.providerName : "없음"}
          </ListGroup.Item>
          <ListGroup.Item>
            <strong>전화번호:</strong> {user.phoneNumber}
          </ListGroup.Item>
          <ListGroup.Item>
            <strong>기술 스택:</strong>{" "}
            {user.techStack &&
              user.techStack.map((tech, index) => (
                <Badge key={index} bg="primary" className="me-1">
                  {tech}
                </Badge>
              ))}
          </ListGroup.Item>
          <ListGroup.Item>
            <strong>사용자 링크:</strong>{" "}
            {user.links ? user.links : "사용자의 링크가 없습니다."}
          </ListGroup.Item>
        </ListGroup>

        {/* 하단 버튼: 비밀번호 변경/포트폴리오 관리 */}
        <div className="d-flex">
          <Button
            variant="outline-primary"
            className="rounded-pill px-4"
            onClick={onOpenPasswordModal}
          >
            <FaLock className="me-2" /> 비밀번호 변경
          </Button>
        </div>
      </Card.Body>

      {/* 카드 우측 상단 수정 버튼 */}
      <Button
        variant="secondary"
        className="position-absolute top-0 end-0 m-3"
        onClick={onOpenEditInfoModal}
      >
        <FaUserEdit className="me-1" /> 수정
      </Button>
    </Card>
  );
};

export default UserInfoCard;
