import axios from 'axios';

export const API_URL = 'http://localhost:8080';
const prefix = `${API_URL}/api/user`;

const userApi = {
    getAllUsers: async () => {
      try {
        const response = await axios.get(`${prefix}/list`);
        return response.data;
      } catch (error) {
        console.error("전체 유저 조회 실패:", error);
        throw error;
      }
    },
    getUserById: async (id) => {
        try {
            const response = await axios.get(`${prefix}/list/${id}`);
            return response.data;
        } catch (error) {
            console.error("유저 상세 조회 실패:", error);
            throw error;
        }
    },
    createUser: async (userData) => {
        try {
        const response = await axios.post(`${prefix}/create`, userData);
        console.log(response);
        return response.data;
        } catch (error) {
        console.error("새 유저 생성 실패:", error);
        throw error;
        }
    },
    updateUser: async (id, updatedData) => {
        try {
        const response = await axios.put(`${prefix}/list/${id}`, updatedData);
        return response.data;
        } catch (error) {
        console.error("유저 수정 실패:", error);
        throw error;
        }
    },
    deleteUser: async (id) => {
        try {
        const response = await axios.delete(`${prefix}/list/${id}`);
        return response.data;
        } catch (error) {
        console.error("유저 삭제 실패:", error);
        throw error;
        }
    },
    // AUTH로 이식
    // checkEmail: async (email) => {
    //     try {
    //         const response = await axios.post(`${prefix}/check-email`, email);
    //         return response.data;  // `true` 또는 `false` 반환
    //     } catch (error) {
    //         console.error("❌ 이메일 중복 확인 오류!");

    //         if (error.response) {
    //             console.error("📌 응답 상태 코드:", error.response.status);
    //             console.error("📌 응답 데이터:", error.response.data);
    //         } else if (error.request) {
    //             console.error("📌 요청은 전송되었지만 응답 없음:", error.request);
    //         } else {
    //             console.error("📌 요청 설정 오류:", error.message);
    //         }        
    //         console.error("📌 요청 정보:", error.config);
    //         throw error;
    //     }
    // },
    // login : async (email,password) => {
    //     try {
    //         const response = await axios.post(`${prefix}/login`, {email, password});
    //         console.log(response);
    //         return response.data;
    //     } catch (error) {
    //         if (error.response && error.response.status !== 401) {
    //             console.error("❌ 서버 오류!");
    //         }
    //     }
        
    // },

};
export default userApi;