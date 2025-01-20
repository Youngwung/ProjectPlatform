import React, { useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import { Container, Form, Button } from "react-bootstrap";

const ModifyPortfolio = () => {
  const { projectId } = useParams(); // URL에서 projectId 가져오기
  const location = useLocation(); // 전달받은 state 데이터
  const { portfolio } = location.state || {}; // state가 없을 경우 대비

  const [title, setTitle] = useState(portfolio?.title || "");
  const [description, setDescription] = useState(portfolio?.description || "");
  const [skills, setSkills] = useState(portfolio?.skills.join(", ") || "");
  const [githubUrl, setGithubUrl] = useState(portfolio?.github_url || "");

  const handleSubmit = (e) => {
    e.preventDefault();
    // 여기에 수정 요청 API 호출 로직 추가
    console.log({
      id: projectId,
      title,
      description,
      skills: skills.split(",").map((skill) => skill.trim()),
      github_url: githubUrl,
    });
  };

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

        <Button variant="primary" type="submit">
          수정 완료
        </Button>
      </Form>
    </Container>
  );
};

export default ModifyPortfolio;
