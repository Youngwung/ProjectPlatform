import React from "react";
import { Button, Modal } from "react-bootstrap";

export default function LoginModal({show, handleClose, handleConfirm}) {
	return (
		<div>
			<Modal show={show} onHide={handleClose}>
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
