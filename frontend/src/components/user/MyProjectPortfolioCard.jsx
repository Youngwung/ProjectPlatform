import React, { useState, useEffect } from "react";
// react-bootstrap에서 필요한 컴포넌트들을 임포트
import { Card, Tabs, Tab, Alert, Spinner, ListGroup } from "react-bootstrap";
// 내 프로젝트 데이터를 불러오는 API 함수 (프로젝트 API 모듈 경로에 맞게 수정)
import { getMyProjects } from "../../api/projectApi";
// 내 포트폴리오 데이터를 불러오는 API 모듈 (포트폴리오 API)
import portfolioApi from "../../api/portfolioApi";

/**
 * MyProjectPortfolioCard 컴포넌트
 * - 내 프로젝트와 내 포트폴리오 데이터를 탭으로 나누어 표시함
 * - AlertCard와 유사한 UI 스타일 적용 (카드 그림자, 둥근 모서리, 패딩 등)
 */
const MyProjectPortfolioCard = () => {
  // 현재 선택된 탭 상태 ("project" 또는 "portfolio")
  const [activeTab, setActiveTab] = useState("project");

  // 내 프로젝트 데이터 저장 상태
  const [projectList, setProjectList] = useState([]);
  // 내 포트폴리오 데이터 저장 상태
  const [portfolioList, setPortfolioList] = useState([]);

  // 데이터 로딩 상태 (로딩 중 스피너 표시용)
  const [loading, setLoading] = useState(false);
  // 에러 메시지 저장 상태
  const [errorMessage, setErrorMessage] = useState("");

  /**
   * 내 프로젝트 데이터를 불러오는 함수
   */
  const loadProjects = async () => {
    // 로딩 시작 및 에러 메시지 초기화
    setLoading(true);
    setErrorMessage("");
    try {
      // API 호출하여 내 프로젝트 데이터를 가져옴
      const data = await getMyProjects();
      console.log("✅ 내 프로젝트 데이터:", data);
      // 가져온 데이터를 상태에 저장
      setProjectList(data);
    } catch (error) {
      console.error("❌ 내 프로젝트 데이터를 불러오는데 실패:", error);
      // 에러 발생 시 에러 메시지 설정
      setErrorMessage("내 프로젝트를 불러오는데 실패했습니다.");
    }
    // 로딩 종료
    setLoading(false);
  };

  /**
   * 내 포트폴리오 데이터를 불러오는 함수
   */
  const loadPortfolios = async () => {
    // 로딩 시작 및 에러 메시지 초기화
    setLoading(true);
    setErrorMessage("");
    try {
      // API 호출하여 내 포트폴리오 데이터를 가져옴
      const data = await portfolioApi.getMyPortfolios();
      console.log("✅ 내 포트폴리오 데이터:", data);
      // 가져온 데이터를 상태에 저장
      setPortfolioList(data);
    } catch (error) {
      console.error("❌ 내 포트폴리오 데이터를 불러오는데 실패:", error);
      // 에러 발생 시 에러 메시지 설정
      setErrorMessage("내 포트폴리오를 불러오는데 실패했습니다.");
    }
    // 로딩 종료
    setLoading(false);
  };

  /**
   * activeTab 상태가 변경될 때마다 해당 탭에 맞는 데이터를 불러옴
   */
  useEffect(() => {
    if (activeTab === "project") {
      loadProjects();
    } else if (activeTab === "portfolio") {
      loadPortfolios();
    }
  }, [activeTab]);

  return (
    <Card className="shadow-lg border-0 rounded-4 modern-card mt-4">
      <Card.Body className="p-4">
        {/* 제목 */}
        <h5 className="fw-bold text-secondary mb-3">내 프로젝트 & 포트폴리오</h5>

        {/* 탭 메뉴 */}
        <Tabs
          activeKey={activeTab}               // 현재 선택된 탭 지정
          onSelect={(k) => setActiveTab(k)}    // 탭 선택 시 activeTab 상태 업데이트
          className="mb-3"
        >
          <Tab eventKey="project" title="내 프로젝트" />
          <Tab eventKey="portfolio" title="내 포트폴리오" />
        </Tabs>

        {/* 로딩 중 스피너 표시 */}
        {loading && (
          <div className="text-center">
            <Spinner animation="border" role="status" />
            <span className="visually-hidden">Loading...</span>
          </div>
        )}
        {/* 에러 메시지 표시 */}
        {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}

        {/* 내 프로젝트 탭 콘텐츠 */}
        {!loading && !errorMessage && activeTab === "project" && (
          <div>
            {projectList.length > 0 ? (
              <ListGroup>
                {projectList.map((project) => (
                  <ListGroup.Item key={project.id}>
                    <h6 className="mb-0">{project.title}</h6>
                    {/* 추가 프로젝트 정보를 원하면 확장 가능 */}
                  </ListGroup.Item>
                ))}
              </ListGroup>
            ) : (
              <p>등록된 프로젝트가 없습니다.</p>
            )}
          </div>
        )}

        {/* 내 포트폴리오 탭 콘텐츠 */}
        {!loading && !errorMessage && activeTab === "portfolio" && (
          <div>
            {portfolioList.length > 0 ? (
              <ListGroup>
                {portfolioList.map((portfolio) => (
                  <ListGroup.Item key={portfolio.id}>
                    <h6 className="mb-0">{portfolio.title}</h6>
                    {/* 추가 포트폴리오 정보를 원하면 확장 가능 */}
                  </ListGroup.Item>
                ))}
              </ListGroup>
            ) : (
              <p>등록된 포트폴리오가 없습니다.</p>
            )}
          </div>
        )}
      </Card.Body>
    </Card>
  );
};

export default MyProjectPortfolioCard;
