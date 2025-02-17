import React, { useState } from "react";
import { Card, Tabs, Tab, ListGroup, Badge } from "react-bootstrap";
import { FaClipboardList} from "react-icons/fa";

/**
 * @param {array} projectList - 신청한 프로젝트 목록
 * @param {array} portfolioList - 내 포트폴리오 목록
 */
const DashboardCard = ({ projectList = [], portfolioList = [] }) => {
  // 현재 선택된 탭 (기본값: 프로젝트 목록)
  const [activeTab, setActiveTab] = useState("projects");

  return (
    <Card className="shadow-lg border-0 rounded-4 modern-card position-relative h-100">
      <Card.Body className="p-4 d-flex flex-column">
        <h5 className="fw-bold text-secondary mb-3">
          <FaClipboardList className="me-2" /> 대시보드
        </h5>

        {/* 스크롤이 가능한 콘텐츠 영역 */}
        <div style={{ flex: 1, overflowY: "auto", maxHeight: "300px", paddingRight: "5px" }}>
          <Tabs activeKey={activeTab} onSelect={(k) => setActiveTab(k)} className="mb-3">
            {/* 신청 프로젝트 목록 탭 */}
            <Tab eventKey="projects"
                 title={
                        <>
                           신청 프로젝트 목록{" "}
                           <Badge bg="primary">{projectList.length}</Badge>
                        </>
                    }
            >
              {projectList.length === 0 ? (
                <p>신청한 프로젝트가 없습니다.</p>
              ) : (
                <ListGroup>
                  {projectList.map((project) => (
                    <ListGroup.Item key={project.id}>{project.name}</ListGroup.Item>
                  ))}
                </ListGroup>
              )}
            </Tab>

            {/* 내 포트폴리오 목록 탭 */}
            <Tab eventKey="portfolio"
                title={
                    <>
                        신청 포트폴리오 목록{" "}
                        <Badge bg="primary">{portfolioList.length}</Badge>
                    </>
                }
            >
              {portfolioList.length === 0 ? (
                <p>포트폴리오가 없습니다.</p>
              ) : (
                <ListGroup>
                  {portfolioList.map((portfolio) => (
                    <ListGroup.Item key={portfolio.id}>{portfolio.title}</ListGroup.Item>
                  ))}
                </ListGroup>
              )}
            </Tab>
          </Tabs>
        </div>
      </Card.Body>
    </Card>
  );
};

export default DashboardCard;