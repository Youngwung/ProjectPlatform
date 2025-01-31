import React from "react";
import { Container } from "react-bootstrap";

const LinkList = ({ links, linkTypes, setEditLink, handleDeleteLink }) => {
  // 🔹 linkTypeId -> linkTypeName 변환 (최적화)
  const linkTypeMap = Object.fromEntries(linkTypes.map((type) => [type.id, type.name]));

  return (
    <div>
      <h2>링크 목록</h2>
      {links.length === 0 ? (
        <p>등록된 링크가 없습니다.</p>
      ) : (
        links.map((link) => (
          <div key={link.id}>
            <Container>
              <div style={{ border: "1px solid black" }}>
                <p><strong>userId:</strong> {link.userId}</p>
                <p><strong>URL:</strong> {link.url}</p>
                <p><strong>설명:</strong> {link.description}</p>
                <p><strong>링크 타입:</strong> {linkTypeMap[link.linkTypeId] || "없음"}</p>
                <button
                  style={{ border: "1px solid green", background: "green", color: "white" }}
                  onClick={() => setEditLink({ ...link })}
                >
                  수정
                </button>
                <button
                  style={{ border: "1px solid red", background: "red", color: "white" }}
                  onClick={() => handleDeleteLink(link.id)}
                >
                  삭제
                </button>
              </div>
            </Container>
          </div>
        ))
      )}
    </div>
  );
};

export default LinkList;
