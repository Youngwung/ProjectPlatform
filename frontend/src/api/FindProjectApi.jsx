import axios from 'axios';

// API 기본 URL 설정
export const API_URL = 'http://localhost:8080';
const prefix = `${API_URL}/api/findProject`;

// FindProject API 함수 모듈
const findProjectApi = {
  // 전체 프로젝트 조회
  getAllProjects: async () => {
    try {
      const response = await axios.get(`${prefix}/list`);
      return response.data; // API 응답 데이터 반환
    } catch (error) {
      console.error("전체 프로젝트 조회 실패:", error);
      throw error; // 호출하는 곳에서 처리할 수 있도록 에러를 던짐
    }
  },

  // 검색어 기반 프로젝트 조회
  searchProjects: async (searchTerm) => {
    try {
      const response = await axios.get(`${prefix}/search`, {
        params: { searchTerm }, // 쿼리 매개변수 전달
      });
      return response.data;
    } catch (error) {
      console.error("프로젝트 검색 실패:", error);
      throw error;
    }
  },

  // 특정 프로젝트 상세 조회
  getProjectById: async (id) => {
    try {
      const response = await axios.get(`${prefix}/${id}`);
      return response.data;
    } catch (error) {
      console.error("프로젝트 상세 조회 실패:", error);
      throw error;
    }
  },

  // 새 프로젝트 생성
  createProject: async (projectData) => {
    try {
      const response = await axios.post(`${prefix}`, projectData);
      return response.data;
    } catch (error) {
      console.error("새 프로젝트 생성 실패:", error);
      throw error;
    }
  },

  // 기존 프로젝트 수정
  updateProject: async (id, updatedData) => {
    try {
      const response = await axios.put(`${prefix}/${id}`, updatedData);
      return response.data;
    } catch (error) {
      console.error("프로젝트 수정 실패:", error);
      throw error;
    }
  },

  // 특정 프로젝트 삭제
  deleteProject: async (id) => {
    try {
      const response = await axios.delete(`${prefix}/${id}`);
      return response.data;
    } catch (error) {
      console.error("프로젝트 삭제 실패:", error);
      throw error;
    }
  },
};

export default findProjectApi;
