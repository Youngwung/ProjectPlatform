import React, { useState, useEffect } from "react";
import { Container, Row, Col, Alert } from "react-bootstrap";
import authApi from "../../api/authApi";
import { getUserBookmarkProjectList } from "../../api/bookmarkProjectApi";

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
  const [projectBookmarkList, setProjectBookmarkList] = useState([]);
  const [portfolioBookmarkList, setPortfoiloProjectBookmarkList] = useState([]);
  const [portfolioNotifications, setPortfolioNotifications] = useState([
    { id: 3, message: "새 포트폴리오가 등록되었습니다." },
    { id: 4, message: "포트폴리오 수정이 완료되었습니다." },
  ]);

  // ✅ 북마크 프로젝트 목록 가져오는 함수
  const handleBookmarkProjectList = async () => {
    try {
      const data = await getUserBookmarkProjectList(); // API 호출
      console.log("✅ 북마크된 프로젝트 리스트:", data);
      setProjectBookmarkList(data); // 상태 업데이트
    } catch (error) {
      console.error("❌ 북마크된 프로젝트 목록을 가져오는 데 실패했습니다:", error);
      setAlertMessage("북마크된 프로젝트를 불러오는데 실패했습니다.");
      setAlertVariant("danger");
    }
  };

  useEffect(() => {
    // ✅ 유저 정보 가져오기
    authApi.getAuthenticatedUser(1)
      .then((data) => {
        console.log("✅ 부모 컴포넌트에서 받은 user 값:", data);
        setUser(data);
      })
      .catch(() => setAlertMessage("유저 정보를 불러오는데 실패했습니다."));

    // ✅ 북마크 프로젝트 목록 가져오기
    handleBookmarkProjectList();
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
        <Col md={6}>{user && <UserInfoCard user={user} />}</Col>
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
          {user && (
            <ExperienceCard
              experience={user.experience}
              onSaveExperience={(newExperience) => {
                setUser((prevUser) => ({
                  ...prevUser,
                  experience: newExperience,
                }));

                authApi.updateUserExperience({ experience: newExperience })
                  .then(() => {
                    setAlertMessage("경력이 성공적으로 업데이트되었습니다.");
                    setAlertVariant("success");
                  })
                  .catch((error) => {
                    console.error("❌ 경력 업데이트 실패:", error);
                    setAlertMessage("경력 업데이트에 실패했습니다.");
                    setAlertVariant("danger");
                  });
              }}
            />
          )}
        </Col>
      </Row>

      {/* 알림 정보 */}
      <Row>
        <Col md={12}>
          <NotificationCard
            projectBookmarkList={projectBookmarkList} // ✅ 수정된 변수명
            portfolioNotifications={portfolioNotifications}
          />
        </Col>
      </Row>
    </Container>
  );
};

export default MyPageTotalInfo;
