import React, { useState } from "react";
import { Button, Card, Col, Container, Form, Row } from "react-bootstrap";
import { FcGoogle } from "react-icons/fc";
import { useNavigate } from "react-router-dom";
import AuthApi from "../api/authApi";

const Login = () => {
	const navigate = useNavigate();
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");

	const handleSubmit = async (event) => {
		event.preventDefault();
		alert("로그인 버튼 클릭");
		//console.log(email, password);
		if (email === "" || password === "") {
			alert("이메일 주소와 비밀번호를 입력하세요.");
		} else {
			try {
				const response = await AuthApi.login(email, password);
				//console.log("서버에 보낸 정보" + response);
				if (response) {
					alert("로그인 성공");
					//console.log("로그인아이디:" + email + "로그인비번" + password);
					window.location.href = "/"; //로그인 성공시 홈으로 이동
				} else {
					//401 에러일때
					alert("이메일 주소 또는 비밀번호가 올바르지 않습니다.");
				}
			} catch (error) {
				console.error("서버 에러", error);
				console.error(error.response);
				throw error;
			}
		}
	};

	// 소셜 로그인 구현을 위한 코드
	const handleLogin = (provider) => {
		const oauth2URL = `http://localhost:8080/oauth2/authorization/${provider}`;
		//console.log(oauth2URL);

		window.location.href = oauth2URL;
	};

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
									<Form.Control
										type="email"
										value={email}
										onChange={(e) => setEmail(e.target.value)}
										placeholder="이메일 주소를 입력하세요."
									/>
								</Form.Group>

								<Form.Group controlId="password">
									<Form.Label>비밀번호</Form.Label>
									<Form.Control
										type="password"
										value={password}
										onChange={(e) => setPassword(e.target.value)}
										placeholder="비밀번호를 입력하세요."
									/>
								</Form.Group>
								<Button variant="primary" type="submit" className="w-100 mt-3">
									로그인
								</Button>
							</Form>
							<Button variant="secondary" className="w-100 mt-3" href="/signup">
								회원가입
							</Button>

							<Row className="my-3 text-center">
								<Col xs={12} md={12} xl={12} className="">
									
									<p className="mb-3">소셜로그인</p>
								</Col>
								<Col xs={12} md={12} xl={4}>
									<button
										// w-100 클래스로 버튼이 Col의 전체 너비를 사용
										className="flex items-center px-5 py-2 border border-gray-300 rounded bg-white cursor-pointer w-100 mb-3"
										onClick={() => handleLogin("google")}
									>
										<FcGoogle className="mr-2 text-xl" /> Google
									</button>
								</Col>
								<Col xs={12} md={12} xl={4}>
									<button
										className="flex items-center px-5 py-2 border border-gray-300 rounded bg-white cursor-pointer w-100 mb-3"
										onClick={() => handleLogin("naver")}
									>
										<img 
											src="https://i.imgur.com/bEMZvBl.png" 
											alt="Naver Login"
											className="mr-2 h-5 w-5"
										/>
										Naver
									</button>
								</Col>
								<Col xs={12} md={12} xl={4}>
									<button
										className="flex items-center px-5 py-2 border border-gray-300 rounded bg-white cursor-pointer w-100"
										onClick={() => handleLogin("kakao")}
									>
										<img 
											src="https://i.imgur.com/HGAA6oU.png"
											alt="Kakao Login" 
											className="mr-2 h-5 w-5"
										/>
										Kakao
									</button>
								</Col>
							</Row>
						</Card.Body>
					</Card>
				</Col>
			</Row>
		</Container>
	);
};
export default Login;
