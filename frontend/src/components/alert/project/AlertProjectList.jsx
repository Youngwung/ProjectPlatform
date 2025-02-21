import React, { useState, useEffect } from "react";
import { Table, Form, Button, Badge } from "react-bootstrap";
import alertApi from "../../../api/alertApi";

const AlertProjectList = () => {
  const [projectAlerts, setProjectAlerts] = useState([]);
  const [selectedAlerts, setSelectedAlerts] = useState([]);

  // 🔹 프로젝트 알림 불러오기
  useEffect(() => {
    const fetchProjectAlerts = async () => {
      const data = await alertApi.getProjectAlerts();
      setProjectAlerts(data);
    };
    fetchProjectAlerts();
  }, []);

  // 🔹 체크박스 선택 핸들러
  const handleCheckboxChange = (id) => {
    setSelectedAlerts((prev) =>
      prev.includes(id) ? prev.filter((alertId) => alertId !== id) : [...prev, id]
    );
  };

  // 🔹 선택된 알림 삭제
  const handleDeleteSelected = async () => {
    await Promise.all(selectedAlerts.map((id) => alertApi.deleteProjectAlert(id)));
    setProjectAlerts(projectAlerts.filter((alert) => !selectedAlerts.includes(alert.id)));
    setSelectedAlerts([]);
  };

  // 🔹 알림 클릭 시 읽음 처리
  const handleMarkAsRead = async (e, id) => {
    e.stopPropagation(); // ✅ 체크박스 클릭과 구분
    await alertApi.markProjectAlertAsRead(id);
    setProjectAlerts((prev) =>
      prev.map((alert) => (alert.id === id ? { ...alert, isRead: true } : alert))
    );
  };

  return (
    <div className="email-style-alerts">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h5 className="fw-bold text-secondary">📌 프로젝트 알림</h5>
        <Button
          variant="danger"
          size="sm"
          onClick={handleDeleteSelected}
          disabled={selectedAlerts.length === 0}
        >
          선택 삭제
        </Button>
      </div>

      <Table hover responsive className="table-borderless">
        <thead>
          <tr>
            <th>
              <Form.Check
                type="checkbox"
                onChange={(e) =>
                  setSelectedAlerts(e.target.checked ? projectAlerts.map((alert) => alert.id) : [])
                }
                checked={selectedAlerts.length === projectAlerts.length && projectAlerts.length > 0}
              />
            </th>
            <th>상태</th>
            <th>내용</th>
            <th className="text-end">날짜</th>
          </tr>
        </thead>
        <tbody>
          {projectAlerts.map((alert) => (
            <tr
              key={alert.id}
              className={alert.isRead ? "text-muted" : "fw-bold"}
              onClick={(e) => handleMarkAsRead(e, alert.id)}
              style={{ cursor: "pointer" }}
            >
              <td>
                <Form.Check
                  type="checkbox"
                  onChange={(e) => {
                    e.stopPropagation();
                    handleCheckboxChange(alert.id);
                  }}
                  checked={selectedAlerts.includes(alert.id)}
                />
              </td>
              <td>
                <Badge bg={alert.isRead ? "secondary" : "primary"} className="px-2">
                  {alert.isRead ? "✔ 읽음" : "❗ 안 읽음"}
                </Badge>
              </td>
              <td>{alert.content}</td>
              <td className="text-end">
                <small className="text-muted">{new Date(alert.createdAt).toLocaleString()}</small>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default AlertProjectList;
