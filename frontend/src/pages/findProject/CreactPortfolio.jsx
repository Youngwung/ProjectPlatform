import React, { useState } from "react";
import { Form, Button, Container } from "react-bootstrap";

const CreatePortfolio = () => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [skills, setSkills] = useState("");
  const [githubUrl, setGithubUrl] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log({
      title,
      description,
      skills: skills.split(",").map((skill) => skill.trim()),
      githubUrl,
    });
    alert("포트폴리오가 저장되었습니다.");
  };

  return (
    <Container className="mt-4">
      <h1 className="text-center mb-4">포트폴리오 작성</h1>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>제목</Form.Label>
          <Form.Control
            type="text"
            placeholder="포트폴리오 제목"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>설명</Form.Label>
          <Form.Control
            as="textarea"
            rows={4}
            placeholder="포트폴리오 설명"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>기술</Form.Label>
          <Form.Control
            type="text"
            placeholder="기술을 쉼표로 구분하여 입력하세요 (예: React, Node.js, MySQL)"
            value={skills}
            onChange={(e) => setSkills(e.target.value)}
          />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>GitHub 링크</Form.Label>
          <Form.Control
            type="url"
            placeholder="GitHub 리포지토리 URL"
            value={githubUrl}
            onChange={(e) => setGithubUrl(e.target.value)}
          />
        </Form.Group>
        <Button variant="primary" type="submit">
          저장
        </Button>
      </Form>
    </Container>
  );
};

export default CreatePortfolio;
