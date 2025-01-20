import React, { useEffect, useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom"; // navigate 가져오기
import { Container, Card, Button } from "react-bootstrap";

const PortfolioDetail = () => {
  const { projectId } = useParams();
  const navigate = useNavigate(); // navigate 함수 초기화
  const [portfolio, setPortfolio] = useState(null);

  useEffect(() => {
    const fetchPortfolio = async () => {
      const dummyData = Array.from({ length: 50 }, (_, index) => ({
        id: index + 1,
        title: `포트폴리오 ${index + 1}`,
        description: `포트폴리오 설명 ${index + 1}`,
        skills: ["React", "Bootstrap", "MySQL"],
        github_url: `https://github.com/project-${index + 1}`,
        image_url: "https://via.placeholder.com/150",
      }));
      const selectedPortfolio = dummyData.find((p) => p.id === parseInt(projectId, 10));
      setPortfolio(selectedPortfolio);
    };
    fetchPortfolio();
  }, [projectId]);

  if (!portfolio) {
    return <Container>로딩 중...</Container>;
  }

  return (
    <Container className="mt-4">
      <Card>
        <Card.Img variant="top" src={portfolio.image_url} alt="포트폴리오 이미지" />
        <Card.Body>
          <Card.Title>{portfolio.title}</Card.Title>
          <Card.Text>{portfolio.description}</Card.Text>
          <Card.Text>
            <strong>기술:</strong> {portfolio.skills.join(", ")}
          </Card.Text>
          <Card.Link href={portfolio.github_url} target="_blank">
            GitHub 링크
          </Card.Link>
          {/* 수정 버튼 */}
          <Button
            variant="primary"
            onClick={() =>
              navigate(`/findProject/modify/${projectId}`, { state: { portfolio } })
            }
          >
            수정
          </Button>
        </Card.Body>
      </Card>
      <Link to="/findProject/list">
        <Button variant="secondary" className="mt-4">
          목록으로 돌아가기
        </Button>
      </Link>
    </Container>
  );
};

export default PortfolioDetail;
