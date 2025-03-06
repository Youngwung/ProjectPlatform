import React, { useContext, useEffect, useState } from "react";
import { AlertContext } from "../../../context/AlertContext";
import { Table, Form, Button, Badge } from "react-bootstrap";
import { HiOutlineMail, HiOutlineMailOpen } from "react-icons/hi";
import { useNavigate } from "react-router-dom";
import alertApi from "../../../api/alertApi";

const AlertProjectList = () => {
  const { getProjectAlerts, markAlertAsRead } = useContext(AlertContext);
  const [allProjectAlerts, setAllProjectAlerts] = useState([]);
  const [selectedAlerts, setSelectedAlerts] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    // 전체 프로젝트 알림 데이터를 가져오는 함수 호출
    const fetchAllAlerts = async () => {
      const alerts = await getProjectAlerts();
      setAllProjectAlerts(alerts);
    };
    fetchAllAlerts();
  }, [getProjectAlerts]);

  const handleCheckboxChange = (id) => {
    setSelectedAlerts((prev) =>
      prev.includes(id) ? prev.filter((alertId) => alertId !== id) : [...prev, id]
    );
  };

  const handleAlertClick = async (id) => {
    await markAlertAsRead(id, true);
    navigate(`/mypage/alert/project/${id}`);
  };
   // 선택 삭제 함수: 선택된 알림들을 삭제 API를 통해 삭제하고, 상태 업데이트
   const handleDeleteSelected = async () => {
    try {
      await Promise.all(
        selectedAlerts.map((id) => alertApi.deleteProjectAlert(id))
      );
      // 삭제 후, 삭제되지 않은 알림들만 필터링해서 상태 업데이트
      setAllProjectAlerts(
        allProjectAlerts.filter((alert) => !selectedAlerts.includes(alert.id))
      );
      setSelectedAlerts([]);
    } catch (error) {
      console.error("선택 삭제 실패:", error);
      alert("선택 삭제 처리 중 오류가 발생했습니다.");
    }
  };
  
  return (
    <div className="email-style-alerts">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h5 className="fw-bold text-secondary">📌 전체 프로젝트 알림</h5>
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
            <th style={{ width: "5%" }}>
              <Form.Check
                type="checkbox"
                onChange={(e) =>
                  setSelectedAlerts(
                    e.target.checked
                      ? allProjectAlerts.map((alert) => alert.id)
                      : []
                  )
                }
                checked={
                  selectedAlerts.length === allProjectAlerts.length &&
                  allProjectAlerts.length > 0
                }
              />
            </th>
            <th className="text-center" style={{ width: "5%" }}>
              <HiOutlineMailOpen
                size={22}
                style={{ cursor: "pointer" }}
                onClick={async () => {
                  await Promise.all(
                    allProjectAlerts.map((alert) => markAlertAsRead(alert.id, true))
                  );
                }}
                title="모든 알림 읽음 처리"
              />
            </th>
            <th style={{ width: "7%" }}>상태</th>
            <th style={{ width: "60%", whiteSpace: "normal", overflowWrap: "break-word" }}>
              내용
            </th>
            <th className="text-end">날짜</th>
          </tr>
        </thead>
        <tbody>
          {allProjectAlerts.map((alert) => (
            <tr
              key={alert.id}
              className={alert.isRead ? "text-muted" : "fw-bold"}
            >
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
              <td
                className="text-center"
                style={{ width: "5%" }}
                onClick={() => handleAlertClick(alert.id)}
              >
                {alert.isRead ? (
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
              <td
                style={{
                  width: "60%",
                  whiteSpace: "normal",
                  overflowWrap: "break-word",
                  cursor: "pointer",
                }}
                onClick={() => handleAlertClick(alert.id)}
              >
                {alert.content}
              </td>
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
