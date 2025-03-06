import React, { useEffect, useState } from "react";
import { Badge, Button, Card, ListGroup, Spinner } from "react-bootstrap";
import { FaLock, FaUserEdit } from "react-icons/fa";
import authApi from "../../api/authApi";
import SkillTagComponent from "../skill/SkillTagComponent";
import EditInfoModal from "./EditInfoModal";
import PasswordModal from "./PasswordModal";

const UserInfoCard = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showEditPasswordModal, setShowEditPasswordModal] = useState(false);
  const [editUser, setEditUser] = useState({}); // 수정할 유저 정보

  // ✅ 사용자 데이터 다시 조회 함수
  const fetchUserData = async () => {
    try {
      console.log("🔍 사용자 데이터 다시 조회 중...");
      const userData = await authApi.getAuthenticatedUser();
      setUser(userData);
      setEditUser(userData); // 수정할 데이터 업데이트
    } catch (error) {
      console.error("❌ 사용자 데이터 조회 실패:", error);
    }
  };
  
  // ✅ 현재 로그인된 사용자 정보 가져오기
  useEffect(() => {
    const fetchUser = async () => {
      try {
        console.log("🔍 현재 로그인된 사용자 정보 가져오는 중...");
        const userData = await authApi.getAuthenticatedUser();
        console.log("✅ 로그인된 사용자 정보:", userData);
        window.debugUserData = userData;
        setUser(userData);
        setEditUser(userData); // 수정할 데이터에도 저장
        setLoading(false);
      } catch (error) {
        console.error("❌ 사용자 정보 불러오기 실패:", error);
        setUser(null);
        setLoading(false);
      }
    };
    fetchUser();
  }, []);

  // ✅ 입력값 변경 핸들러 (editUser 상태 업데이트)
  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "links") {
      setEditUser((prev) => ({
        ...prev,
        links: value.split(",").map((link) => ({ url: link.trim() })), // 객체 배열로 저장
      }));
    } else {
      setEditUser((prev) => ({ ...prev, [name]: value }));
    }
  };

  // ✅ 수정 정보 저장 함수 (백엔드 요청, 인자로 업데이트할 사용자 정보 전달)
  const handleSaveUserInfo = async (updatedUser) => {
    try {
      console.log("✅ 수정된 정보 저장 중...", updatedUser);
      await authApi.editUserInfo(updatedUser); // 백엔드 업데이트 요청
      setUser(updatedUser); // 화면에 즉시 반영
      setShowEditModal(false); // 모달 닫기
      alert("사용자 정보가 수정되었습니다!");
    } catch (error) {
      console.error("❌ 사용자 정보 업데이트 실패:", error);
      alert("수정 중 오류가 발생했습니다.");
    }
  };

  return (
    <Card className="shadow-lg border-0 rounded-4 modern-card position-relative">
      <Card.Body className="p-4">
        {/* 로딩 상태 */}
        {loading ? (
          <div className="text-center">
            <Spinner animation="border" variant="primary" />
            <p className="mt-2">사용자 정보를 불러오는 중...</p>
          </div>
        ) : user ? (
          <>
            {/* 헤더 */}
            <div className="d-flex align-items-center mb-3">
              <FaUserEdit size={28} className="text-primary me-2" />
              <Card.Title className="fs-4 fw-bold text-primary text-uppercase">
                내 정보
              </Card.Title>
            </div>

            {/* 사용자 정보 표시 */}
            <ListGroup variant="flush" className="mb-3">
              <ListGroup.Item>
                <strong>이름:</strong> {user.name}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>아이디(이메일):</strong> {user.email}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>프로바이더:</strong>{" "}
                {user.providerName ? user.providerName : "없음"}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>전화번호:</strong> {user.phoneNumber || "없음"}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>기술 스택:</strong>{" "}
                <SkillTagComponent skills={user.skills} />
              </ListGroup.Item>
              <ListGroup.Item>
              <strong>사용자 링크:</strong>{" "}
                {user.links && user.links.length > 0 ? (
                  user.links.map((link, index) => {
                    // link.url이 http 또는 https로 시작하지 않으면 https://를 붙입니다.
                    const fullUrl = link.url.startsWith("http")
                      ? link.url
                      : `https://${link.url}`;

                    return (
                      <a
                        key={index}
                        href={fullUrl}
                        target="_blank" // 새 탭에서 열기
                        rel="noopener noreferrer"
                        style={{ textDecoration: "none" }} // 밑줄 제거
                        title={link.url} // 마우스 오버 시 원본 URL 표시
                      >
                        <Badge bg="primary" className="me-1">
                          {link.description}
                        </Badge>
                      </a>
                    );
                  })
                ) : (
                  "사용자의 링크가 없습니다."
                )}
              </ListGroup.Item>
            </ListGroup>

            {/* 하단 버튼: 비밀번호 변경 */}
            <div className="d-flex">
              <Button
                variant="outline-primary"
                className="rounded-pill px-4"
                onClick={() => setShowEditPasswordModal(true)}
              >
                <FaLock className="me-2" /> 비밀번호 변경
              </Button>
            </div>

            {/* 디버깅 UI - editUser 데이터 확인 */}
            {/* <div
              style={{
                backgroundColor: "#f8f9fa",
                padding: "10px",
                borderRadius: "5px",
                marginTop: "10px",
              }}
            >
              <strong>🔍 editUser 데이터:</strong>
              <pre>{JSON.stringify(editUser, null, 2)}</pre>

              {Array.isArray(editUser.links) ? (
                <p>
                  ✅ editUser.links는 배열입니다! (길이: {editUser.links.length})
                </p>
              ) : (
                <p>❌ editUser.links가 배열이 아닙니다! 데이터 확인 필요</p>
              )}
            </div> */}
          </>
        ) : (
          <p className="text-center">사용자 정보를 가져올 수 없습니다.</p>
        )}
      </Card.Body>

      {/* 수정 버튼 (모달 열기) */}
      {!loading && user && (
        <Button
          variant="secondary"
          className="position-absolute top-0 end-0 m-3"
          onClick={() => setShowEditModal(true)}
        >
          <FaUserEdit className="me-1" /> 수정
        </Button>
      )}

      {/* ✅ EditInfoModal 연결 */}
      <EditInfoModal
        show={showEditModal}
        onHide={() => setShowEditModal(false)}
        editUser={editUser}
        handleChange={handleChange}
        handleSaveUserInfo={handleSaveUserInfo}
        fetchUserData={fetchUserData}
      />
      <PasswordModal
        show={showEditPasswordModal}
        onHide={() => setShowEditPasswordModal(false)}
        onPasswordChangeSuccess={(msg) => {
          console.log("비밀번호 변경 성공:", msg);}}
      />
    </Card>
  );
};

export default UserInfoCard;
