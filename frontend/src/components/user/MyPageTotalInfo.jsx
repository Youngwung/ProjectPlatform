import React, { useState, useEffect } from "react";
import { Container, Row, Col, Alert } from "react-bootstrap";
import authApi from "../../api/authApi";
import { deleteBookmarkProjectOne,
         getUserBookmarkProjectList,
         deleteBookmarkPortfolioOne,
         getUserBookmarkPortfolioList
        } from "../../api/bookmarkProjectApi";

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
  const [portfolioBookmarkList, setPortfoiloBookmarkList] = useState([]);

  // ✅ 북마크 프로젝트 목록 가져오는 함수
  const handleBookmarkProjectList = async () => {
    try {
      const data = await getUserBookmarkProjectList(); // API 호출
      console.log("✅ 북마크된 프로젝트 리스트:", data);

      // 📌 `projectTitle`을 기준으로 목록 업데이트
      const formattedProjects = data.map((item) => ({
        id: item.id,
        projectId : item.projectId,
        title: item.projectTitle, // 프로젝트 제목으로 매핑
      }));

      setProjectBookmarkList(formattedProjects); // 상태 업데이트
    } catch (error) {
      console.error("❌ 북마크된 프로젝트 목록을 가져오는 데 실패했습니다:", error);
      setAlertMessage("북마크된 프로젝트를 불러오는데 실패했습니다.");
      setAlertVariant("danger");
    }
  };
  const handleBookmarkPortfolioList = async () =>{
    try {
      const data = await getUserBookmarkPortfolioList();
      console.log("✅ 북마크된 포폴 리스트:", data);
      const formattedPortfolios = data.map((item) => ({
        id: item.id,
        portfolioId: item.portfolioId,
        title: item.portfolioTitle, // 프로젝트 제목으로 매핑
      }));
      setPortfoiloBookmarkList(formattedPortfolios)
    } catch (error) {
      console.error("❌ 북마크된 포폴 목록을 가져오는 데 실패했습니다:", error);
      setAlertMessage("북마크된 포폴을 불러오는데 실패했습니다.");
      setAlertVariant("danger");
    }

  }
  const handleDeleteBookmarkProject = async (id) => {
    try {
      await deleteBookmarkProjectOne(id); // 서버에서 삭제 요청
      setProjectBookmarkList((prevList) =>
        prevList.filter((project) => project.id !== id) // UI에서 즉시 반영
      );
      setAlertMessage("북마크가 삭제되었습니다.");
      setAlertVariant("success");
    } catch (error) {
      console.error("❌ 북마크 삭제 실패:", error);
      setAlertMessage("북마크 삭제에 실패했습니다.");
      setAlertVariant("danger");
    }
  };
  const handleDeleteBookmarkPortfolio = async (id) => {
    try {
      await deleteBookmarkPortfolioOne(id); // 서버에서 삭제 요청
      setPortfoiloBookmarkList((prevList) =>
        prevList.filter((portfolio) => portfolio.id !== id) // UI에서 즉시 반영
      );
      setAlertMessage("북마크가 삭제되었습니다.");
      setAlertVariant("success");
    } catch (error) {
      console.error("❌ 북마크 삭제 실패:", error);
      setAlertMessage("북마크 삭제에 실패했습니다.");
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
    handleBookmarkPortfolioList();
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
            bookmarkProjectList={projectBookmarkList} // ✅ 데이터 전달 (수정된 projectList)
            onDeleteBookmarkProjectList={handleDeleteBookmarkProject}
            bookmarkPortfolioList={portfolioBookmarkList}
            onDeleteBookmarkPortfolioList={handleDeleteBookmarkPortfolio}
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
            projectBookmarkList={projectBookmarkList}
            portfolioNotifications={[]}
          />
        </Col>
      </Row>
    </Container>
  );
};

export default MyPageTotalInfo;
