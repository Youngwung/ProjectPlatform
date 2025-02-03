import React from "react";
import { Form, Button, Row, Col, Container } from "react-bootstrap";

const LinkForm = ({ newLink, setNewLink, editLink, setEditLink, handleCreateLink, handleUpdateLink, linkTypes }) => {
  return (
    <Container>
      <h2 className="mt-3">{editLink ? "링크 수정" : "새 링크 생성"}</h2>
      <Form>
        <Row className="mb-3">
          <Col md={6}>
            <Form.Group controlId="formUrl">
              <Form.Label>URL</Form.Label>
              <Form.Control
                type="text"
                placeholder="URL 입력"
                value={editLink ? editLink.url : newLink.url}
                onChange={(e) =>
                  editLink
                    ? setEditLink({ ...editLink, url: e.target.value })
                    : setNewLink({ ...newLink, url: e.target.value })
                }
              />
            </Form.Group>
          </Col>
          <Col md={6}>
            <Form.Group controlId="formDescription">
              <Form.Label>설명</Form.Label>
              <Form.Control
                type="text"
                placeholder="설명 입력"
                value={editLink ? editLink.description : newLink.description}
                onChange={(e) =>
                  editLink
                    ? setEditLink({ ...editLink, description: e.target.value })
                    : setNewLink({ ...newLink, description: e.target.value })
                }
              />
            </Form.Group>
          </Col>
        </Row>

        <Form.Group controlId="formLinkType" className="mb-3">
          <Form.Label>링크 타입</Form.Label>
          <Form.Select
            value={editLink ? editLink.linkTypeId : newLink.linkTypeId}
            onChange={(e) =>
              editLink
                ? setEditLink({ ...editLink, linkTypeId: Number(e.target.value) })
                : setNewLink({ ...newLink, linkTypeId: Number(e.target.value) })
            }
          >
            <option value="">-- 링크 타입 선택 --</option>
            {linkTypes.map((type) => (
              <option key={type.id} value={type.id}>
                {type.name}
              </option>
            ))}
          </Form.Select>
        </Form.Group>

        {/* 버튼 영역 */}
        <div className="d-flex justify-content-end">
          {editLink ? (
            <>
              <Button variant="success" className="me-2" onClick={handleUpdateLink}>
                저장
              </Button>
              <Button variant="secondary" onClick={() => setEditLink(null)}>
                취소
              </Button>
            </>
          ) : (
            <Button variant="primary" onClick={handleCreateLink}>
              생성
            </Button>
          )}
        </div>
      </Form>
    </Container>
  );
};

export default LinkForm;
