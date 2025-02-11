import React, { useState, useEffect } from "react";
import { useLocation, useParams, useNavigate } from "react-router-dom";
import { Container, Form, Button, Spinner, Alert } from "react-bootstrap";
import portfolioApi from "../../api/portfolioApi";

const ModifyPortfolio = () => {
  const { portfolioId } = useParams(); // URL에서 portfolioId 가져오기
  const location = useLocation(); // 전달받은 state 데이터
  const navigate = useNavigate();

  // 초기 상태 설정
  const portfolioInit = {
    id: null,
    title: "",
    description: "",
    skills: [],
    github_url: "",
  };

  const [portfolio, setPortfolio] = useState(location.state?.portfolio || portfolioInit);
  const [loading, setLoading] = useState(!location.state?.portfolio); // state 없으면 로딩 필요
  const [error, setError] = useState(null);
  const [title, setTitle] = useState(portfolio.title);
  const [description, setDescription] = useState(portfolio.description);
  const [skills, setSkills] = useState(portfolio.skills?.join(", ") || "");
  const [githubUrl, setGithubUrl] = useState(portfolio.github_url);

  // `location.state`에 데이터가 없는 경우 API에서 직접 불러오기
  useEffect(() => {
    if (!location.state?.portfolio) {
      const fetchPortfolio = async () => {
        try {
          setLoading(true);
          setError(null);
          const data = await portfolioApi.getOne(portfolioId);
          if (!data || !data.id) {
            throw new Error("해당 포트폴리오 데이터를 찾을 수 없습니다.");
          }
          setPortfolio(data);
          setTitle(data.title);
          setDescription(data.description);
          setSkills(data.skills?.join(", ") || "");
          setGithubUrl(data.github_url);
        } catch (err) {
          console.error("❌ 포트폴리오 불러오기 실패:", err);
          setError(err.message);
        } finally {
          setLoading(false);
        }
      };
      fetchPortfolio();
    }
  }, [portfolioId, location.state]);

  // 폼 제출 시 업데이트 요청
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      setError(null);

      const updatedData = {
        id: portfolioId,
        title,
        description,
        skills: skills.split(",").map((skill) => skill.trim()),
        github_url: githubUrl,
      };

      console.log("📌 업데이트 요청 데이터:", updatedData);

      await portfolioApi.updateProject(portfolioId, updatedData);
      alert("포트폴리오가 성공적으로 수정되었습니다.");
      navigate("/portfolio/list");
    } catch (err) {
      console.error("❌ 포트폴리오 수정 실패:", err);
      setError("포트폴리오 수정 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  // 로딩 중일 때 UI
  if (loading) {
    return (
      <Container className="text-center mt-4">
        <Spinner animation="border" variant="primary" />
        <p>로딩 중...</p>
      </Container>
    );
  }

  // 에러 발생 시 UI
  if (error) {
    return (
      <Container className="text-center mt-4">
        <Alert variant="danger">{error}</Alert>
        <Button variant="secondary" onClick={() => navigate("/portfolio/list")}>
          목록으로 돌아가기
        </Button>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <h1>포트폴리오 수정</h1>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>제목</Form.Label>
          <Form.Control
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>설명</Form.Label>
          <Form.Control
            as="textarea"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>기술</Form.Label>
          <Form.Control
            type="text"
            value={skills}
            onChange={(e) => setSkills(e.target.value)}
            placeholder="예: React, Bootstrap, MySQL"
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>GitHub 링크</Form.Label>
          <Form.Control
            type="text"
            value={githubUrl}
            onChange={(e) => setGithubUrl(e.target.value)}
          />
        </Form.Group>

        <Button variant="primary" type="submit" disabled={loading}>
          {loading ? "수정 중..." : "수정 완료"}
        </Button>
      </Form>
    </Container>
  );
};

export default ModifyPortfolio;
