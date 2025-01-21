import React, { useState } from "react";
import { Container, Form, Button } from "react-bootstrap";
import { Outlet, useNavigate, useLocation } from "react-router-dom";

const FindProject = () => {
  const [searchTerm, setSearchTerm] = useState(""); // 검색어 상태 추가
  const navigate = useNavigate();
  const location = useLocation(); // 현재 경로 가져오기

  // 특정 경로에서 헤더와 검색창/버튼 숨기기
  const isListOrDetail = 
                        location.pathname.includes("/findProject/list") || 
                        location.pathname.includes("/findProject/detail");

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
            <Button variant="primary" onClick={() => navigate("/findProject/add")}>
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

export default FindProject;
