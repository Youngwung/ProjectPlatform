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
  updateUserExperience: async (updatedData) => {
    try {
        console.log("📌 서버로 전송할 원본 데이터:", updatedData);

        // ✅ id 값이 올바른 숫자인지 확인 후 변환
        const requestData = {
            id: updatedData.id ? parseInt(updatedData.id, 10) : null, // NaN 방지
            experience: updatedData.experience ? String(updatedData.experience) : null // 기본값 설정
        };

        console.log("📡 백엔드로 전송할 데이터:", requestData); // ✅ 전송되는 데이터 확인

        const response = await axios.put(`${API_URL}/updatedExperience`, requestData, {
            headers: { "Content-Type": "application/json" },
            withCredentials: true // 🔥 JWT 쿠키 포함
        });

        console.log("✅ 서버 응답:", response.data);
        return response.data;
    } catch (error) {
        console.error("🚨 유저 경험 수정 실패", error);
        throw error;
    }
  },



  // ✅ 비밀번호 검증 API (verifyPassword 함수)
  verifyPassword: async ({ password }) => {
    try {
      // 전달받은 값에 대해 상세 로그 기록 (비밀번호는 민감 정보이므로 존재 여부만 체크)
      console.log(
        `🔍 verifyPassword 호출됨 - passwordProvided: ${password ? 'YES' : 'NO'}`
      );

      // userId는 서버에서 쿠키(accessToken)를 통해 추출하므로 클라이언트에서는 password만 전달합니다.
      const response = await axios.post(
        `${API_URL}/verify-password`,
        { password },
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true, // 쿠키 기반 인증
        }
      );

      // 서버로부터 받은 응답을 꼼꼼하게 로그로 기록 (응답 데이터가 boolean 형태라고 가정)
      console.log("✅ 비밀번호 검증 응답 데이터:", response.data);
      
      // 응답 데이터가 true 또는 false인지 확인 후 반환
      if (typeof response.data !== "boolean") {
        console.warn("⚠️ 예상과 다른 응답 데이터 형식:", response.data);
      }
      
      return response.data;
    } catch (error) {
      // 에러가 발생했을 경우 상세 로그 기록 (에러 객체의 message와 response 정보 포함)
      console.error("❌ 비밀번호 검증 실패:", {
        errorMessage: error.message,
        errorResponse: error.response ? error.response.data : "No response data",
      });
      throw error;
    }
  },
  // ✅ 비밀번호 변경 API (현재 비밀번호와 새 비밀번호 모두 전송)
  changePassword: async (password, newPassword) => {
    try {
      // 비밀번호 제공 여부 로그 (민감 정보는 출력하지 않음)
      console.log(
        `%c[DEBUG] changePassword 호출됨 - 현재 비밀번호 제공: ${password ? 'YES' : 'NO'}, 새 비밀번호 제공: ${newPassword ? 'YES' : 'NO'}`,
        'color: green; font-weight: bold;'
      );

      const response = await axios.put(
        `${API_URL}/change-password`, 
        { 
          password,    // 현재 비밀번호 (검증용)
          newPassword  // 새 비밀번호 (변경할 값)
        }, 
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true, // 쿠키 기반 인증 사용
        }
      );

      // 응답 데이터 로깅
      console.log(
        `%c[INFO] 비밀번호 변경 성공, 응답 데이터: `,
        'color: blue; font-weight: bold;',
        response.data
      );
      return response.data;
    } catch (error) {
      // 에러 메시지와 추가 정보를 로깅
      console.error(`[ERROR] 비밀번호 변경 실패: ${error.message}`);
      if (error.response) {
        console.error(
          `[ERROR] 상태 코드: ${error.response.status}, 응답 데이터: `,
          error.response.data
        );
      } else {
        console.error(`[ERROR] 응답 데이터 없음`);
      }
      throw error;
    }
  },
  // ✅ 회원 탈퇴 API
  deleteUser: async () => {
    try {
      const response = await axios.delete(`${API_URL}/deleteuser`, {
        withCredentials: true, // ✅ JWT 쿠키 포함
      });
      console.log("✅ 회원 탈퇴 성공:", response.data);
      return response.data;
    } catch (error) {
      console.error("❌ 회원 탈퇴 실패:", error);
      throw error;
    }
  },
};

export default authApi;
