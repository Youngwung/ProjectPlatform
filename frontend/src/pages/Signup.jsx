import { jwtDecode } from "jwt-decode";
import React, { useEffect, useRef, useState } from "react";
import {
	Button,
	Card,
	Col,
	Container,
	Form,
	InputGroup,
	Row,
} from "react-bootstrap";
import { useSearchParams } from "react-router-dom";
import authApi from "../api/authApi";
import userApi from "../api/userApi";

//TODO 아이디 찾기 오류 발생하여 수정해야함

const handleSubmit = async (e) => {
	e.preventDefault();
	const email = e.target.email.value;
	// TODO: 패스워드 정규식 처리 필요, 현재 서버에서 8자 이상 받기로 설정되어있음
	const password = e.target.password.value;
	const confirmPassword = e.target.confirmPassword.value;
	const name = e.target.name.value;
	const phoneNumber = e.target.phoneNumber.value;
	const experience = e.target.experience.value;
	// TODO: 토큰에서 ProviderName 전달하는 로직으로 변경
	const providerId = "4";
	if (password !== confirmPassword) {
		alert("비밀번호가 일치하지 않습니다.");
		return;
	}
	try {
		const response = await userApi.createUser({
			email,
			password,
			name,
			phoneNumber,
			experience,
			// TODO: 토큰에서 ProviderName 전달하는 로직으로 변경
			providerId,
		});
		console.log(response);
		alert("회원가입이 완료되었습니다.");
		window.location.href = "/login";
	} catch (error) {
		alert("회원가입 중 오류가 발생했습니다.");
	}
};

const Signup = () => {
	const [queryParams] = useSearchParams();

	// 소셜 로그인 구현 부분
	const [userInfo, setUserInfo] = useState({
		email: "",
		name: "",
		providerName: "",
	});

	useEffect(() => {
		const tempToken= queryParams.get("token", "");
		console.log(tempToken);
		if (tempToken) {
			const decoded = jwtDecode(tempToken);
			console.log(decoded);
			setUserInfo({
				...userInfo,
				email: decoded.sub,
				name: decoded.name,
				providerName: decoded.providerName,
			});
		}
	}, []);

	const emailRef = useRef();

	const handleDuplicate = async () => {
		// TODO 중복되지 않은 이메일도 중복되었다고 뜸
		const email = emailRef.current.value;
		if (!email) {
			alert("이메일을 입력해주세요.");
			return;
		}
		try {
			const response = await authApi.checkEmail(email);
			if (response) {
				alert("중복된 이메일입니다.");
			} else {
				alert("사용 가능한 이메일입니다.");
			}
		} catch (error) {
			alert("중복 확인 중 오류가 발생했습니다.");
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
									{userInfo.name ? 
									(
										<Form.Control
										type="text"
										className="bg-slate-300"
										placeholder="이름을 입력해주세요"
										value={userInfo.name}
										readOnly={true}
										/>
									) 
									: 
									(
										<Form.Control
										type="text"
										placeholder="이름을 입력해주세요"
										required
										/>
									)}
									
								</Form.Group>

								<Form.Group controlId="email">
									<Form.Label>이메일</Form.Label>
									<InputGroup>
										{userInfo.email ? (
											<Form.Control
											type="email"
											placeholder="이메일을 입력해주세요"
											ref={emailRef}
											value={userInfo.email}
											className="bg-slate-300"
											readOnly={true}
											required
										/>
										) 
										: 
										(
											<Form.Control
												type="email"
												placeholder="이메일을 입력해주세요"
												ref={emailRef}
												required
											/>	
										)}
										<Button variant="secondary" onClick={handleDuplicate}>
											중복확인
										</Button>
										
									</InputGroup>
								</Form.Group>

								<Form.Group controlId="password">
									<Form.Label>비밀번호</Form.Label>
									<Form.Control
										type="password"
										placeholder="비밀번호를 입력해주세요"
										required
									/>
								</Form.Group>

								<Form.Group controlId="confirmPassword">
									<Form.Label>비밀번호 확인</Form.Label>
									<Form.Control
										type="password"
										placeholder="비밀번호를 확인해주세요"
										required
									/>
								</Form.Group>

								<Form.Group controlId="phoneNumber">
									<Form.Label>전화번호</Form.Label>
									<Form.Control
										type="text"
										placeholder="전화번호를 입력해주세요"
									/>
								</Form.Group>

								<Form.Group controlId="experience">
									<Form.Label>경력</Form.Label>
									<Form.Control
										as="textarea"
										rows={10}
										placeholder="경력을 입력해주세요 미입력가능"
									/>
								</Form.Group>

								<Button className="w-100 mt-3" variant="primary" type="submit">
									회원가입
								</Button>
							</Form>
						</Card.Body>
					</Card>
				</Col>
			</Row>
		</Container>
	);
};

export default Signup;
