import React, { useEffect, useState } from "react";
import { Alert, Card, Col, Container, Pagination, Row, Spinner } from "react-bootstrap";
import { useNavigate, useOutletContext } from "react-router-dom";
import portfolioApi from "../../api/portfolioApi";
import SkillTagComponent from "../../components/skill/SkillTagComponent";

const ListPage = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [nodata, setNodata] = useState(false); // ✅ 상태 변수 추가
  const [portfolios, setPortfolios] = useState([]); // 전체 포트폴리오 데이터
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 상태
  const portfolioPerPage = 12; // 페이지당 포트폴리오 수
  const { searchTerm } = useOutletContext(); // Outlet에서 검색어 받아오기

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
        setLoading(true);
        const response = await portfolioApi.getAllProjects();
        console.log("✅ 전체 프로젝트 조회 성공:", response);

        if (response.length === 0) {
          setNodata(true); // ✅ nodata 상태를 true로 변경
        } else {
          setPortfolios(response);
          setNodata(false); // ✅ 데이터가 있으면 false 설정
        }
      } catch (error) {
        console.error("❌ 전체 프로젝트 조회 실패:", error);
        setError("데이터를 불러오는 중 오류가 발생했습니다.");
      } finally {
        setLoading(false);
        console.log(portfolios);
      }
    };

    fetchAllportfolios();
  }, []);

  if (loading) {
    return (
      <Container className="text-center mt-4">
        <Spinner animation="border" variant="primary" />
        <p>로딩 중...</p>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="text-center mt-4">
        <Alert variant="danger">{error}</Alert>
      </Container>
    );
  }

  if (nodata) {
    return (
      <Container className="text-center mt-4">
        <Alert variant="warning">포트폴리오 데이터가 없습니다.</Alert>
      </Container>
    );
  }

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
                  <SkillTagComponent skills={portfolio.skills} />
                </Card.Text>
                <Card.Text>
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
