import React, { useState } from "react";
import { Card, Tabs, Tab, ListGroup, Badge } from "react-bootstrap";
import { FaBell } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import alertApi from "../../api/alertApi"; // ✅ API 호출 추가

/**
 * @param {array} projectAlerts - 프로젝트 관련 알림 목록
 * @param {array} portfolioAlerts - 포트폴리오 관련 알림 목록
 */
const AlertCard = ({ projectAlerts = [], portfolioAlerts = [] }) => {
  const [activeTab, setActiveTab] = useState("projects");
  const navigate = useNavigate(); // ✅ 페이지 이동을 위한 Hook

  /**
   * 🔹 알림 클릭 시 상세 페이지로 이동 & 읽음 처리
   */
  const handleAlertClick = async (alertId, isProject) => {
    try {
      // ✅ 읽음 처리 API 호출
      await alertApi.markAlertAsRead(alertId, isProject);

      // ✅ 알림 상세 페이지로 이동 (예: /alert/1)
      navigate(`/alert/${alertId}`);
    } catch (error) {
      console.error("❌ 알림 읽음 처리 실패:", error);
    }
  };

  return (
    <Card className="shadow-lg border-0 rounded-4 modern-card">
      <Card.Body className="p-4">
        <h5 className="fw-bold text-secondary mb-3">
          <FaBell className="me-2" /> 최근 알림
        </h5>

        {/* 알림 탭 */}
        <Tabs activeKey={activeTab} onSelect={(k) => setActiveTab(k)} className="mb-3">
          {/* 프로젝트 알림 탭 */}
          <Tab eventKey="projects" title="프로젝트 알림">
            {projectAlerts.length === 0 ? (
              <p>프로젝트 관련 알림이 없습니다.</p>
            ) : (
              <ListGroup>
                {projectAlerts.map((alert) => (
                  <ListGroup.Item
                    key={alert.id}
                    action
                    onClick={() => handleAlertClick(alert.id, true)} // ✅ 클릭 시 상세 페이지 이동
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
          <Tab eventKey="portfolio" title="포트폴리오 알림">
            {portfolioAlerts.length === 0 ? (
              <p>포트폴리오 관련 알림이 없습니다.</p>
            ) : (
              <ListGroup>
                {portfolioAlerts.map((alert) => (
                  <ListGroup.Item
                    key={alert.id}
                    action
                    onClick={() => handleAlertClick(alert.id, false)} // ✅ 클릭 시 상세 페이지 이동
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
      </Card.Body>
    </Card>
  );
};

export default AlertCard;
