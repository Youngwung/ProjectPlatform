import React from 'react';
import { Button, Modal } from 'react-bootstrap';

const ModalComponent = ({ show, handleClose, handleConfirm, description }) => {
    return (
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>확인</Modal.Title>
            </Modal.Header>
            <Modal.Body>{description}</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    취소
                </Button>
                <Button variant="primary" onClick={handleConfirm}>
                    확인
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ModalComponent;
