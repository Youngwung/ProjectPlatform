import React,{useRef} from 'react'
import { Form, Button, Card,Container,Row,Col,InputGroup} from 'react-bootstrap'
import userApi from '../api/userApi'


const handleSubmit = async (e) => {
    e.preventDefault();
    const email = e.target.email.value;
    const password = e.target.password.value;
    const confirmPassword = e.target.confirmPassword.value;
    const name = e.target.name.value;
    const phoneNumber = e.target.phoneNumber.value;
    const experience = e.target.experience.value;
    const providerId = "4"
    const skills = e.target.skills.value;
    if (password !== confirmPassword) {
        alert('비밀번호가 일치하지 않습니다.');
        return;
    }
    try {
        const response = await userApi.createUser({ email, password, name, phoneNumber, experience, providerId,skills });
        console.log(response);
        alert('회원가입이 완료되었습니다.');
        window.location.href = '/login';
    } catch (error) {
        alert('회원가입 중 오류가 발생했습니다.');
    }
}
const Signup = () => {
    const emailRef = useRef();

    const handleDuplicate = async () => {
        const email = emailRef.current.value; 
        if (!email) {
            alert("이메일을 입력해주세요.");
            return;
        }
        try {
            const response = await userApi.checkEmail(email);
            if (response) {
                alert('중복된 이메일입니다.');
            } else {
                alert('사용 가능한 이메일입니다.');
            }
        } catch (error) {
            alert('중복 확인 중 오류가 발생했습니다.');
        }
    };
  return (
    <Container>
        <Row className="justify-content-md-center">
            <Col md={6}>
                <Card>
                    <Card.Body>
                        <Card.Title>회원가입</Card.Title>
                        <Form onSubmit={handleSubmit}>
                            <Form.Group controlId="name">
                                <Form.Label>이름</Form.Label>
                                <Form.Control type="text" placeholder="이름을 입력해주세요" />
                            </Form.Group>

                            <Form.Group controlId="email">
                                <Form.Label>이메일</Form.Label>
                                <InputGroup>
                                    <Form.Control type="email" placeholder="이메일을 입11력해주세요" ref={emailRef}/>
                                    <Button variant="secondary" onClick={handleDuplicate}>중복확인</Button>
                                </InputGroup>
                            </Form.Group>

                            <Form.Group controlId="password">
                                <Form.Label>비밀번호</Form.Label>
                                <Form.Control type="password" placeholder="비밀번호를 입력해주세요" />
                            </Form.Group>

                            <Form.Group controlId="confirmPassword">
                                <Form.Label>비밀번호 확인</Form.Label>
                                <Form.Control type="password" placeholder="비밀번호를 확인해주세요" />
                            </Form.Group>

                            <Form.Group controlId="phoneNumber">
                                <Form.Label>전화번호</Form.Label>
                                <Form.Control type="text" placeholder="전화번호를 입력해주세요" />
                            </Form.Group>
                            
                            <Form.Group controlId='experience'>
                                <Form.Label>경력</Form.Label>
                                <Form.Control type='text' placeholder='경력을 입력해주세요' />
                            </Form.Group>
                            <Form.Group controlId='skills'>
                                <Form.Label>기술 스택</Form.Label>
                                <Form.Control type='text' placeholder='스킬을 입력해주세요' />
                            </Form.Group>

                            <Button className='w-100 mt-3' variant="primary" type="submit" >
                                회원가입
                            </Button>
                        </Form>
                    </Card.Body>
                </Card>
            </Col>
        </Row>
    </Container>
  )
}

export default Signup;