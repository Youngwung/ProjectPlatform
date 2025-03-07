import React, { useEffect, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

export default function LoginModal({parentShow, redirectUrl}) {
	const [show, setShow] = useState(parentShow);
	const navigate = useNavigate();
	const handleClose = () => {
		setShow(false);
		navigate({pathname: "/"});
	}
	const handleConfirm = () => {
		setShow(false);
		navigate({
			pathname: "/login",
			search: `redirectUrl=${redirectUrl}`
		})
	}

	useEffect(() => {
		setShow(parentShow)
	
	}, [parentShow])
	
	
	return (
		<div>
			<Modal show={show} onHide={handleClose} backdrop="static" keyboard={false}>
				<Modal.Header closeButton>
					<Modal.Title>확인</Modal.Title>
				</Modal.Header>
				<Modal.Body>로그인이 필요한 서비스입니다. 로그인 페이지로 이동하시겠습니까?</Modal.Body>
				<Modal.Footer>
					<Button variant="secondary" onClick={handleClose}>
						취소
					</Button>
					<Button variant="primary" onClick={handleConfirm}>
						확인
					</Button>
				</Modal.Footer>
			</Modal>
		</div>
	);
}
