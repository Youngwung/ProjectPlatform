import React, { useState } from 'react';
import { Table, Button } from 'react-bootstrap';

const AlertPortfolioList = () => {
    const [portfolioAlerts, setPortfolioAlerts] = useState([
        { id: 1, title: '새 포트폴리오 1', description: '내용 A' },
        { id: 2, title: '새 포트폴리오 2', description: '내용 B' },
    ]);

    // 알람 삭제 함수
    const handleDelete = (id) => {
        setPortfolioAlerts(portfolioAlerts.filter((alert) => alert.id !== id));
    };

    return (
        <div className="d-none d-md-block">
                <Table striped bordered hover responsive>
                <thead>
                    <tr>
                        <th>체크박스</th>
                        <th>포폴아이디</th>
                        <th>제목</th>
                        <th>내용</th>
                        <th>삭제</th>
                    </tr>
                </thead>
                <tbody>
                    {portfolioAlerts.map((alert) => (
                        <tr key={alert.id}>
                            <td>체크박스</td>
                            <td>{alert.title}</td>
                            <td>{alert.description}</td>
                            <td>
                                <Button
                                    variant="danger"
                                    size="sm"
                                    onClick={() => handleDelete(alert.id)}
                                >
                                    삭제
                                </Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>
        </div>
    );
};

export default AlertPortfolioList;
