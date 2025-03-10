import React, { useContext, useState } from "react";
import {
  Badge,
  Button,
  Dropdown,
  ListGroup,
  OverlayTrigger,
  Tab,
  Tabs,
  Tooltip,
} from "react-bootstrap";
import { FaBell, FaSearch } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { AlertContext } from "../../context/AlertContext";

const AlertBtn = () => {
  const { projectAlerts, portfolioAlerts, markAlertAsRead } = useContext(AlertContext);
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState("projects");

  // 프로젝트/포트폴리오별 읽지 않은 알림 개수
  const unreadProjectCount = projectAlerts.filter((alert) => !alert.isRead).length;
  const unreadPortfolioCount = portfolioAlerts.filter((alert) => !alert.isRead).length;
  // 전체 알림 개수
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

      {/* 드롭다운 메뉴 */}
      <Dropdown.Menu align="end" className="p-3" style={{ minWidth: "320px" }}>
        {/* 탭: 프로젝트, 포트폴리오 */}
        <Tabs activeKey={activeTab} onSelect={(k) => setActiveTab(k)} className="mb-3">
          {/* 프로젝트 탭 */}
          <Tab
            eventKey="projects"
            title={
              <>
                프로젝트 <Badge bg="primary">{projectAlerts.length}</Badge>
              </>
            }
          >
            {projectAlerts.length === 0 ? (
              <p className="text-muted text-center">
                <span>새로운 프로젝트</span> <br />
                <span>관련 알림이 없습니다.</span>
              </p>
            ) : (
              <ListGroup variant="flush">
                {projectAlerts.map((alert) => (
                  <ListGroup.Item
                    key={alert.id}
                    action
                    onClick={() => handleAlertClick(alert.id, true)}
                    className="py-3"
                  >
                    {/* 첫 줄: 상태 배지와 날짜 */}
                    <div className="d-flex justify-content-between align-items-center mb-1">
                      <Badge bg="primary" className="me-2">
                        {alert.status}
                      </Badge>
                      <small className="text-muted">{alert.createdAt}</small>
                    </div>
                    {/* 둘째 줄: 알림 제목 및 내용 보기 툴팁 */}
                    <div>
                      <p style={{ display: "inline-flex", alignItems: "center", whiteSpace: "nowrap" }}>
                        프로젝트 <strong className="me-2">[{alert.projectTitle}]</strong>알림
                        <OverlayTrigger
                          placement="top"
                          overlay={<Tooltip id="tooltip-alert">{alert.content}</Tooltip>}
                        >
                          <span
                            style={{ textDecoration: "underline", cursor: "pointer", marginLeft: "0.5rem" }}
                            onClick={(e) => e.stopPropagation()} // 클릭 시 상세 이동 방지
                          >
                            <FaSearch />
                          </span>
                        </OverlayTrigger>
                      </p>
                    </div>
                  </ListGroup.Item>
                ))}
              </ListGroup>
            )}
          </Tab>

          {/* 포트폴리오 탭 */}
          <Tab
            eventKey="portfolio"
            title={
              <>
                포트폴리오 <Badge bg="success">{portfolioAlerts.length}</Badge>
              </>
            }
          >
            {portfolioAlerts.length === 0 ? (
              <p className="text-muted text-center">새로운 포트폴리오 알림이 없습니다.</p>
            ) : (
              <ListGroup variant="flush">
                {portfolioAlerts.map((alert) => (
                  <ListGroup.Item
                    key={alert.id}
                    action
                    onClick={() => handleAlertClick(alert.id, false)}
                    className="py-3"
                  >
                    <div className="d-flex justify-content-between align-items-center mb-1">
                      <Badge bg="success" className="me-2">
                        {alert.status}
                      </Badge>
                      <small className="text-muted">{alert.createdAt}</small>
                    </div>
                    <div>
                      {alert.content}{" "}
                      <OverlayTrigger
                        placement="top"
                        overlay={
                          <Tooltip id="tooltip-alert">
                            {alert.content}
                          </Tooltip>
                        }
                      >
                        <span
                          style={{ textDecoration: "underline", cursor: "pointer" }}
                          onClick={(e) => e.stopPropagation()}
                        >
                          (내용 보기)
                        </span>
                      </OverlayTrigger>
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
          <Button variant="link" onClick={() => navigate("/mypage/alert")}>
            알람 페이지 바로가기
          </Button>
        </div>
      </Dropdown.Menu>
    </Dropdown>
  );
};

export default AlertBtn;
