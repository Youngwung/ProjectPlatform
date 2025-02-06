import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

const AuthApi = {
  // 로그인 요청
  login: async (email, password) => {
    try {
      const response = await axios.post(
        `${API_URL}/login`,
        { email, password },
        { withCredentials: true } // 쿠키를 포함하여 요청
      );
      return response.data; // 서버에서 반환된 데이터 (성공 메시지)
    } catch (error) {
      console.error("로그인 실패:", error);
    }
  },

  // 로그아웃 요청 (쿠키 삭제)
  logout: async () => {
    try {
      await axios.post(`${API_URL}/logout`, {}, { withCredentials: true });
      return true;
    } catch (error) {
      console.error("로그아웃 실패:", error);
      throw error;
    }
  },

  // 인증된 사용자 정보 조회
  getAuthenticatedUser: async () => {
    try {
      const response = await axios.get(`${API_URL}/getAuthenticatedUser`, {
        withCredentials: true, // 쿠키를 포함하여 요청
      });
      return response.data; // 사용자 정보 반환
    } catch (error) {
      console.error("사용자 정보 조회 실패:", error);
      throw error;
    }
  },
};

export default AuthApi;
