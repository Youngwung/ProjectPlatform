import React, { useState, useEffect } from 'react';
import { Container, Nav, Navbar, NavDropdown,OverlayTrigger,Tooltip } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { FaUser, FaBell } from "react-icons/fa"; // 🔹 react-icons에서 가져옴
import AuthApi from '../api/authApi';

const Top = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userName, setUsername] = useState('');

  // ✅ 로그인 상태 확인
  useEffect(() => {
    const checkAuth = async () => {
      try {
        const userData = await AuthApi.getAuthenticatedUser(); // 🔥 사용자 정보 가져오기
        setIsAuthenticated(true);
        setUsername(userData); // 🔹 사용자 이름 저장 (현재 이메일)
      } catch (error) {
        setIsAuthenticated(false);
        setUsername('');
      }
    };
    checkAuth();
  }, []);

  // ✅ 로그아웃 함수
  const handleLogout = async () => {
    try {
      await AuthApi.logout();
      alert('로그아웃 되었습니다.');
      setIsAuthenticated(false);
      setUsername('');
      window.location.href = '/'; // 🔹 홈으로 이동
    } catch (error) {
      alert('로그아웃 실패');
      console.error('로그아웃 에러:', error);
    }
  };

  return (
    <>
      <header className="text-center py-5 bg-primary text-white">
        <h1>구인 구직 사이트</h1>
        <p>당신의 커리어를 여기서 시작하세요!</p>
      </header>
      <Navbar expand="lg" className="bg-body-tertiary">
        <Container fluid>
          <Navbar.Brand as={Link} to="/">Project Platform</Navbar.Brand>
          <Navbar.Toggle aria-controls="navbarScroll" />
          <Navbar.Collapse id="navbarScroll">
            <Nav className="me-auto my-2 my-lg-0" style={{ maxHeight: '100px' }} navbarScroll>
              <NavDropdown title="프로젝트" id="navbarScrollingDropdown">
                <NavDropdown.Item as={Link} to="/project">전체보기</NavDropdown.Item>
                <NavDropdown.Item as={Link} to="/project/frontend">Frontend</NavDropdown.Item>
                <NavDropdown.Item as={Link} to="/project/backend">Backend</NavDropdown.Item>
              </NavDropdown>
              <NavDropdown title="포트폴리오" id="navbarScrollingDropdown">
                <NavDropdown.Item as={Link} to="/portfolio">전체보기</NavDropdown.Item>
                <NavDropdown.Item as={Link} to="/portfolio/frontend">Frontend</NavDropdown.Item>
                <NavDropdown.Item as={Link} to="/portfolio/backend">Backend</NavDropdown.Item>
              </NavDropdown>
              <Nav.Link as={Link} to="/link">링크</Nav.Link>
            </Nav>

            {/* 🔹 사용자 정보 및 아이콘 표시 */}
            <Nav className="d-flex align-items-center">
              {isAuthenticated ? (
                <>
                  {/* 🔹 알람 아이콘 (현재 미구현) */}
                  {/* <Nav.Link as={Link} to="/notifications">
                    <FaBell size={24} className="mx-2" />
                  </Nav.Link> */}

                  {/* 🔹 사용자 이름 및 마이페이지 아이콘 */}
                  <span className="mx-2">{userName} 님</span>
                  <OverlayTrigger
                    placement="bottom"
                    overlay={<Tooltip id="mypage-tooltip">마이페이지</Tooltip>}
                  >
                    <Nav.Link as={Link} to="/mypage">
                      <FaUser size={20} />
                    </Nav.Link>
                  </OverlayTrigger>

                  {/* 🔹 로그아웃 버튼 */}
                  <Nav.Link onClick={handleLogout} style={{ cursor: 'pointer', marginLeft: '15px' }}>로그아웃</Nav.Link>
                </>
              ) : (
                <>
                  {/* 🔹 로그인/회원가입 버튼 */}
                  <Nav.Link as={Link} to="/login" style={{ marginLeft: '15px' }}>로그인</Nav.Link>
                  <Nav.Link as={Link} to="/signup">회원가입</Nav.Link>
                </>
              )}
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </>
  );
};

export default Top;
