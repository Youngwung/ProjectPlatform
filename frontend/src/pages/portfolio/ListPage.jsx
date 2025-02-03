import React, { useState, useEffect } from "react";
import { Card, Col, Container, Row } from "react-bootstrap";
import { useOutletContext, useNavigate } from "react-router-dom";
import { Pagination } from "react-bootstrap";
import portfolioApi from "../../api/portfolioApi";

const ListPage = () => {
  const [portfolios, setportfolios] = useState([]); // 전체 포트폴리오 데이터
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 상태
  const portfolioPerPage = 12; // 페이지당 포트폴리오 수
  const { searchTerm } = useOutletContext(); // Outlet에서 검색어 받아오기
  const navigate = useNavigate();

  // 페이지 계산
  const indexOfLastportfolio = currentPage * portfolioPerPage;
  const indexOfFirstportfolio = indexOfLastportfolio - portfolioPerPage;

  // 검색 및 페이지 처리
  const filteredportfolio = portfolios.filter((portfolio) =>
    portfolio.title.toLowerCase().includes(searchTerm.toLowerCase())
  );
  const currentportfolio = filteredportfolio.slice(
    indexOfFirstportfolio,
    indexOfLastportfolio
  );
  const totalPages = Math.ceil(filteredportfolio.length / portfolioPerPage);

  // 페이지 변경 처리
  const handlePageChange = (pageNumber) => setCurrentPage(pageNumber);
  
  const portfolioInit={
    id: 0,
    userId: 0,
    title: "",
    description: "",
    skills: "",
    imageUrl: "",
    createAt: "",
    updateAt: "",
    links: "",
  }
  // 전체 프로젝트 가져오기
  useEffect(() => {
    const fetchAllportfolios = async () => {
      try {
        const response = await portfolioApi.getAllProjects(); // getAllProjects 호출
        console.log("전체 프로젝트 조회 성공:", response);
        setportfolios(response);
      } catch (error) {
        console.error("전체 프로젝트 조회 실패:", error);
      }
    };
    fetchAllportfolios();
  }, []);

  return (
    <Container>
      <Row>
        {currentportfolio.map((portfolio) => (
          <Col md={3} className="mb-4" key={portfolio.id}>
            <Card
              onClick={() => navigate(`/portfolio/list/${portfolio.id}`)}
              style={{ cursor: "pointer" }}
            >
              <Card.Img variant="top" src={portfolio.image_url} />
              <Card.Body>
                <Card.Title>{portfolio.title}</Card.Title>
                <Card.Text>
                  <strong>작성자:</strong> {portfolio.userId}
                </Card.Text>
                <Card.Text>{portfolio.description}</Card.Text>
                <Card.Text>
                  <strong>기술스택:</strong> {portfolio.skills}
                </Card.Text>
                <Card.Text>
                  {/* TODO 링크 user.id 로 link정보 가져오는 api호출 */}
                  <strong>링크 :</strong> 
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
      <Pagination className="justify-content-center">
        {Array.from({ length: totalPages }, (_, index) => (
          <Pagination.Item
            key={index + 1}
            active={currentPage === index + 1}
            onClick={() => handlePageChange(index + 1)}
          >
            {index + 1}
          </Pagination.Item>
        ))}
      </Pagination>
    </Container>
  );
};

export default ListPage;
