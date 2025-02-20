import React, { useCallback, useEffect, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import InputSkillComponent from "./InputSkillComponent";

const SkillModalComponent = ({ show, handleClose, handleConfirm, skills }) => {
	const [exSkills, setExSkills] = useState("");
	useEffect(() => {
		console.log(skills);
		setExSkills(skills);
	}, [skills]);

	// 스킬 Input 컴포넌트로부터 데이터 불러오기
	const [validation, setValidation] = useState(false);
	const handleValidationComplete = useCallback(({ isValid, value }) => {
		setValidation(isValid);
		if (isValid) {
			setExSkills(value);
		}
	}, []);

	return (
		<Modal show={show} onHide={handleClose}>
			<Modal.Header closeButton>
				<Modal.Title>유저 기술 스택 수정</Modal.Title>
			</Modal.Header>
			<Modal.Body>
				<InputSkillComponent
					// 스킬 유효성 검사 결과를 부모 컴포넌트로 전달
					onValidationComplete={handleValidationComplete}
					// 스킬 데이터 초기값 설정
					skills={exSkills}
				/>
			</Modal.Body>
			<Modal.Footer>
				<Button variant="secondary" onClick={handleClose}>
					취소
				</Button>
				<Button
					variant="primary"
					disabled={!validation}
					onClick={() => handleConfirm(exSkills)}
				>
					확인
				</Button>
			</Modal.Footer>
		</Modal>
	);
};

export default SkillModalComponent;
