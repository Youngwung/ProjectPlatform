import React, { useState, useEffect } from "react";
import { Container, Row, Col, Alert } from "react-bootstrap";
import authApi from "../../api/authApi";

import UserInfoCard from "./UserInfoCard";
import DashboardCard from "./DashboardCard";
import ExperienceCard from "./ExperienceCard";
import NotificationCard from "./NotificationCard";

const MyPageTotalInfo = () => {
  // 사용자 정보 상태
  const [user, setUser] = useState(null);
  const [alertMessage, setAlertMessage] = useState("");
  const [alertVariant, setAlertVariant] = useState("success");

  // 프로젝트 & 포트폴리오 알림
  const [projectNotifications, setProjectNotifications] = useState([
    //TODO api로 요청
    { id: 1, message: "프로젝트 A가 승인되었습니다." },
    { id: 2, message: "새 프로젝트 신청이 접수되었습니다." },
  ]);

  const [portfolioNotifications, setPortfolioNotifications] = useState([
    //TODO api로 요청
    { id: 3, message: "새 포트폴리오가 등록되었습니다." },
    { id: 4, message: "포트폴리오 수정이 완료되었습니다." },
  ]);

  useEffect(() => {
    authApi.getAuthenticatedUser(1)
      .then((data) => {
        console.log("✅ 부모 컴포넌트에서 받은 user 값:", data);
        setUser(data);
      })
      .catch(() => setAlertMessage("유저 정보를 불러오는데 실패했습니다."));
  }, []);

  return (
    <Container>
      <h1>유저 정보</h1>
      {alertMessage && (
        <Alert variant={alertVariant} dismissible onClose={() => setAlertMessage("")}>
          {alertMessage}
        </Alert>
      )}

      {/* 유저 정보 & 대시보드 */}
      <Row className="mb-4">
        <Col md={6}>
          {user && <UserInfoCard user={user} />}
        </Col>
        <Col md={6}>
          <DashboardCard
            projectCount={3}
            alarmCount={portfolioNotifications.length}
            projectLabel="신청 프로젝트 목록"
            alarmLabel="내 포트폴리오 목록"
          />
        </Col>
      </Row>

      {/* 경력 정보 */}
      <Row className="mb-4">
        <Col md={12}>
          {user && <ExperienceCard experience={user.experience} />}
        </Col>
      </Row>

      {/* 알림 정보 */}
      <Row>
        <Col md={12}>
          <NotificationCard
            projectNotifications={projectNotifications}
            portfolioNotifications={portfolioNotifications}
          />
        </Col>
      </Row>
    </Container>
  );
};

export default MyPageTotalInfo;
