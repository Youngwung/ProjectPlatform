import React, { useState } from "react";
import { Card, Tabs, Tab, ListGroup, Button } from "react-bootstrap";
import { FaBell, FaChevronRight } from "react-icons/fa";

/**
 * @param {array} projectNotifications - 프로젝트 관련 알림 목록
 * @param {array} portfolioNotifications - 포트폴리오 관련 알림 목록
 */
const NotificationCard = ({ projectNotifications = [], portfolioNotifications = [] }) => {
  // 현재 선택된 탭 (기본값: 프로젝트 알림)
  const [activeTab, setActiveTab] = useState("projects");

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
            {projectNotifications.length === 0 ? (
              <p>프로젝트 관련 알림이 없습니다.</p>
            ) : (
              <ListGroup>
                {projectNotifications.map((notif) => (
                  <ListGroup.Item key={notif.id}>{notif.message}</ListGroup.Item>
                ))}
              </ListGroup>
            )}
          </Tab>

          {/* 포트폴리오 알림 탭 */}
          <Tab eventKey="portfolio" title="포트폴리오 알림">
            {portfolioNotifications.length === 0 ? (
              <p>포트폴리오 관련 알림이 없습니다.</p>
            ) : (
              <ListGroup>
                {portfolioNotifications.map((notif) => (
                  <ListGroup.Item key={notif.id}>{notif.message}</ListGroup.Item>
                ))}
              </ListGroup>
            )}
          </Tab>
        </Tabs>

        {/* 전체 보기 버튼 */}
        <div className="d-flex justify-content-end mt-3">
          <Button 
            variant="outline-primary" 
            size="sm" 
            onClick={() => alert("준비 중입니다.")}>
            전체 보기 <FaChevronRight className="ms-1" />
          </Button>
        </div>
      </Card.Body>
    </Card>
  );
};

export default NotificationCard;
