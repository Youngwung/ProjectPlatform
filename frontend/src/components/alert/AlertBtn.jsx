import React, { useEffect, useState } from "react";
import { Badge, Button, Dropdown, ListGroup, Tab, Tabs } from "react-bootstrap";
import { FaBell } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import alertApi from "../../api/alertApi";

const AlertBtn = () => {
  // 프로젝트와 포트폴리오 알림 데이터를 저장할 상태
  const [projectAlerts, setProjectAlerts] = useState([]);
  const [portfolioAlerts, setPortfolioAlerts] = useState([]);
  // 현재 활성 탭 (프로젝트: "projects", 포트폴리오: "portfolio")
  const [activeTab, setActiveTab] = useState("projects");
  // 드롭다운 표시 여부
  const [showDropdown, setShowDropdown] = useState(false);
  const navigate = useNavigate();

  // 프로젝트 알림 API 호출 (상위 5개)
  const handleProjectAlerts = async () => {
    try {
      const data = await alertApi.getUnreadProjectAlerts();
      console.log("✅ 프로젝트 알림 리스트:", data);
      const formattedAlerts = data.slice(0, 5).map(alert => ({
        id: alert.id,
        content: alert.content,
        status: alert.status,
        createdAt: new Date(alert.createdAt).toLocaleString(),
        isRead: alert.isRead,
      }));
      setProjectAlerts(formattedAlerts);
    } catch (error) {
      console.error("❌ 프로젝트 알림을 가져오는 데 실패:", error);
      alert(`프로젝트 알림을 가져오는 데 실패했습니다: ${error.message}`);
    }
  };

  // 포트폴리오 알림 API 호출 (상위 5개)
  const handlePortfolioAlerts = async () => {
    try {
      const data = await alertApi.getUnreadPortfolioAlerts();
      console.log("✅ 포트폴리오 알림 리스트:", data);
      const formattedAlerts = data.slice(0, 5).map(alert => ({
        id: alert.id,
        content: alert.content,
        status: alert.status,
        createdAt: new Date(alert.createdAt).toLocaleString(),
        isRead: alert.isRead,
      }));
      setPortfolioAlerts(formattedAlerts);
    } catch (error) {
      console.error("❌ 포트폴리오 알림을 가져오는 데 실패:", error);
      alert(`포트폴리오 알림을 가져오는 데 실패했습니다: ${error.message}`);
    }
  };

  // 컴포넌트 마운트 시 알림 데이터를 가져옵니다.
  useEffect(() => {
    handleProjectAlerts();
    handlePortfolioAlerts();
    // const intervalId = setInterval(() => {
    //   handleProjectAlerts();
    //   handlePortfolioAlerts();
    // }, 30000); // 30,000ms = 30초

    // 컴포넌트 언마운트 시 interval 클리어
    // return () => clearInterval(intervalId);
  }, []);

  // 알림 클릭 시: 읽음 처리 후 상세 페이지로 이동
  const handleAlertClick = async (alertId, isProject) => {
    try {
      await alertApi.markAlertAsRead(alertId, isProject);
      if (isProject) {
        navigate(`mypage/alert/project/${alertId}`);
      } else {
        navigate(`mypage/alert/portfolio/${alertId}`);
      }
    } catch (error) {
      console.error("❌ 알림 읽음 처리 실패:", error);
      alert(`알림 읽음 처리 실패: ${error.message}`);
    }
  };

  // 전체 알림 개수 (프로젝트 + 포트폴리오)
  const totalAlerts = projectAlerts.length + portfolioAlerts.length;

  return (
    <Dropdown show={showDropdown} onToggle={setShowDropdown}>
      {/* 알림 버튼 */}
      <Dropdown.Toggle
        variant="light"
        id="alert-dropdown"
        className="d-flex align-items-center position-relative"
      >
        <FaBell size={20} />
        {totalAlerts > 0 && (
          <Badge
            bg="danger"
            pill
            className="position-absolute top-0 start-100 translate-middle"
          >
            {totalAlerts}
          </Badge>
        )}
      </Dropdown.Toggle>

      {/* 드롭다운 메뉴 - 탭 형태로 프로젝트/포트폴리오 알림 구분 */}
      <Dropdown.Menu align="end" className="p-3" style={{ minWidth: "300px" }}>
        <Tabs activeKey={activeTab} onSelect={(k) => setActiveTab(k)} className="mb-3">
          {/* 프로젝트 알림 탭 */}
          <Tab
            eventKey="projects"
            title={
              <>
                프로젝트 <Badge bg="primary">{projectAlerts.length}</Badge>
              </>
            }
          >
            {projectAlerts.length === 0 ? (
              <p className="text-muted">프로젝트 관련 알림이 없습니다.</p>
            ) : (
              <ListGroup variant="flush">
                {projectAlerts.map((alert) => (
                  <ListGroup.Item
                    key={alert.id}
                    action
                    onClick={() => {
                        setShowDropdown(false);
                        handleAlertClick(alert.id, true)
                    }}
                  >
                    <div className="d-flex justify-content-between align-items-center">
                      <div>
                        <Badge bg={alert.isRead ? "secondary" : "primary"} className="me-2">
                          {alert.status}
                        </Badge>
                        {alert.content}
                      </div>
                      <small className="text-muted">{alert.createdAt}</small>
                    </div>
                  </ListGroup.Item>
                ))}
              </ListGroup>
            )}
          </Tab>

          {/* 포트폴리오 알림 탭 */}
          <Tab
            eventKey="portfolio"
            title={
              <>
                포트폴리오 <Badge bg="success">{portfolioAlerts.length}</Badge>
              </>
            }
          >
            {portfolioAlerts.length === 0 ? (
              <p className="text-muted">포트폴리오 관련 알림이 없습니다.</p>
            ) : (
              <ListGroup variant="flush">
                {portfolioAlerts.map((alert) => (
                  <ListGroup.Item
                    key={alert.id}
                    action
                    onClick={() => handleAlertClick(alert.id, false)}
                  >
                    <div className="d-flex justify-content-between align-items-center">
                      <div>
                        <Badge bg={alert.isRead ? "secondary" : "success"} className="me-2">
                          {alert.status}
                        </Badge>
                        {alert.content}
                      </div>
                      <small className="text-muted">{alert.createdAt}</small>
                    </div>
                  </ListGroup.Item>
                ))}
              </ListGroup>
            )}
          </Tab>
        </Tabs>

        {/* 드롭다운 최하단에 알람 페이지 바로가기 버튼 */}
        <Dropdown.Divider />
        <div className="text-center">
          <Button variant="link" onClick={() => {
             setShowDropdown(false); // 드롭다운 닫기
             navigate("/mypage/alert"); // 알람 페이지로 이동
          }}>
            알람 페이지 바로가기
          </Button>
        </div>
      </Dropdown.Menu>
    </Dropdown>
  );
};

export default AlertBtn;
