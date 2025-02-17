import axios from "axios";

const API_URL = "http://localhost:8080/api/auth";

const authApi = {
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
  checkEmail: async (email) => {
    try {
        const response = await axios.post(`${API_URL}/check-email`, { email }, {
            headers: { "Content-Type": "application/json" } // ✅ 명시적으로 JSON 타입 지정
        });
        return response.data;  // `true` 또는 `false` 반환
    } catch (error) {
        console.error("❌ 이메일 중복 확인 오류!");

        if (error.response) {
            console.error("📌 응답 상태 코드:", error.response.status);
            console.error("📌 응답 데이터:", error.response.data);
        } else if (error.request) {
            console.error("📌 요청은 전송되었지만 응답 없음:", error.request);
        } else {
            console.error("📌 요청 설정 오류:", error.message);
        }        
        console.error("📌 요청 정보:", error.config);
        throw error;
    }
  },
  editUserInfo: async (updatedUserData) => {
    try {
      // ✅ 링크 타입 포함하여 변환
      const formattedLinks = updatedUserData.links?.map((link) => ({
        url: link.url,
        linkTypeId: link.linkTypeId || 1,
        description : link.description || "",
      })) || [];
  
      const response = await axios.put(
        `${API_URL}/updateuser`,
        { ...updatedUserData, links: formattedLinks },
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );
  
      console.log("✅ 사용자 정보 수정 성공:", response.data);
      return response.data;
    } catch (error) {
      console.error("❌ 사용자 정보 수정 실패:", error);
      throw error;
    }
  },

  // ✅ 비밀번호 변경 API
  changePassword: async (newPassword) => {
    try {
      const response = await axios.put(
        `${API_URL}/change-password`, 
        { newPassword }, 
        {
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: true, // ✅ 쿠키 기반 인증 사용
        }
      );
      console.log("✅ 비밀번호 변경 성공");
      return response.data;
    } catch (error) {
      console.error("❌ 비밀번호 변경 실패:", error);
      throw error;
    }
  },

};

export default authApi;
