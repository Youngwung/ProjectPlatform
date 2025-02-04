import React from 'react'
import { Form, Button, Card,Container,Row,Col} from 'react-bootstrap'
const handleSubmit = (event) => {
    event.preventDefault();
    const email = event.target.email.value;
    const password = event.target.password.value;
    if (email !== 'test@example.com' || password !== 'password') {
        //TODO :  api 키를 연동해서 검색
        alert('이메일 주소 또는 비밀번호가 올바르지 않습니다.');
    } else {
        //TODO : 로그인 성공 시 처리
        alert('로그인 성공');
        //TODO : main 페이지로 리다이렉트
    }
}
const Login = () => {
  return (
    <Container className="my-5">
      <Row className="justify-content-center">
        <Col md={6}>
          <Card>
            <Card.Body>
              <h2 className="text-center">로그인</h2>
              <Form onSubmit={handleSubmit}>
                <Form.Group controlId="email">
                  <Form.Label>이메일 주소</Form.Label>
                  <Form.Control type="email" placeholder="이메일 주소를 입력하세요." />
                </Form.Group>

                <Form.Group controlId="password">
                  <Form.Label>비밀번호</Form.Label>
                  <Form.Control type="password" placeholder="비밀번호를 입력하세요." />
                </Form.Group>
                <Button variant="primary" type="submit" className="w-100 mt-3">
                  로그인
                </Button>
              </Form>
              <Button variant="secondary" className="w-100 mt-3" href='/signup'>
                회원가입
              </Button>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  )
}

export default Login