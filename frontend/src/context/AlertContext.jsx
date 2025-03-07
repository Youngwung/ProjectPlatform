import React, { createContext, useState, useEffect } from "react";
import alertApi from "../api/alertApi";

export const AlertContext = createContext();

export const AlertProvider = ({ children }) => {
  // 프로젝트, 포트폴리오 알림 상태 (기본적으로 상위 5개 unread 알림)
  const [projectAlerts, setProjectAlerts] = useState([]);
  const [portfolioAlerts, setPortfolioAlerts] = useState([]);

  // 🔹 상위 5개의 unread 프로젝트 알림 조회
  const fetchUnReadProjectAlerts = async () => {
    try {
      const data = await alertApi.getUnreadProjectAlerts();
      const formattedAlerts = data.slice(0, 5).map((alert) => ({
        id: alert.id,
        content: alert.content,
        status: alert.status,
        createdAt: new Date(alert.createdAt).toLocaleString(),
        isRead: alert.isRead,
        projectTitle : alert.project.title,
      }));
      setProjectAlerts(formattedAlerts);
    } catch (error) {
      console.error("프로젝트 알림 가져오기 실패:", error);
    }
  };

  // 🔹 상위 5개의 unread 포트폴리오 알림 조회
  const fetchUnreadPortfolioAlerts = async () => {
    try {
      const data = await alertApi.getUnreadPortfolioAlerts();
      const formattedAlerts = data.slice(0, 5).map((alert) => ({
        id: alert.id,
        content: alert.content,
        status: alert.status,
        createdAt: new Date(alert.createdAt).toLocaleString(),
        isRead: alert.isRead,
      }));
      setPortfolioAlerts(formattedAlerts);
    } catch (error) {
      console.error("포트폴리오 알림 가져오기 실패:", error);
    }
  };

  // 🔹 컴포넌트 마운트 시 초기 알림 데이터 조회
  useEffect(() => {
    fetchUnReadProjectAlerts();
    fetchUnreadPortfolioAlerts();
  }, []);

  // 🔹 동적 업데이트: 30초마다 최신 상태를 반영
  // (알림이 실시간으로 업데이트되지 않는 경우를 대비하여 추가)
  //TODO : 5분마다 최신 상태를 반영하도록 수정 (배포할때)
  useEffect(() => {
    const interval = setInterval(() => {
      fetchUnReadProjectAlerts();
      fetchUnreadPortfolioAlerts();
    }, 300000);
    return () => clearInterval(interval);
  }, []);

  // 🔹 알림 읽음 처리 함수 (API 호출 후 해당 알림 제거)
  const markAlertAsRead = async (alertId, isProject) => {
    try {
      await alertApi.markAlertAsRead(alertId, isProject);
      if (isProject) {
        setProjectAlerts((prev) =>
          prev.filter((alert) => alert.id !== alertId)
        );
      } else {
        setPortfolioAlerts((prev) =>
          prev.filter((alert) => alert.id !== alertId)
        );
      }
    } catch (error) {
      console.error("알림 읽음 처리 실패:", error);
    }
  };

  // 🔹 전체 프로젝트 알림 조회 (모든 알림을 반환)
  const getProjectAlerts = async () => {
    try {
      const data = await alertApi.getProjectAlerts();
      //console.log("전체 프로젝트 알림:", data);
      return data;
    } catch (error) {
      console.error("🚨 프로젝트 알림 조회 실패:", error);
      return [];
    }
  };

  // 🔹 전체 포트폴리오 알림 조회 (모든 알림을 반환)
  const getPortfolioAlerts = async () => {
    try {
      const data = await alertApi.getPortfolioAlerts();
      // console.log("전체 포트폴리오 알림:", data);
      return data;
    } catch (error) {
      console.error("🚨 포트폴리오 알림 조회 실패:", error);
      return [];
    }
  };

  // 전역 알림 상태를 새로 고침하는 함수 추가
  const refreshAlerts = async () => {
    await fetchUnReadProjectAlerts();
    await fetchUnreadPortfolioAlerts();
  };

  return (
    <AlertContext.Provider
      value={{
        projectAlerts,
        portfolioAlerts,
        fetchUnReadProjectAlerts,
        fetchUnreadPortfolioAlerts,
        markAlertAsRead,
        getProjectAlerts,        // 전체 프로젝트 알림 조회 함수 추가
        getPortfolioAlerts,     // 전체 포트폴리오 알림 조회 함수 추가
        refreshAlerts,          // 전역 알림 상태를 새로 고침하는 함수 추가
      }}
    >
      {children}
    </AlertContext.Provider>
  );
};
