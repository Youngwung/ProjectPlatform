import React, { useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import { Outlet, useLocation, useNavigate } from "react-router-dom";

const IndexPage = () => {
  const [searchTerm, setSearchTerm] = useState(""); // 검색어 상태 추가
  const navigate = useNavigate();
  const location = useLocation(); // 현재 경로 가져오기

  // 특정 경로에서 헤더와 검색창/버튼 숨기기
  const isListOrDetail = 
                        location.pathname.includes("/portfolio/list") || 
                        location.pathname.includes("/portfolio/detail");

  return (
    <Container className="mt-4">
      {/* 특정 페이지에서 숨기도록 조건부 렌더링 */}
      {isListOrDetail && (
        <>
          <h1 className="text-center mb-4">포트폴리오 목록</h1>
          <div className="d-flex justify-content-between mb-4">
            <Form.Control
              type="text"
              placeholder="검색어를 입력하세요"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              style={{ maxWidth: "300px" }}
            />
            <Button variant="primary" onClick={() => navigate("/portfolio/create")}>
              글 작성
            </Button>
          </div>
        </>
      )}

      {/* 검색어 상태 전달 */}
      <Outlet context={{ searchTerm }} />
    </Container>
  );
};

export default IndexPage;
