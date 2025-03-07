// ===========================
// File: components/NotificationModal.jsx
// ===========================
import React, { useState } from "react";
import { Modal, Button, Tabs, Tab, ListGroup } from "react-bootstrap";

/**
 * @param {boolean} show - 모달 열림 여부
 * @param {function} onHide - 모달 닫기 함수
 * @param {array} portfolioNotifications - 포트폴리오 관련 알림 목록
 * @param {array} projectNotifications - 프로젝트 관련 알림 목록
 */
const NotificationModal = ({
  show,
  onHide,
  portfolioNotifications = [],
  projectNotifications = []
}) => {
  // 탭 상태: 기본은 'portfolio' 탭으로
  const [activeKey, setActiveKey] = useState("portfolio");

  // 상위 5개만 보여주려면 .slice(0,5) 사용
  const portfolioTopFive = portfolioNotifications.slice(0, 5);
  const projectTopFive = projectNotifications.slice(0, 5);

  // "전체 보기" 버튼 클릭 시 동작. 예: 알림 전체 페이지로 이동 or 콘솔출력
  const handleViewAllPortfolio = () => {
    //console.log("포트폴리오 알림 전체보기");
  };

  const handleViewAllProject = () => {
    //console.log("프로젝트 알림 전체보기");
  };

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>알림</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Tabs
          id="notifications-tabs"
          activeKey={activeKey}
          onSelect={(k) => setActiveKey(k || "portfolio")}
          className="mb-3"
        >
          <Tab eventKey="portfolio" title="포트폴리오">
            {/* 포트폴리오 알림 목록 */}
            {portfolioTopFive.length === 0 ? (
              <p>포트폴리오 관련 알림이 없습니다.</p>
            ) : (
              <ListGroup>
                {portfolioTopFive.map((item) => (
                  <ListGroup.Item key={item.id}>
                    {item.message}
                  </ListGroup.Item>
                ))}
              </ListGroup>
            )}
            {/* 전체보기 버튼 */}
            {portfolioNotifications.length > 5 && (
              <div className="mt-3 d-flex justify-content-end">
                <Button variant="outline-primary" size="sm" onClick={handleViewAllPortfolio}>
                  전체 보기
                </Button>
              </div>
            )}
          </Tab>

          <Tab eventKey="project" title="프로젝트">
            {/* 프로젝트 알림 목록 */}
            {projectTopFive.length === 0 ? (
              <p>프로젝트 관련 알림이 없습니다.</p>
            ) : (
              <ListGroup>
                {projectTopFive.map((item) => (
                  <ListGroup.Item key={item.id}>
                    {item.message}
                  </ListGroup.Item>
                ))}
              </ListGroup>
            )}
            {/* 전체보기 버튼 */}
            {projectNotifications.length > 5 && (
              <div className="mt-3 d-flex justify-content-end">
                <Button variant="outline-success" size="sm" onClick={handleViewAllProject}>
                  전체 보기
                </Button>
              </div>
            )}
          </Tab>
        </Tabs>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          닫기
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default NotificationModal;
