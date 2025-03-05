import { jwtDecode } from "jwt-decode";
import React, { useEffect, useState } from "react";
import {
	Alert,
	Button,
	Card,
	Col,
	Container,
	Form,
	InputGroup,
	Row,
	OverlayTrigger,
	Tooltip,
} from "react-bootstrap";
import { useSearchParams } from "react-router-dom";
import authApi from "../api/authApi";
import userApi from "../api/userApi";

const Signup = () => {
	const [queryParams] = useSearchParams();
	const [errorMsg, setErrorMsg] = useState("");
	const [isPasswordValid, setIsPasswordValid] = useState(true);
	const [isConfirmedEmail, setIsConfirmedEmail] = useState(false)
	// 입력값을 useState로 관리
	const [formData, setFormData] = useState({
		name: "",
		email: "",
		password: "",
		confirmPassword: "",
		phoneNumber: "",
		experience: "",
		providerName: "local", // 기본값을 로컬 로그인("local")으로 설정
	});

	// 소셜 로그인 정보 저장
	useEffect(() => {
		const tempToken = queryParams.get("token");
		if (tempToken) {
			const decoded = jwtDecode(tempToken);
			console.log(decoded);
			console.log(decoded.sub);
			
			
			setFormData((prevData) => ({
				...prevData,
				email: decoded.sub,
				name: decoded.name,
				providerName: decoded.providerName, // 소셜 로그인 제공자 정보 저장
			}));
			console.log(formData.email);
			console.log(formData.email === "");
			console.log(formData.email !== "");
		}
	}, [queryParams]);

	/** ✅ 비밀번호 유효성 검사 */
	const validatePassword = (password) => {
		const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
		return passwordRegex.test(password);
	};

	/** ✅ 입력값 변경 핸들러 */
	const handleInputChange = (e) => {
		const { id, value } = e.target;
		setFormData((prevData) => ({ ...prevData, [id]: value }));

		if (id === "password") {
			setIsPasswordValid(validatePassword(value));
		}
	};

	/** ✅ 이메일 중복 확인 */
	const handleDuplicate = async () => {
		const { email } = formData;
		if (!email) {
			alert("이메일을 입력해주세요.");
			return;
		}
		try {
			const response = await authApi.checkEmail(email);
			console.log("response", response);
			//true 중복, false 중복되지 않음
			const ischeckEmail = response.exists;
			console.log("ischeckEmail", response.exists);
			if (ischeckEmail) { // 중복된 이메일일 경우
				alert("❌ 중복된 이메일입니다. 다른 이메일을 입력해주세요.");
				setIsConfirmedEmail(false);
				return; // 🚀 중복이면 함수 종료
			}
	
			// ✅ 사용 가능한 이메일일 경우에만 confirm 창 띄우기
			const confirmUse = window.confirm("✅ 사용 가능한 이메일입니다. 이 이메일을 사용하시겠습니까?");
			if (confirmUse) {
				alert("✔ 이메일이 확정되었습니다.");
				setIsConfirmedEmail(true);
			} else {
				alert("이메일 입력을 변경할 수 있습니다.");
				setIsConfirmedEmail(false);
			}
		} catch (error) {
			console.error("Error during email check:", error);
			alert("⚠ 이메일 중복 확인 중 오류가 발생했습니다. 다시 시도해주세요.");
		}
	};


	/** ✅ 회원가입 */
	const handleSubmit = async (e) => {
		e.preventDefault();
		const { email, password, confirmPassword, name, phoneNumber, experience, providerName } = formData;

		// 🚨 입력값이 비어 있는 경우 return 처리
		if (!email) {
			alert("이메일을 입력해주세요.");
			return;
		}
		if (!isConfirmedEmail) {
			alert("이메일 중복 확인을 완료해주세요.");
			return;
		}
		if (!name) {
			alert("이름을 입력해주세요.");
			return;
		}
		if (!phoneNumber) {
			alert("전화번호를 입력해주세요.");
			return;
		}

		// 소셜 로그인일경우 비밀번호 검사
		if (providerName === "local") {
			if (!password) {
				alert("비밀번호를 입력해주세요.");
				return;
			}
			if (!validatePassword(password)) {
				alert("비밀번호는 최소 8자 이상, 숫자, 문자, 특수문자를 포함해야 합니다.");
				return;
			}
			if (!confirmPassword) {
				alert("비밀번호 확인을 입력해주세요.");
				return;
			}
			if (password !== confirmPassword) {
				alert("비밀번호가 일치하지 않습니다.");
				return;
			}
		}

		try {
			await userApi.createUser({
				email,
				password,
				name,
				phoneNumber,
				experience,
				providerName,
			});
			alert("회원가입이 완료되었습니다.");
			window.location.href = "/login";
		} catch (error) {
			alert("회원가입 중 오류가 발생했습니다.");
		}
	};

	return (
		<Container>
			<Row className="justify-content-md-center">
				<Col md={6}>
					<Card>
						<Card.Body>
							<Card.Title>회원가입</Card.Title>
							{errorMsg && <Alert variant="danger">{errorMsg}</Alert>}
							<Form onSubmit={handleSubmit}>
								<Form.Group controlId="name">
									<Form.Label>이름</Form.Label>
									<Form.Control
										type="text"
										value={formData.name}
										onChange={handleInputChange}
										readOnly = {formData.providerName !== "local"}
									/>
								</Form.Group>

								<Form.Group controlId="email">
									<Form.Label>이메일</Form.Label>
									<InputGroup>
										<Form.Control
											type="email"
											placeholder="이메일을 입력해주세요"
											value={formData.email}
											onChange={handleInputChange}
											required = {formData.email !== ''}
											// readOnly={!!formData.providerName && formData.providerName !== "local"} // 소셜 로그인 이메일 수정 불가
											readOnly={formData.email === ''} // 소셜 로그인 이메일 수정 불가
										/>
										<Button variant="secondary" onClick={handleDuplicate} disabled={!!formData.providerName && formData.providerName !== "local"}>
											중복확인
										</Button>
									</InputGroup>
								</Form.Group>

								{/* ✅ 일반 회원가입일 경우 비밀번호 입력 가능 */}
								{formData.providerName === "local" && (
									<>
										<Form.Group controlId="password">
											<Form.Label>비밀번호</Form.Label>
											<Form.Control
												type="password"
												placeholder="비밀번호를 입력해주세요"
												value={formData.password}
												onChange={handleInputChange}
												required
											/>
											{!isPasswordValid && formData.password.length > 0 && (
												<small className="text-danger">비밀번호는 최소 8자 이상, 숫자, 문자, 특수문자를 포함해야 합니다.</small>
											)}
										</Form.Group>

										<Form.Group controlId="confirmPassword">
											<Form.Label>비밀번호 확인</Form.Label>
											<Form.Control
												type="password"
												placeholder="비밀번호를 확인해주세요"
												value={formData.confirmPassword}
												onChange={handleInputChange}
												required
											/>
										</Form.Group>
									</>
								)}

								<Form.Group controlId="phoneNumber">
									<Form.Label>전화번호</Form.Label>
									<Form.Control
										type="text"
										placeholder="전화번호를 입력해주세요"
										value={formData.phoneNumber}
										onChange={handleInputChange}
									/>
								</Form.Group>

								<Form.Group controlId="experience">
									<Form.Label>경력</Form.Label>
									<Form.Control
										as="textarea"
										rows={10}
										placeholder="경력을 입력해주세요 (미입력 가능)"
										value={formData.experience}
										onChange={handleInputChange}
									/>
								</Form.Group>

								<OverlayTrigger
									placement="top" // 툴팁 위치 (top, bottom, left, right 선택 가능)
									overlay={!isConfirmedEmail ?
										<Tooltip>중복 확인을 해주세요</Tooltip>
										:<></>}
								>
									<span className="d-inline-block w-100">
										<Button
											className="w-100 mt-3"
											variant="primary"
											type="submit"
											disabled={!isConfirmedEmail}
											style={!isConfirmedEmail ? { pointerEvents: "none" } : {}}
										>
											회원가입
										</Button>
									</span>
								</OverlayTrigger>

							</Form>
						</Card.Body>
					</Card>
				</Col>
			</Row>
		</Container>
	);
};

export default Signup;
