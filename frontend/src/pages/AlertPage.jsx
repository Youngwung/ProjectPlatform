import React, { useEffect, useState } from 'react';
import AlertPortfolioList from '../components/alert/portfolio/AlertPortfolioList';
import AlertProjectList from '../components/alert/project/AlertProjectList';
import { Container } from 'react-bootstrap';

const AlertPage = () => {
    // 프로젝트/포트폴리오 전환 상태
    const [isProject, setIsProject] = useState(true);

    // 초기 데이터 로드 및 상태 관리
    useEffect(() => {
        console.log("AlertPage loaded");
        return () => {
            console.log("AlertPage unmounted");
        };
    }, []);

    return (
        <Container>
        <div className="">
            <h1>알람</h1>

            {/* 버튼을 통해 전환 */}
            <div className="mb-3">
                <button
                    className={`btn ${isProject ? "btn-primary" : "btn-outline-primary"} me-2`}
                    onClick={() => setIsProject(true)}
                >
                    프로젝트 알람
                </button>
                <button
                    className={`btn ${!isProject ? "btn-success" : "btn-outline-success"}`}
                    onClick={() => setIsProject(false)}
                >
                    포트폴리오 알람
                </button>
            </div>

            {/* 알람 리스트 */}
            <div className="d-flex justify-content-between">
                <section>
                    {isProject ? (
                        <AlertProjectList />
                    ) : (
                        <AlertPortfolioList />
                    )}
                </section>
            </div>
        </div>
        </Container>
    );
};

export default AlertPage;
