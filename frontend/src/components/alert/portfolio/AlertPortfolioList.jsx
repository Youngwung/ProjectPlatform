import React, { useState, useEffect } from "react";
import { Table, Form, Button, Badge } from "react-bootstrap";
import { HiOutlineMail, HiOutlineMailOpen } from "react-icons/hi";
import alertApi from "../../../api/alertApi";

const AlertPortfolioList = () => {
  const [portfolioAlerts, setPortfolioAlerts] = useState([]);
  const [selectedAlerts, setSelectedAlerts] = useState([]);
  const [readStatus, setReadStatus] = useState({});

  // 🔹 포트폴리오 알림 불러오기
  useEffect(() => {
    const fetchPortfolioAlerts = async () => {
      const data = await alertApi.getPortfolioAlerts();
      setPortfolioAlerts(data);

      // 🔹 초기 읽음 상태 설정
      const initialReadStatus = {};
      data.forEach((alert) => {
        initialReadStatus[alert.id] = alert.read;
      });
      setReadStatus(initialReadStatus);
    };
    fetchPortfolioAlerts();
  }, []);

  // 🔹 체크박스 선택 핸들러
  const handleCheckboxChange = (id) => {
    setSelectedAlerts((prev) =>
      prev.includes(id) ? prev.filter((alertId) => alertId !== id) : [...prev, id]
    );
  };
    // 🔹 선택된 알림 삭제
    const handleDeleteSelected = async () => {
      await Promise.all(selectedAlerts.map((id) => alertApi.deletePortfolioAlert(id)));
      setPortfolioAlerts(portfolioAlerts.filter((alert) => !selectedAlerts.includes(alert.id)));
      setSelectedAlerts([]);
    };
  // 🔹 개별 알림 읽음 처리
  const handleMarkAsRead = async (id) => {
    if (readStatus[id]) return;
    await alertApi.markPortfolioAlertAsRead(id);
    setReadStatus((prev) => ({
      ...prev,
      [id]: true,
    }));
  };

  // 🔹 전체 읽음 처리
  const handleMarkAllAsRead = async () => {
    await alertApi.markAllAlertsAsRead(false); // 포트폴리오 알림 전체 읽음
    setReadStatus((prev) => {
      const updatedStatus = { ...prev };
      Object.keys(updatedStatus).forEach((id) => {
        updatedStatus[id] = true; // UI 업데이트
      });
      return updatedStatus;
    });
  };

  return (
    <div className="email-style-alerts">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h5 className="fw-bold text-secondary">📌 포트폴리오 알림</h5>
        <Button variant="danger" size="sm" onClick={handleDeleteSelected} disabled={selectedAlerts.length === 0}>
          선택 삭제
        </Button>
      </div>

      <Table hover responsive className="table-borderless">
        <thead>
          <tr>
            <th style={{ width: "5%" }}>
              <Form.Check
                type="checkbox"
                onChange={(e) =>
                  setSelectedAlerts(e.target.checked ? portfolioAlerts.map((alert) => alert.id) : [])
                }
                checked={selectedAlerts.length === portfolioAlerts.length && portfolioAlerts.length > 0}
              />
            </th>
            <th className="text-center" style={{ width: "5%" }}>
              <HiOutlineMailOpen
                size={22}
                style={{ cursor: "pointer" }}
                onClick={handleMarkAllAsRead}
                title="모든 알림 읽음 처리"
              />
            </th>
            <th style={{ width: "7%" }}>상태</th>
            <th style={{ width: "60%", whiteSpace: "normal", overflowWrap: "break-word" }}>내용</th>
            <th className="text-end">날짜</th>
          </tr>
        </thead>
        <tbody>
          {portfolioAlerts.map((alert) => (
            <tr key={alert.id} className={readStatus[alert.id] ? "text-muted" : "fw-bold"}>
              <td className="text-center" style={{ width: "5%" }}>
                <Form.Check
                  type="checkbox"
                  onChange={(e) => {
                    e.stopPropagation();
                    handleCheckboxChange(alert.id);
                  }}
                  checked={selectedAlerts.includes(alert.id)}
                />
              </td>
              <td className="text-center" style={{ width: "5%" }} onClick={() => handleMarkAsRead(alert.id)}>
                {readStatus[alert.id] ? (
                  <HiOutlineMailOpen size={20} color="gray" />
                ) : (
                  <HiOutlineMail size={20} color="black" />
                )}
              </td>
              <td style={{ width: "7%" }}>
                <Badge bg="info" className="px-2">
                  {alert.status}
                </Badge>
              </td>
              <td style={{ width: "60%", whiteSpace: "normal", overflowWrap: "break-word" }}>{alert.content}</td>
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

export default AlertPortfolioList;
