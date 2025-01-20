import React, { useState, useEffect } from "react";
import { Card, Col, Container, Row } from "react-bootstrap";
import { useOutletContext, useNavigate } from "react-router-dom";
import { Pagination } from "react-bootstrap";

const ListPage = () => {
  const [portfolios, setPortfolios] = useState([]); // 전체 포트폴리오 데이터
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 상태
  const portfoliosPerPage = 12; // 페이지당 포트폴리오 수
  const { searchTerm } = useOutletContext(); // Outlet에서 검색어 받아오기
  const navigate = useNavigate();

  // 검색 및 페이지 처리
  const indexOfLastPortfolio = currentPage * portfoliosPerPage;
  const indexOfFirstPortfolio = indexOfLastPortfolio - portfoliosPerPage;
  const filteredPortfolios = portfolios.filter((portfolio) =>
    portfolio.title.toLowerCase().includes(searchTerm.toLowerCase())
  );
  const currentPortfolios = filteredPortfolios.slice(
    indexOfFirstPortfolio,
    indexOfLastPortfolio
  );
  const totalPages = Math.ceil(filteredPortfolios.length / portfoliosPerPage);

  // 페이지 변경 처리
  const handlePageChange = (pageNumber) => setCurrentPage(pageNumber);

  // 더미 데이터 생성
  useEffect(() => {
    const fetchData = async () => {
      const dummyData = Array.from({ length: 50 }, (_, index) => ({
        id: index + 1,
        title: `포트폴리오 ${index + 1}`,
        description: `포트폴리오 설명 ${index + 1}`,
        skills: ["React", "Bootstrap", "MySQL"],
        github_url: `https://github.com/project-${index + 1}`,
        image_url: "https://via.placeholder.com/150",
      }));
      setPortfolios(dummyData);
    };
    fetchData();
  }, []);

  return (
    <Container>
      <Row>
        {currentPortfolios.map((portfolio) => (
          <Col md={3} className="mb-4" key={portfolio.id}>
            <Card
              onClick={() => navigate(`/findProject/detail/${portfolio.id}`)}
              style={{ cursor: "pointer" }}
            >
              <Card.Img variant="top" src={portfolio.image_url} />
              <Card.Body>
                <Card.Title>{portfolio.title}</Card.Title>
                <Card.Text>{portfolio.description}</Card.Text>
                <Card.Text>
                  <strong>기술:</strong> {portfolio.skills.join(", ")}
                </Card.Text>
                <Card.Link href={portfolio.github_url} target="_blank">
                  GitHub 링크
                </Card.Link>
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
