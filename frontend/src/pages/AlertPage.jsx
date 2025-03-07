import React, { useEffect, useState } from 'react';
import AlertPortfolioList from '../components/alert/portfolio/AlertPortfolioList';
import AlertProjectList from '../components/alert/project/AlertProjectList';
import { Container, Row, Col, Button } from 'react-bootstrap';

const AlertPage = () => {
    // 프로젝트/포트폴리오 전환 상태
    const [isProject, setIsProject] = useState(true);

    // 초기 데이터 로드 및 상태 관리
    useEffect(() => {
        // console.log("🔔 AlertPage loaded");
        return () => {
            // console.log("🔕 AlertPage unmounted");
        };
    }, []);

    return (
        <Container fluid className="mt-4"> {/* ✅ 전체 너비 적용 */}
            
            {/* ✅ 상단 중앙 정렬된 버튼 */}
            <Row className="justify-content-center mb-3">
                <Col xs={12} md={8} lg={6} className="text-center">
                    <Button 
                        variant={isProject ? "primary" : "outline-primary"} 
                        className="me-2 px-4 py-2 fw-bold"
                        onClick={() => setIsProject(true)}
                    >
                        프로젝트 알람
                    </Button>
                    <Button 
                        variant={!isProject ? "success" : "outline-success"} 
                        className="px-4 py-2 fw-bold"
                        onClick={() => setIsProject(false)}
                    >
                        포트폴리오 알람
                    </Button>
                </Col>
            </Row>

            {/* ✅ 알림 목록 */}
            <Row >
                <Col md={12}>
                    {isProject ? <AlertProjectList /> : <AlertPortfolioList />}
                </Col>
            </Row>
        </Container>
    );
};

export default AlertPage;


//TODO  AlertProjectList.jsx 와  AlertPortfolioList.jsx 의 삭제 버튼 클릭 이벤트 처리와 삭제 로직 구현 또한 각각의 isRead상태를 표시를 하는것을 작성

// TODO 페이징처리와 검색기능 구현은 보류