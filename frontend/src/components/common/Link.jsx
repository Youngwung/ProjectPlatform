import React, { useEffect, useState } from "react";
import linkApi from "../../api/linkApi";
import { Container } from "react-bootstrap";

const Link = () => {
  const [links, setLinks] = useState([]); // 링크 목록 상태
  const [linkTypes, setLinkTypes] = useState([]); // 링크 타입 상태
  const [newLink, setNewLink] = useState({userId:1, url: "", description: "", linkTypeName: "" }); // 새 링크 생성 상태
  const [editLink, setEditLink] = useState(null); // 수정 중인 링크 상태
  const [error, setError] = useState(null); // 에러 상태

  // **1. 링크 목록 조회**
  const fetchLinks = async () => {
    try {
      const data = await linkApi.getAllLinks();
      setLinks(data);
    } catch (err) {
      setError("링크 목록을 불러오는 데 실패했습니다.");
    }
  };

  // **2. 링크 타입 조회**
  const fetchLinkTypes = async () => {
    try {
      const data = await linkApi.getAllLinkTypes();
      setLinkTypes(data);
    } catch (err) {
      setError("링크 타입을 불러오는 데 실패했습니다.");
    }
  };

  // **3. 새 링크 생성**
  const handleCreateLink = async () => {
    try {
      // userId 유효성 검사
      if (!newLink.userId) {
        setError("사용자 ID를 입력해야 합니다.");
        return;
      }
      if (!newLink.linkTypeName) {
        setError("링크 타입을 선택해야 합니다.");
        return;
      }
  
      await linkApi.createLink(newLink);
      setNewLink({ url: "", description: "", linkTypeName: "", userId: 1 }); // 필드 초기화
      fetchLinks(); // 목록 갱신
    } catch (err) {
      setError("새 링크를 생성하는 데 실패했습니다.");
    }
  };
  

  // **4. 링크 수정**
  const handleUpdateLink = async () => {
    if (!editLink) return;
    try {
      await linkApi.updateLink(editLink.id, {
        url: editLink.url,
        description: editLink.description,
        linkTypeName: editLink.linkTypeName,
      });
      setEditLink(null); // 수정 상태 초기화
      fetchLinks(); // 목록 갱신
    } catch (err) {
      setError("링크를 수정하는 데 실패했습니다.");
    }
  };

  // **5. 링크 삭제**
  const handleDeleteLink = async (id) => {
    try {
      await linkApi.deleteLink(id);
      fetchLinks(); // 목록 갱신
    } catch (err) {
      setError("링크를 삭제하는 데 실패했습니다.");
    }
  };

  // 컴포넌트가 마운트될 때 링크 목록 및 링크 타입 불러오기
  useEffect(() => {
    fetchLinks();
    fetchLinkTypes();
  }, []);

  return (
    <div>
      <h1>Link 관리</h1>

      {/* 에러 메시지 */}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {/* 새 링크 생성 */}
      <div>
        <h2>새 링크 생성</h2>
        <input
          type="text"
          placeholder="URL"
          value={newLink.url}
          onChange={(e) => setNewLink({ ...newLink, url: e.target.value })}
        />
        <input
          type="text"
          placeholder="설명"
          value={newLink.description}
          onChange={(e) => setNewLink({ ...newLink, description: e.target.value })}
        />
        <select
          value={newLink.linkTypeName}
          onChange={(e) => setNewLink({ ...newLink, linkTypeName: e.target.value })}
        >
          <option value="">-- 링크 타입 선택 --</option>
          {linkTypes.map((type) => (
            <option key={type.id} value={type.name}>
              {type.name}
            </option>
          ))}
        </select>
        <button onClick={handleCreateLink}>생성</button>
      </div>

      {/* 링크 목록 */}
      <div>
        <h2>링크 목록</h2>
        {links.length === 0 ? (
          <p>등록된 링크가 없습니다.</p>
        ) : (
          links.map((link) => (
            <div key={link.id}>
              {editLink && editLink.id === link.id ? (
                // 링크 수정 UI
                <div>
                  <input
                    type="text"
                    style={{ border: "1px solid black" }}
                    value={editLink.url}
                    onChange={(e) =>
                      setEditLink({ ...editLink, url: e.target.value })
                    }
                  />
                  <input
                    type="text"
                    style={{ border: "1px solid black" }}
                    value={editLink.description}
                    onChange={(e) =>
                      setEditLink({ ...editLink, description: e.target.value })
                    }
                  />
                  <select
                    value={editLink.linkTypeName}
                    onChange={(e) =>
                      setEditLink({ ...editLink, linkTypeName: e.target.value })
                    }
                  >
                    <option value="">-- 링크 타입 선택 --</option>
                    {linkTypes.map((type) => (
                      <option key={type.id} value={type.name}>
                        {type.name}
                      </option>
                    ))}
                  </select>
                  <button onClick={handleUpdateLink}>저장</button>
                  <button onClick={() => setEditLink(null)}>취소</button>
                </div>
              ) : (
                // 링크 목록 UI
                <Container>
                  <div style={{ border: "1px solid black" }}>
                    <p>
                      <strong>userId:</strong> {link.userId}
                    </p>
                    <p>
                      <strong>URL:</strong> {link.url}
                    </p>
                    <p>
                      <strong>설명:</strong> {link.description}
                    </p>
                    <p>
                      <strong>링크 타입:</strong> {link.linkTypeName || "없음"}
                    </p>
                    <button
                      style={{
                        border: "1px solid green",
                        background: "green",
                        color: "white",
                      }}
                      onClick={() => setEditLink(link)}
                    >
                      수정
                    </button>
                    <button
                      style={{
                        border: "1px solid red",
                        background: "red",
                        color: "white",
                      }}
                      onClick={() => handleDeleteLink(link.id)}
                    >
                      삭제
                    </button>
                  </div>
                </Container>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default Link;
