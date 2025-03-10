import axios from "axios";

export const API_URL = "http://localhost:8080";
const linkPrefix = `${API_URL}/api/link`;
const linkTypePrefix = `${API_URL}/api/linkType`;

const linkApi = {
  getAllLinks: async () => {
    try {
      const response = await axios.get(`${linkPrefix}/list`);
      //console.log("서버에서 데이터 뿌려주기")
      return response.data;
    } catch (error) {
      console.error("전체 링크 조회 실패:", error);
      throw error;
    }
  },
  getOneLink: async (id) => {
    try {
      const response = await axios.get(`${linkPrefix}/list/${id}`);
      return response.data;
    } catch (error) {
      console.error("링크 조회 실패:", error);
      throw error;
    }
  },
  createLink: async (linkData) => {
    try {
      const response = await axios.post(`${linkPrefix}/create`, linkData);
      //console.log(linkData);
      return response.data;
    } catch (error) {
      console.error("새 링크 생성 실패:", error);
      throw error;
    }
  },
  updateLink: async (id, updatedData) => {
    try {
      const response = await axios.put(`${linkPrefix}/${id}`, updatedData);
      //console.log("updatedData:", updatedData);
      return response.data;
    } catch (error) {
      console.error("링크 수정 실패:", error);
      throw error;
    }
  },
  deleteLink: async (id, userId) => {
    try {
      // axios.delete의 두 번째 인자에 params 객체로 userId 전달
      const response = await axios.delete(`${linkPrefix}/${id}`, {
        params: { userId: userId }
      });
      return response.data;
    } catch (error) {
      console.error("링크 삭제 실패:", error);
      throw error;
    }
  },

  getAllLinkTypes: async () => {
    try {
      const response = await axios.get(`${linkTypePrefix}/list`);
      return response.data;
    } catch (error) {
      console.error("링크 타입 조회 실패:", error);
      throw error;
    }
  },
};

export default linkApi;
