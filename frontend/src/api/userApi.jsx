import axios from 'axios';

export const API_URL = 'http://localhost:8080';
const prefix = `${API_URL}/api`;

const userApi = {
    getAllUsers: async () => {
      try {
        const response = await axios.get(`${prefix}/user`);
        return response.data;
      } catch (error) {
        console.error("전체 유저 조회 실패:", error);
        throw error;
      }
    },
    getUserById: async (id) => {
        try {
            const response = await axios.get(`${prefix}/user/${id}`);
            return response.data;
        } catch (error) {
            console.error("유저 상세 조회 실패:", error);
            throw error;
        }
    },
    createUser: async (userData) => {
        try {
        const response = await axios.post(`${prefix}/user`, userData);
        return response.data;
        } catch (error) {
        console.error("새 유저 생성 실패:", error);
        throw error;
        }
    },
    updateUser: async (id, updatedData) => {
        try {
        const response = await axios.put(`${prefix}/user/${id}`, updatedData);
        return response.data;
        } catch (error) {
        console.error("유저 수정 실패:", error);
        throw error;
        }
    },
    deleteUser: async (id) => {
        try {
        const response = await axios.delete(`${prefix}/user/${id}`);
        return response.data;
        } catch (error) {
        console.error("유저 삭제 실패:", error);
        throw error;
        }
    },
}
export default userApi;