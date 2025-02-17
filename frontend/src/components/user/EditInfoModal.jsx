import React, { useState, useEffect } from "react";
import { Modal, Button, Form, InputGroup } from "react-bootstrap";
import linkApi from "../../api/linkApi"; // ✅ 링크 타입 API 가져오기

const EditInfoModal = ({
  show,
  onHide,
  editUser = {},
  handleChange,
  handleSaveUserInfo,
  fetchUserData, // 추가: 모달을 다시 열 때 사용자 데이터를 다시 조회하는 함수
}) => {
  const [userLinks, setUserLinks] = useState([]);
  const [linkTypes, setLinkTypes] = useState([]);
  const [newLink, setNewLink] = useState(null);
  const [editMode, setEditMode] = useState(null);
  const [originalLink, setOriginalLink] = useState(null);

  // editUser의 링크 데이터를 userLinks 상태에 초기화
  useEffect(() => {
    if (!editUser || Object.keys(editUser).length === 0) {
      return;
    }

    if (editUser?.links) {
      setUserLinks(
        editUser.links.map((link) => ({
          ...link,
          url: String(link.url ?? ""),
          description: String(link.description ?? ""),
        }))
      );
    } else {
      setUserLinks([]);
    }
  }, [editUser]);

  // 링크 타입을 백엔드에서 받아오기
  useEffect(() => {
    const fetchLinkTypes = async () => {
      try {
        const types = await linkApi.getAllLinkTypes();
        setLinkTypes(types);
      } catch (error) {
        console.error("🚨 링크 타입 불러오기 실패:", error);
        alert("링크 타입을 불러오는 데 실패했습니다. 다시 시도해주세요.");
      }
    };
    fetchLinkTypes();
  }, []);

  // 새 링크 추가 함수
  const addNewLink = () => {
    if (!linkTypes.length) {
      alert("링크 타입을 불러오는 중입니다. 잠시 후 다시 시도해주세요.");
      return;
    }
    setEditMode(null);
    setNewLink({
      userId: editUser.id,
      url: "",
      description: "",
      linkTypeId: linkTypes[0]?.id ?? null,
    });
  };

  // newLink 객체의 값 변경 핸들러
  const handleNewLinkChange = (key, value) => {
    setNewLink((prev) => ({ ...prev, [key]: value }));
  };

  // 기존 링크 업데이트 함수
  const updateLink = (index, key, value) => {
    setUserLinks((prevLinks) =>
      prevLinks.map((link, i) => (i === index ? { ...link, [key]: value } : link))
    );
  };

  // 수정 모드 활성화
  const enableEditMode = (index) => {
    setNewLink(null);
    setOriginalLink(userLinks[index]);
    setEditMode(index);
  };

  // 수정 모드 취소
  const cancelEditMode = () => {
    if (originalLink !== null && editMode !== null) {
      setUserLinks((prevLinks) =>
        prevLinks.map((link, i) => (i === editMode ? originalLink : link))
      );
    }
    setEditMode(null);
    setOriginalLink(null);
  };

  // 수정 모드 확인
  const confirmEditMode = () => {
    setEditMode(null);
    setOriginalLink(null);
  };

  // 새 링크 추가 취소 함수
  const cancelNewLink = () => {
    setNewLink(null);
  };

  // 새 링크 추가 확인 함수
  const confirmNewLink = () => {
    if (!newLink.url.trim() || !newLink.description.trim()) {
      alert("URL과 설명을 올바르게 입력하세요.");
      return;
    }
    setUserLinks((prev) => [...prev, newLink]);
    setNewLink(null);
  };

  // 링크 삭제 함수
  const deleteLink = async (index) => {
    const link = userLinks[index];
    if (!window.confirm("정말 삭제하시겠습니까?")) return;
    try {
      await linkApi.deleteLink(link.id, link.userId);
      setUserLinks((prevLinks) => prevLinks.filter((_, i) => i !== index));
      alert("✅ 링크가 삭제되었습니다.");
    } catch (error) {
      console.error("🚨 링크 삭제 실패:", error);
      alert("링크 삭제에 실패했습니다. 다시 시도해주세요.");
    }
  };

  // 전체 저장 함수
  const handleSave = async () => {
    try {
      let allLinks = [...userLinks];
      if (
        newLink &&
        !userLinks.some(
          (link) =>
            link.url === newLink.url &&
            link.description === newLink.description &&
            link.linkTypeId === newLink.linkTypeId
        )
      ) {
        allLinks.push(newLink);
      }
      const processedLinks = await Promise.all(
        allLinks.map(async (link) => {
          if (!link.id) {
            const createdLink = await linkApi.createLink(link);
            return createdLink;
          } else {
            return link;
          }
        })
      );
      const updatedUser = { ...editUser, links: processedLinks };
      await handleSaveUserInfo(updatedUser);
      onHide(); // 모달 닫기
    } catch (error) {
      console.error("전체 저장 실패:", error);
      alert("저장 중 오류가 발생했습니다.");
    }
  };

  // 모달이 닫힐 때 데이터 초기화 및 다시 조회
  const handleClose = () => {
    onHide();
    fetchUserData(); // 모달이 닫힐 때 사용자 데이터를 다시 조회
  };

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton className="border-0">
        <Modal.Title>내 정보 수정</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          {/* 전화번호 입력 필드 */}
          <div>기술스택추가해야함</div>{/* TODO*/}
          <Form.Group className="mb-3">
            <Form.Label>전화번호</Form.Label>
            <Form.Control
              type="text"
              name="phoneNumber"
              value={editUser.phoneNumber || ""}
              onChange={handleChange}
              placeholder="전화번호를 입력하세요"
            />
          </Form.Group>

          {/* 새 링크 추가 버튼 */}
          <div className="mb-3 d-flex justify-content-between align-items-center">
            <div>링크목록</div>
            <Button variant="primary" onClick={addNewLink}>
              ➕ 새 링크 추가
            </Button>
          </div>

          {/* 새 링크 입력 UI */}
          {newLink && (
            <div className="border p-3 mb-3">
              <InputGroup className="mb-2">
                <Form.Control
                  type="text"
                  placeholder="URL 입력"
                  value={newLink.url}
                  onChange={(e) => handleNewLinkChange("url", e.target.value)}
                />
                <Form.Select
                  value={newLink.linkTypeId}
                  onChange={(e) =>
                    handleNewLinkChange("linkTypeId", parseInt(e.target.value))
                  }
                >
                  {linkTypes.map((type) => (
                    <option key={type.id} value={type.id}>
                      {type.name}
                    </option>
                  ))}
                </Form.Select>
              </InputGroup>
              <InputGroup className="mb-2">
                <Form.Control
                  type="text"
                  placeholder="설명 입력"
                  value={newLink.description}
                  onChange={(e) =>
                    handleNewLinkChange("description", e.target.value)
                  }
                />
              </InputGroup>
              <div className="d-flex justify-content-end">
                <Button variant="secondary" onClick={cancelNewLink}>
                  취소
                </Button>
                <Button variant="primary" onClick={confirmNewLink} className="ms-2">
                  확인
                </Button>
              </div>
            </div>
          )}

          {/* 기존 링크 목록 */}
          {userLinks.length > 0 ? (
            userLinks.map((link, index) => {
              const linkTypeName =
                linkTypes.find((type) => type.id === link.linkTypeId)?.name ||
                "알 수 없음";
              return (
                <InputGroup className="mb-2" key={index}>
                  {editMode === index ? (
                    <>
                      <Form.Control
                        type="text"
                        value={String(link.url)}
                        onChange={(e) =>
                          updateLink(index, "url", e.target.value)
                        }
                      />
                      <Form.Control
                        type="text"
                        value={String(link.description)}
                        onChange={(e) =>
                          updateLink(index, "description", e.target.value)
                        }
                      />
                      <Button variant="secondary" onClick={cancelEditMode}>
                        ❌ 취소
                      </Button>
                      <Button variant="primary" onClick={confirmEditMode} className="ms-2">
                        ✅ 확인
                      </Button>
                    </>
                  ) : (
                    <>
                      <span className="me-2">{linkTypeName}</span>
                      <span className="me-2">{String(link.url)}</span>
                      <span className="me-2">{String(link.description)}</span>
                      <Button
                        variant="warning"
                        onClick={() => enableEditMode(index)}
                        className="me-2"
                      >
                        ✏️ 수정
                      </Button>
                      <Button
                        variant="danger"
                        onClick={() => deleteLink(index)}
                      >
                        🗑 삭제
                      </Button>
                    </>
                  )}
                </InputGroup>
              );
            })
          ) : (
            <p>링크가 없습니다.</p>
          )}
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          취소
        </Button>
        <Button variant="primary" onClick={handleSave}>
          저장
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default EditInfoModal;