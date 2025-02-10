import axios from "axios";

const API_URL = "http://localhost:8080/api/auth";

const AuthApi = {
  // ✅ 로그인 요청 (쿠키 기반)
  login: async (email, password) => {
    try {
      const response = await axios.post(
        `${API_URL}/login`,
        { email, password },
        { withCredentials: true } // ✅ 쿠키 포함 요청
      );

      console.log("📢 로그인 성공, 응답 데이터:", response.data); // ✅ 응답 데이터 확인
      return response.data;
    } catch (error) {
      console.error("❌ 로그인 실패:", error);
      throw error;
    }
  },

  // ✅ 로그아웃 요청 (쿠키 삭제)
  logout: async () => {
    try {
      await axios.post(`${API_URL}/logout`, {}, { withCredentials: true });
      console.log("✅ 로그아웃 성공 (쿠키 삭제)");
      return true;
    } catch (error) {
      console.error("❌ 로그아웃 실패:", error);
      throw error;
    }
  },

  // ✅ 현재 로그인된 사용자 정보 조회
  getAuthenticatedUser: async () => {
    try {
      const response = await axios.get(`${API_URL}/getAuthenticatedUser`, {
        withCredentials: true, // ✅ JWT 쿠키 포함 요청
      });

      console.log("✅ 사용자 정보:", response.data);
      return response.data;
    } catch (error) {
      if (error.response && error.response.status === 401) {
        console.log("🚨 인증되지 않은 사용자 (401 Unauthorized)");
        return null;
      }
      console.error("🚨 사용자 정보 조회 실패:", error);
      throw error;
    }
  },
};

export default AuthApi;
