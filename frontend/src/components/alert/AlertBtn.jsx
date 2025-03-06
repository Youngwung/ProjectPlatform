import React, { useContext, useState } from "react";
import { Badge, Button, Dropdown, ListGroup, Tab, Tabs } from "react-bootstrap";
import { FaBell } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { AlertContext } from "../../context/AlertContext"; // AlertContext import (경로는 프로젝트에 맞게 조정)

const AlertBtn = () => {
  // Context에서 알림 상태와 읽음 처리 함수를 가져옵니다.
  const { projectAlerts, portfolioAlerts, markAlertAsRead } =
    useContext(AlertContext);
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState("projects");
  // 전체 알림 개수 계산 (프로젝트 + 포트폴리오)
  const unreadProjectCount = projectAlerts.filter(alert => !alert.isRead).length;
  const unreadPortfolioCount = portfolioAlerts.filter(alert => !alert.isRead).length;
  const totalAlerts = unreadProjectCount + unreadPortfolioCount;

  // 알림 클릭 시 읽음 처리 후 상세 페이지로 이동
  const handleAlertClick = async (alertId, isProject) => {
    await markAlertAsRead(alertId, isProject);
    if (isProject) {
      navigate(`/mypage/alert/project/${alertId}`);
    } else {
      navigate(`/mypage/alert/portfolio/${alertId}`);
    }
  };

  return (
    <Dropdown>
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

      {/* 드롭다운 메뉴: 프로젝트와 포트폴리오 탭 */}
      <Dropdown.Menu align="end" className="p-3" style={{ minWidth: "300px" }}>
      <Tabs
          activeKey={activeTab}
          onSelect={(k) => setActiveTab(k)}
          className="mb-3"
        >
          <Tab
            eventKey="projects"
            title={
              <>
                프로젝트{" "}
                <Badge bg="primary">{projectAlerts.length}</Badge>
              </>
            }
          >
            {projectAlerts.length === 0 ? (
              <p className="text-muted text-center"><span>새로운 프로젝트</span> <br/><span>관련 알림이 없습니다.</span></p>
              

            ) : (
              <ListGroup variant="flush">
                {projectAlerts.map((alert) => (
                  <ListGroup.Item
                    key={alert.id}
                    action
                    onClick={() => handleAlertClick(alert.id, true)}
                  >
                    <div className="d-flex justify-content-between align-items-center">
                      <div>
                        <Badge bg="primary" className="me-2">
                          {alert.status}
                        </Badge>
                        {alert.content}
                      </div>
                      <small className="text-muted text-center">{alert.createdAt}</small>
                    </div>
                  </ListGroup.Item>
                ))}
              </ListGroup>
            )}
          </Tab>
          <Tab
            eventKey="portfolio"
            title={
              <>
                포트폴리오{" "}
                <Badge bg="success">{portfolioAlerts.length}</Badge>
              </>
            }
          >
            {portfolioAlerts.length === 0 ? (
              <p className="text-muted">새로운 포트폴리오 알림이 없습니다.</p>
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
                        <Badge bg="success" className="me-2">
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

        {/* 드롭다운 하단: 알람 페이지 바로가기 */}
        <Dropdown.Divider />
        <div className="text-center">
          <Button
            variant="link"
            onClick={() => navigate("/mypage/alert")}
          >
            알람 페이지 바로가기
          </Button>
        </div>
      </Dropdown.Menu>
    </Dropdown>
  );
};

export default AlertBtn;