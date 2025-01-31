import React, { useState, useEffect } from "react";
import { Card, Col, Container, Row } from "react-bootstrap";
import { useOutletContext, useNavigate } from "react-router-dom";
import { Pagination } from "react-bootstrap";
import findProjectApi from "../../api/findProjectApi";

const ListPage = () => {
  const [findProjects, setfindProjects] = useState([]); // 전체 포트폴리오 데이터
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 상태
  const findProjectPerPage = 12; // 페이지당 포트폴리오 수
  const { searchTerm } = useOutletContext(); // Outlet에서 검색어 받아오기
  const navigate = useNavigate();

  // 페이지 계산
  const indexOfLastfindProject = currentPage * findProjectPerPage;
  const indexOfFirstfindProject = indexOfLastfindProject - findProjectPerPage;

  // 검색 및 페이지 처리
  const filteredfindProject = findProjects.filter((findProject) =>
    findProject.title.toLowerCase().includes(searchTerm.toLowerCase())
  );
  const currentfindProject = filteredfindProject.slice(
    indexOfFirstfindProject,
    indexOfLastfindProject
  );
  const totalPages = Math.ceil(filteredfindProject.length / findProjectPerPage);

  // 페이지 변경 처리
  const handlePageChange = (pageNumber) => setCurrentPage(pageNumber);
  
  const findProjectInit={
    id: 0,
    userId: 0,
    title: "",
    description: "",
    skills: "",
    imageUrl: "",
    createAt: "",
    updateAt: "",
  }
  // 전체 프로젝트 가져오기
  useEffect(() => {
    const fetchAllFindProjects = async () => {
      try {
        const response = await findProjectApi.getAllProjects(); // getAllProjects 호출
        console.log("전체 프로젝트 조회 성공:", response);
        setfindProjects(response);
      } catch (error) {
        console.error("전체 프로젝트 조회 실패:", error);
      }
    };
    fetchAllFindProjects();
  }, []);

  return (
    <Container>
      <Row>
        {currentfindProject.map((findProject) => (
          <Col md={3} className="mb-4" key={findProject.id}>
            <Card
              onClick={() => navigate(`/findProject/list/${findProject.id}`)}
              style={{ cursor: "pointer" }}
            >
              <Card.Img variant="top" src={findProject.image_url} />
              <Card.Body>
                <Card.Title>{findProject.title}</Card.Title>
                <Card.Text>
                  <strong>작성자:</strong> {findProject.userId}
                </Card.Text>
                <Card.Text>{findProject.description}</Card.Text>
                <Card.Text>
                  <strong>기술스택:</strong> {findProject.skills}
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
