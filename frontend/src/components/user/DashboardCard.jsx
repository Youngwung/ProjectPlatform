import React, { useState } from "react";
import { Card, Tabs, Tab, ListGroup, Badge, Button } from "react-bootstrap";
import { FaClipboardList } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

/**
 * @param {array} bookmarkProjectList - 신청한 프로젝트 목록
 * @param {array} bookmarkPortfolioList - 내 포트폴리오 목록
 */
const DashboardCard = ({ bookmarkProjectList = [], bookmarkPortfolioList = [],onDeleteBookmarkProjectList,onDeleteBookmarkPortfolioList}) => {
  const [activeTab, setActiveTab] = useState("projects");
  const navigate = useNavigate()
  return (
    <Card className="shadow-lg border-0 rounded-4 modern-card position-relative h-100">
      <Card.Body className="p-4 d-flex flex-column">
        <h5 className="fw-bold text-secondary mb-3">
          <FaClipboardList className="me-2" /> 북마크
        </h5>
        {/* 스크롤이 가능한 콘텐츠 영역 */}
        <div style={{ flex: 1, overflowY: "auto", maxHeight: "300px", paddingRight: "5px" }}>
          <Tabs activeKey={activeTab} onSelect={(k) => setActiveTab(k)} className="mb-3">
            {/* 신청 프로젝트 목록 탭 */}
            <Tab eventKey="projects"
                 title={
                        <>
                           북마크 프로젝트 목록{" "}
                           <Badge bg="primary">{bookmarkProjectList.length}</Badge>
                        </>
                    }
            >
              {bookmarkProjectList.length === 0 ? (
                <p>북마크한 프로젝트가 없습니다.</p>
              ) : (
                <ListGroup>
                  {bookmarkProjectList.map((bookmark) => (
                    <ListGroup.Item 
                      key={bookmark.id} 
                      className="d-flex justify-content-between"
                      style={{ cursor: "pointer" }}
                      onClick={()=> navigate(`/project/read/${bookmark.projectId}`)}
                    >
                      <span>{bookmark.title}</span>
                      <Button 
                        variant="danger" 
                        size="sm" 
                        onClick={(e) => {
                          onDeleteBookmarkProjectList(bookmark.id)
                          e.stopPropagation()
                          }}
                      >
                        삭제
                      </Button>
                    </ListGroup.Item>
                  ))}
                </ListGroup>
              )}
            </Tab>
            {/* 내 포트폴리오 목록 탭 */}
            <Tab eventKey="portfolio"
                title={
                    <>
                        북마크 포트폴리오 목록{" "}
                        <Badge bg="primary">{bookmarkPortfolioList.length}</Badge>
                    </>
                }
            >
              {bookmarkPortfolioList.length === 0 ? (
                <p>북마크한 포트폴리오가 없습니다.</p>
              ) : (
                <ListGroup>
                  {bookmarkPortfolioList.map((bookmark) => (
                    <ListGroup.Item
                      key={bookmark.id}
                      className="d-flex justify-content-between"
                      onClick={()=> navigate(`/portfolio/list/${bookmark.id}`)}
                      style={{ cursor: "pointer" }}
                    >
                      <span>{bookmark.title}</span>
                      <Button 
                        variant="danger"
                        size="sm" 
                        onClick={(e) => {
                          onDeleteBookmarkPortfolioList(bookmark.id)
                          e.stopPropagation()
                          }}
                        >
                        삭제
                      </Button>
                    </ListGroup.Item>
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
