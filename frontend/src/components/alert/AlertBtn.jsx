import React, { useState } from 'react';
import { Row, Col, Dropdown, Badge, ListGroup, Button } from 'react-bootstrap';
import { FaBell} from 'react-icons/fa';

const AlertBtn = () => {
    // 🔹 알람 데이터
    const [newProjectList, setNewProjectList] = useState([
        "새 프로젝트 1", "새 프로젝트 2", "새 프로젝트 3"
    ]);
    const [newPortfolioList, setNewPortfolioList] = useState([
        "새 포트폴리오 1", "새 포트폴리오 2"
    ]);

    // 🔹 상태 관리
    const [showDropdown, setShowDropdown] = useState(false);
    const [showProjectAlerts, setShowProjectAlerts] = useState(false);
    const [showPortfolioAlerts, setShowPortfolioAlerts] = useState(false);

    return (
        <Dropdown show={showDropdown} onToggle={(isOpen) => setShowDropdown(isOpen)}>
            {/* 알람 버튼 */}
            <Dropdown.Toggle
                variant="light"
                id="alert-dropdown"
                className="d-flex align-items-center position-relative"
            >
                <FaBell size={20} />
                {/* 알람 개수 표시 */}
                {(newProjectList.length + newPortfolioList.length > 0) && (
                    <Badge
                        bg="danger"
                        pill
                        className="position-absolute top-0 start-100 translate-middle"
                    >
                        {newProjectList.length + newPortfolioList.length}
                    </Badge>
                )}
            </Dropdown.Toggle>

            {/* 드롭다운 메뉴 */}
            <Dropdown.Menu align="end" className="p-3">
                {/* 프로젝트 및 포트폴리오 버튼 */}
                <Row className="align-items-center">
                    <Col className="d-flex justify-content-between">
                        <Button
                            variant="outline-primary"
                            onClick={() => setShowProjectAlerts(!showProjectAlerts)}
                        >
                            프로젝트 <Badge bg="primary">{newProjectList.length}</Badge>
                        </Button>
                        <Button
                            variant="outline-success"
                            onClick={() => setShowPortfolioAlerts(!showPortfolioAlerts)}
                        >
                            포트폴리오 <Badge bg="success">{newPortfolioList.length}</Badge>
                        </Button>
                    </Col>
                </Row>

                {/* 프로젝트 알람 리스트 */}
                {showProjectAlerts && (
                    <>
                        <Dropdown.Divider />
                        <ListGroup variant="flush">
                            {newProjectList.length > 0 ? (
                                newProjectList.map((alert, index) => (
                                    <ListGroup.Item key={index}>{alert}</ListGroup.Item>
                                ))
                            ) : (
                                <ListGroup.Item className="text-muted">새 알람 없음</ListGroup.Item>
                            )}
                        </ListGroup>
                    </>
                )}

                {/* 포트폴리오 알람 리스트 */}
                {showPortfolioAlerts && (
                    <>
                        <Dropdown.Divider />
                        <ListGroup variant="flush">
                            {newPortfolioList.length > 0 ? (
                                newPortfolioList.map((alert, index) => (
                                    <ListGroup.Item key={index}>{alert}</ListGroup.Item>
                                ))
                            ) : (
                                <ListGroup.Item className="text-muted">새 알람 없음</ListGroup.Item>
                            )}
                        </ListGroup>
                    </>
                )}
            </Dropdown.Menu>
        </Dropdown>
    );
};

export default AlertBtn;
