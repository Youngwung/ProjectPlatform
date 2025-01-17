import React from 'react';
import { Nav, Navbar, Container, NavDropdown} from 'react-bootstrap';
import { Link } from 'react-router-dom';

const Top = () => {
  return (
    <>
      <header className="text-center py-5 bg-primary text-white">
        <h1>구인 구직 사이트</h1>
        <p>당신의 커리어를 여기서 시작하세요!</p>
      </header>
      <Navbar expand="lg" className="bg-body-tertiary">
        <Container fluid className=''>
          <Navbar.Brand as={Link} to="/">Project Platform</Navbar.Brand>
          <Navbar.Toggle aria-controls="navbarScroll" />
          <Navbar.Collapse id="navbarScroll">
            <Nav
              className="me-auto my-2 my-lg-0"
              style={{ maxHeight: '100px' }}
              navbarScroll
            >
              <NavDropdown title="구인" id="navbarScrollingDropdown">
                <NavDropdown.Item as={Link} to="/findProject">전체보기</NavDropdown.Item>
                <NavDropdown.Item as={Link} to="/findProject/frontend">frontend</NavDropdown.Item>
                <NavDropdown.Item as={Link} to="/findProject/backend">backend</NavDropdown.Item>
              </NavDropdown>
              <NavDropdown title="구직" id="navbarScrollingDropdown">
                <NavDropdown.Item as={Link} to="/joinProject">전체보기</NavDropdown.Item>
                <NavDropdown.Item as={Link} to="/joinProject/frontend">frontend</NavDropdown.Item>
                <NavDropdown.Item as={Link} to="/joinProject/backend">backend</NavDropdown.Item>
              </NavDropdown>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </>
  );
};

export default Top;
