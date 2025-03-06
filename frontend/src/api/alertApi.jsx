import axios from 'axios';

const AlertPortfolioApiUrl = 'http://localhost:8080/api/alert/portfolio';
const AlertProjectApiUrl = 'http://localhost:8080/api/alert/project';

const alertApi = {
  /** 🔹 유저의 모든 포트폴리오 알림 조회 */
  getPortfolioAlerts: async () => {
    try {
      const response = await axios.get(`${AlertPortfolioApiUrl}/list`, { withCredentials: true });
      console.log(response.data);
      return response.data;
    } catch (error) {
      console.error('🚨 포트폴리오 알림 조회 실패:', error);
      return [];
    }
  },
  /** 🔹 유저의 읽지 않은 포트폴리오 알림 조회 */
  getUnreadPortfolioAlerts: async () => {
    try {
      const response = await axios.get(`${AlertPortfolioApiUrl}/unread`, { withCredentials: true });
      return response.data;
    } catch (error) {
      console.error('🚨 읽지 않은 포트폴리오 알림 조회 실패:', error);
      return [];
    }
  },

  /** 🔹 특정 포트폴리오 알림 읽음 처리 */
  markPortfolioAlertAsRead: async (alertId) => {
    try {
      await axios.put(`${AlertPortfolioApiUrl}/${alertId}/read`, null, { withCredentials: true });
    } catch (error) {
      console.error(`🚨 포트폴리오 알림(${alertId}) 읽음 처리 실패:`, error);
    }
  },

  /** 🔹 특정 포트폴리오 알림 삭제 */
  deletePortfolioAlert: async (alertId) => {
    try {
      await axios.delete(`${AlertPortfolioApiUrl}/${alertId}`, { withCredentials: true });
    } catch (error) {
      console.error(`🚨 포트폴리오 알림(${alertId}) 삭제 실패:`, error);
    }
  },

  /** 🔹 유저의 모든 프로젝트 알림 조회 */
  getProjectAlerts: async () => {
    try {
      const response = await axios.get(`${AlertProjectApiUrl}/list`, { withCredentials: true });
      console.log(response.data);
      return response.data;
    } catch (error) {
      console.error('🚨 프로젝트 알림 조회 실패:', error);
      return [];
    }
  },

  /** 🔹 유저의 읽지 않은 프로젝트 알림 조회 */
  getUnreadProjectAlerts: async () => {
    try {
      const response = await axios.get(`${AlertProjectApiUrl}/unread`, { withCredentials: true });
      return response.data;
    } catch (error) {
      console.error('🚨 읽지 않은 프로젝트 알림 조회 실패:', error);
      return [];
    }
  },

  /** 🔹 특정 프로젝트 알림 읽음 처리 */
  markProjectAlertAsRead: async (alertId) => {
    try {
      await axios.put(`${AlertProjectApiUrl}/${alertId}/read`, null, { withCredentials: true });
    } catch (error) {
      console.error(`🚨 프로젝트 알림(${alertId}) 읽음 처리 실패:`, error);
    }
  },

  /** 🔹 특정 프로젝트 알림 삭제 */
  deleteProjectAlert: async (alertId) => {
    try {
      await axios.delete(`${AlertProjectApiUrl}/${alertId}`, { withCredentials: true });
    } catch (error) {
      console.error(`🚨 프로젝트 알림(${alertId}) 삭제 실패:`, error);
    }
  },

  /** 🔹 특정 알림 읽음 처리 (포트폴리오 & 프로젝트 공통) */
  markAlertAsRead: async (alertId, isProject) => {
    const apiUrl = isProject ? AlertProjectApiUrl : AlertPortfolioApiUrl;
    try {
      await axios.put(`${apiUrl}/${alertId}/read`, null, { withCredentials: true });
    } catch (error) {
      console.error(`🚨 알림(${alertId}) 읽음 처리 실패:`, error);
    }
  },

  markAllAlertsAsRead: async (isProject) => {
    const apiUrl = isProject ? AlertProjectApiUrl : AlertPortfolioApiUrl;
    try {
      await axios.put(`${apiUrl}/all/read`, null, { withCredentials: true });
      console.log(`🟢${apiUrl}에서 모든 알림 읽음 처리 완료`);
    } catch (error) {
      console.error("🚨 모든 알림 읽음 처리 실패:", error);
    }
  },

  getOneAlert: async (alertId, isProject) => {
    const apiUrl = isProject ? AlertProjectApiUrl : AlertPortfolioApiUrl;
    try {
      const response = await axios.get(`${apiUrl}/${alertId}`, { withCredentials: true });
      console.log(`🟢 알림(${alertId}) 조회 결과:`, response.data);
      return response.data;
    } catch (error) {
      console.error(`🚨 알림(${alertId}) 조회 실패:`, error);
      return null;
    }
  },

  /** 🔹 프로젝트 참가 신청 API */
  applyProject: async (projectId) => {
    try {
      await axios.post(`${AlertProjectApiUrl}/${projectId}/apply`, null, { withCredentials: true });
      console.log("🟢 프로젝트 참가 신청 완료");
    } catch (error) {
      console.error("🚨 프로젝트 참가 신청 실패:", error);
    }
  },

  /** 🔹 프로젝트 초대 API */
  inviteToProject: async (projectId, inviteeId) => {
    try {
      await axios.post(`${AlertProjectApiUrl}/${projectId}/invite/${inviteeId}`, null, { withCredentials: true });
      console.log("🟢 프로젝트 초대 전송 완료");
    } catch (error) {
      console.error("🚨 프로젝트 초대 실패:", error);
    }
  },

  /** 🔹 프로젝트 초대 수락 API */
  acceptInvite: async (projectId, inviteId) => {
    try {
      await axios.post(`${AlertProjectApiUrl}/${projectId}/invite/${inviteId}/accept`, null, { withCredentials: true });
      console.log("🟢 초대 수락 완료");
    } catch (error) {
      console.error("🚨 초대 수락 실패:", error);
    }
  },

  /** 🔹 프로젝트 초대 거절 API */
  rejectInvite: async (projectId, inviteId) => {
    try {
      await axios.post(`${AlertProjectApiUrl}/${projectId}/invite/${inviteId}/reject`, null, { withCredentials: true });
      console.log("🟢 초대 거절 완료");
    } catch (error) {
      console.error("🚨 초대 거절 실패:", error);
    }
  },

  /** 🔹 프로젝트 참가 신청 수락 API */
  acceptApplication: async (projectId, applicantId) => {
    try {
      await axios.post(`${AlertProjectApiUrl}/${projectId}/application/${applicantId}/accept`, null, { withCredentials: true });
      console.log("🟢 참가 신청 수락 완료");
    } catch (error) {
      console.error("🚨 참가 신청 수락 실패:", error);
    }
  },

  /** 🔹 프로젝트 참가 신청 거절 API */
  rejectApplication: async (projectId, applicantId) => {
    try {
      await axios.post(`${AlertProjectApiUrl}/${projectId}/application/${applicantId}/reject`, null, { withCredentials: true });
      console.log("🟢 참가 신청 거절 완료");
    } catch (error) {
      console.error("🚨 참가 신청 거절 실패:", error);
    }
  }
};

export default alertApi;
