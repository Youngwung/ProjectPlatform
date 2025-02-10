import React, { useState } from 'react';
import { Table, Button } from 'react-bootstrap';

const AlertProjectList = () => {
    const [projectAlerts, setProjectAlerts] = useState([
        { id: 1,userId: 1,projectId:1,content:"contant",createAt:"",isRead:false},
        { id: 2,userId: 1,projectId:1,content:"contant",createAt:"",isRead:false},
        { id: 3,userId: 1,projectId:1,content:"contant",createAt:"",isRead:false},
    ]);

    // 알람 삭제 함수
    const handleDelete = (id) => {
        setProjectAlerts(projectAlerts.filter((alert) => alert.id !== id));
    };

    return (
        <div className="d-none d-md-block">
                <Table striped bordered hover responsive>
                <thead>
                    <tr>
                        <th>체크박스</th>
                        <th>내용</th>
                        <th>시간</th>
                        <th>삭제</th>
                    </tr>
                </thead>
                <tbody>
                    {projectAlerts.map((alert) => (
                        <tr key={alert.id}>
                            <td>첵박</td>
                            <td>{alert.content}</td>
                            <td>{alert.createAt}</td>
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

export default AlertProjectList;
