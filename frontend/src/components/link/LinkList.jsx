import React from "react";
import { Container, Table, Button, Row, Col, Card } from "react-bootstrap";

const LinkList = ({ links, linkTypes, setEditLink, handleDeleteLink }) => {
  const linkTypeMap = Object.fromEntries(linkTypes.map((type) => [type.id, type.name]));

  return (
    <Container>
      <h2 className="mt-4">링크 목록</h2>

      {/* 데스크톱 화면에서는 테이블 형태로 표시 */}
      <div className="d-none d-md-block">
        <Table striped bordered hover responsive>
          <thead>
            <tr>
              <th>User ID</th>
              <th>URL</th>
              <th>설명</th>
              <th>링크 타입</th>
              <th>액션</th>
            </tr>
          </thead>
          <tbody>
            {links.map((link) => (
              <tr key={link.id}>
                <td>{link.userId}</td>
                <td>{link.url}</td>
                <td>{link.description}</td>
                <td>{linkTypeMap[link.linkTypeId] || "없음"}</td>
                <td>
                  <Button variant="warning" className="me-2" onClick={() => setEditLink({ ...link })}>
                    수정
                  </Button>
                  <Button variant="danger" onClick={() => handleDeleteLink(link.id)}>
                    삭제
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>

      {/* 모바일 화면에서는 카드 형태로 표시 */}
      <div className="d-md-none">
        <Row>
          {links.map((link) => (
            <Col xs={12} key={link.id} className="mb-3">
              <Card>
                <Card.Body>
                  <Card.Title>{link.url}</Card.Title>
                  <Card.Text>
                    <strong>설명:</strong> {link.description} <br />
                    <strong>링크 타입:</strong> {linkTypeMap[link.linkTypeId] || "없음"}
                  </Card.Text>
                  <div className="d-flex justify-content-end">
                    <Button variant="warning" className="me-2" onClick={() => setEditLink({ ...link })}>
                      수정
                    </Button>
                    <Button variant="danger" onClick={() => handleDeleteLink(link.id)}>
                      삭제
                    </Button>
                  </div>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </div>
    </Container>
  );
};

export default LinkList;
