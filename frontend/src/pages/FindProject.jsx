import React, { useState, useEffect } from "react";
import { Container, Row, Col, Card, Pagination, Form, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const FindProject = () => {
  const [portfolios, setPortfolios] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [searchTerm, setSearchTerm] = useState("");
  const portfoliosPerPage = 12;
  const navigate = useNavigate();

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

  const indexOfLastPortfolio = currentPage * portfoliosPerPage;
  const indexOfFirstPortfolio = indexOfLastPortfolio - portfoliosPerPage;
  const filteredPortfolios = portfolios.filter((portfolio) =>
    portfolio.title.toLowerCase().includes(searchTerm.toLowerCase())
  );
  const currentPortfolios = filteredPortfolios.slice(indexOfFirstPortfolio, indexOfLastPortfolio);

  const totalPages = Math.ceil(filteredPortfolios.length / portfoliosPerPage);

  const handlePageChange = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <Container className="mt-4">
      <h1 className="text-center mb-4">포트폴리오 목록</h1>
      <div className="d-flex justify-content-between mb-4">
        <Form.Control
          type="text"
          placeholder="검색어를 입력하세요"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          style={{ maxWidth: "300px" }}
        />
        <Button variant="primary" onClick={() => alert("글 작성 페이지로 이동")}>
          글 작성
        </Button>
      </div>
      <Row>
        {currentPortfolios.map((portfolio) => (
          <Col md={3} className="mb-4" key={portfolio.id}>
            <Card onClick={() => navigate(`/findProject/detail/${portfolio.id}`)} style={{ cursor: "pointer" }}>
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

export default FindProject;
