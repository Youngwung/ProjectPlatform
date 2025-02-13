import React, { useState, useEffect } from "react";
import { Container, Row, Col, Tabs, Tab, Alert, Nav } from "react-bootstrap";
import { useNavigate,Outlet } from "react-router-dom";
import userApi from "../api/userApi";

import UserInfoCard from "../components/user/UserInfoCard";
import DashboardCard from "../components/user/DashboardCard";
import PasswordModal from "../components/user/PasswordModal";
import EditInfoModal from "../components/user/EditInfoModal";
import PortfolioModal from "../components/user/PortfolioModal";
import SettingModal from "../components/user/SettingModal";
import NotificationModal from "../components/user/NotificationModal";
import NotificationCard from "../components/user/NotificationCard";
import ExperienceCard from "../components/user/ExperienceCard";
import DeleteConfirmModal from "../components/user/DeleteConfirmModal";

const MyPage = () => {
  const navigate = useNavigate();
  // 모달 상태
  const [showPasswordModal, setShowPasswordModal] = useState(false);
  const [showPortfolioModal, setShowPortfolioModal] = useState(false);
  const [showSettingModal, setShowSettingModal] = useState(false);
  const [showEditInfoModal, setShowEditInfoModal] = useState(false);
  const [showNotificationModal, setShowNotificationModal] = useState(false);
  const [showDeleteConfirmModal, setShowDeleteConfirmModal] = useState(false);
  // 사용자 정보 상태
  const [user, setUser] = useState({
    id: null,
    name: "",
    email: "",
    phoneNumber: "",
    experience: "",
    links: "",
    providerId: null,
    providerName: "",
    techStack: [],
    techStackStr: "",
  });

  const [editUser, setEditUser] = useState({ ...user });

  // 프로젝트 알림 & 포트폴리오 알림
  const [projectNotifications, setProjectNotifications] = useState([
    { id: 1, message: "프로젝트 A가 승인되었습니다." },
    { id: 2, message: "새 프로젝트 신청이 접수되었습니다." },
    { id: 3, message: "프로젝트 B가 마감되었습니다." },
  ]);
  
  const [portfolioNotifications, setPortfolioNotifications] = useState([
    { id: 4, message: "새 포트폴리오가 등록되었습니다." },
    { id: 5, message: "포트폴리오가 조회되었습니다." },
    { id: 6, message: "포트폴리오 수정이 완료되었습니다." },
  ]);

  // Alert 메시지 상태
  const [alertMessage, setAlertMessage] = useState("");
  const [alertVariant, setAlertVariant] = useState("success");

  // 사용자 정보 가져오기
  useEffect(() => {
    userApi.getUserById(1)
      .then((data) => {
        setUser(data);
        setEditUser(data);
      })
      .catch(() => setAlertMessage("유저 정보를 불러오는데 실패했습니다."));
  }, []);
  const handleSaveExperience = (updatedExperience) => {
    setUser((prevUser) => ({ ...prevUser, experience: updatedExperience }));
    userApi.updateUser(user.id, { ...user, experience: updatedExperience })
      .then(() => alert("경력이 업데이트되었습니다."))
      .catch(() => alert("경력 업데이트 실패"));
  };

  // Alert 메시지 닫기
  const closeAlert = () => setAlertMessage("");

  return (
    <Container fluid className="mt-4">
      <Row>
        {/* 사이드바 */}
        <Col md={3} lg={2} className="bg-light p-3 border-end">
          <h5 className="fw-bold">Quick Controller</h5>
          <Nav className="flex-column">
            <Nav.Link onClick={()=>navigate('/mypage')}>마이페이지</Nav.Link>
            <Nav.Link onClick={()=>navigate('/mypage/alert')}>알람</Nav.Link>
          </Nav>
          <h5 className="fw-bold">Quick Modal Controller</h5>
          <Nav className="flex-column">
            <Nav.Link onClick={() => setShowEditInfoModal(true)}>내 정보 수정</Nav.Link>
            {/* <Nav.Link onClick={() => setShowPortfolioModal(true)}>navigate('/alertproject')</Nav.Link> */}
            <Nav.Link onClick={() => setShowSettingModal(true)}>비밀번호 변경</Nav.Link>
            <Nav.Link onClick={()=> setShowDeleteConfirmModal(true)}>계정 탈퇴</Nav.Link>
          </Nav>
        </Col>
          <Col md={9} lg={10}>
            <h1>유저 정보</h1>
          {alertMessage && (
            <Alert variant={alertVariant} dismissible onClose={closeAlert}>
              {alertMessage}
            </Alert>
          )}

          {/* userinfo & 대시보드 */}
          <Row className="mb-4">
            <Col md={6}>
              <UserInfoCard user={user} onOpenEditModal={() => setShowEditInfoModal(true)} />
            </Col>
            <Col md={6}>
              {/* 신청 프로젝트 목록 & 내 포트폴리오 목록 */}
              <DashboardCard 
                projectCount={3} 
                alarmCount={portfolioNotifications.length} 
                projectLabel="신청 프로젝트 목록" 
                alarmLabel="내 포트폴리오 목록"
              />
            </Col>
          </Row>

         {/* 🚀 경력 카드 추가 */}
         <Row className="mb-4">
            <Col md={12}>
              <ExperienceCard experience={user.experience} onSaveExperience={handleSaveExperience} />
            </Col>
          </Row>

          {/* 알림 카드 추가 */}
          <Row className="mb-4">
            <Col md={12}>
              <NotificationCard 
                projectNotifications={projectNotifications} 
                portfolioNotifications={portfolioNotifications}
              />
            </Col>
          </Row>
        </Col>        
      </Row>

      {/* 모달들 */}
      <PasswordModal show={showPasswordModal} onHide={() => setShowPasswordModal(false)} />
      <EditInfoModal show={showEditInfoModal} onHide={() => setShowEditInfoModal(false)} />
      <PortfolioModal show={showPortfolioModal} onHide={() => setShowPortfolioModal(false)} />
      <SettingModal show={showSettingModal} onHide={() => setShowSettingModal(false)} />
      <NotificationModal show={showNotificationModal} onHide={() => setShowNotificationModal(false)} notifications={projectNotifications} />
      <DeleteConfirmModal show={showDeleteConfirmModal} onHide={()=> setShowDeleteConfirmModal(false)}/>
         {/* 🚀 Outlet을 통해 동적 콘텐츠 변경 */}
         <Col md={9} lg={10}>
          <Outlet />
        </Col>
    </Container>
  );
};

export default MyPage;
