import React, { useState } from "react";
import { Form, Button, Container } from "react-bootstrap";
import portfolioApi from "../../api/portfolioApi";

const CreatePortfolio = () => {
  const [userId, setUserId] = useState(6); // 사용자 ID 상태  
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [skills, setSkills] = useState("");
  const [links, setlinks] = useState(""); // 링크 ID 상태

  const handleSubmit = async (e) => {
    e.preventDefault();

    const portfolioData = {
      userId: userId,
      title: title,
      description: description,
      skills: skills,
      links: links, // 링크 String
    };

    console.log("포트폴리오 데이터:", portfolioData);

    try {
      const response = await portfolioApi.createProject(portfolioData);
      console.log("포트폴리오 저장 성공:", response);
      alert("포트폴리오가 성공적으로 저장되었습니다!");
    } catch (error) {
      console.error("포트폴리오 저장 실패:", error);
      alert("포트폴리오 저장 중 문제가 발생했습니다. 다시 시도해주세요.");
    }
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
          <Form.Label>링크 ID</Form.Label>
          <Form.Control
            type="text"
            placeholder="링크 ID 입력 (선택)"
            value={links}
            onChange={(e) => setlinks(e.target.value)}
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
