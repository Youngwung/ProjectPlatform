import React, { useEffect, useState } from "react";
import { Form, Button, Container } from "react-bootstrap";
import findProjectApi from "../../api/findProjectApi";

const CreatePortfolio = () => {
  const [id, setId] = useState(0);
  const [userId, setUserId] = useState(5);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [imageUrl, setImageUrl] = useState("");
  const [skills, setSkills] = useState("");
  const [githubUrl, setGithubUrl] = useState("");
  const [createAt, setCreateAt] = useState("");
  const [updateAt, setUpdateAt] = useState("");
  
  useEffect(() => {
    const fetchNewProject = async () => {
      try {
        const response = await findProjectApi.createProject();
        console.log("새 프로젝트 생성 성공:", response);
        setId(response.id);
        setUserId(response.userId);
        setTitle(response.title);
        setDescription(response.description);
        setImageUrl(response.imageUrl);
        setSkills(response.skills);
        setGithubUrl(response.githubUrl);
        setCreateAt(response.createAt);
        setUpdateAt(response.updateAt);
      } catch (error) {
        console.error("새 프로젝트 생성 실패:", error);
      }
    };
    fetchNewProject();
  }, []);
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log({
      title,
      imageUrl,
      description,
      skills: skills.split(",").map((skill) => skill.trim()),
      githubUrl,
      createAt,
      updateAt,
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
        <Button variant="primary" type="submit" onClick={handleSubmit}>
          저장
        </Button>
      </Form>
    </Container>
  );
};

export default CreatePortfolio;
