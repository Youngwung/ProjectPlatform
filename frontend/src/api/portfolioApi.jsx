import axios from "axios";

// API 기본 URL 설정
const prefix = `${process.env.REACT_APP_API_URL}/api/portfolio`;
// Axios 인스턴스 생성 (쿠키 포함)
const axiosInstance = axios.create({
	baseURL: prefix,
	withCredentials: true, // ✅ 쿠키 자동 포함
});

// portfolio API 함수 모듈
const portfolioApi = {
	// 프로젝트 하나만 조회
	getOne: async (id) => {
		try {
			const response = await axiosInstance.get(`/list/${id}`);
			return { ...response.data }; // 기본 상태와 병합하여 반환
		} catch (error) {
			console.error("❌ 프로젝트 조회 실패:", error);
			throw error;
		}
	},

	// 전체 프로젝트 조회
	getAllProjects: async () => {
		try {
			const response = await axiosInstance.get("/list");
			return response.data;
		} catch (error) {
			console.error("❌ 전체 프로젝트 조회 실패:", error);
			throw error;
		}
	},

	// 검색어 기반 프로젝트 조회
	searchProjects: async (searchTerm) => {
		try {
			const response = await axiosInstance.get("/search", {
				params: { searchTerm },
			});
			return response.data;
		} catch (error) {
			console.error("❌ 프로젝트 검색 실패:", error);
			throw error;
		}
	},

	// 새 프로젝트 생성 (쿠키 인증 포함)
	createProject: async (projectData) => {
		try {
			const response = await axiosInstance.post("/create", projectData);
			//console.log(response.data);
			return response.data;
		} catch (error) {
			console.error("❌ 새 프로젝트 생성 실패:", error);
			throw error;
		}
	},

	// 기존 프로젝트 수정
	updateProject: async (id, updatedData) => {
		try {
			const response = await axiosInstance.put(`/${id}`, updatedData, {
				withCredentials: true,
			});
			return response.data;
		} catch (error) {
			console.error("❌ 프로젝트 수정 실패:", error);
			throw error;
		}
	},

	// 특정 프로젝트 삭제
	deleteProject: async (id) => {
		try {
			const response = await axiosInstance.delete(`/${id}`, {
				withCredentials: true,
			});
			return response.data;
		} catch (error) {
			console.error("❌ 프로젝트 삭제 실패:", error);
			throw error;
		}
	},

	portfolioSearch: async (params) => {
		const { page, size, query, querySkills, sortOption } = params;
		let searchParams = new URLSearchParams();
		searchParams.append("page", page);
		searchParams.append("size", size);
		searchParams.append("query", query);
		searchParams.append("sortOption", sortOption);

		try {
			if (querySkills && querySkills.length > 0) {
				querySkills.forEach((skill) => {
					searchParams.append("querySkills", skill);
				});
			} else {
				searchParams.append("querySkills", "");
			}
			const res = await axiosInstance.get(`${prefix}/search?${searchParams}`);
			return res.data;
		} catch (error) {
			console.error("프로젝트 검색 실패", error);
      throw error;
		}
	},
	getMyPortfolios: async () => {
		try {
			const response = await axiosInstance.get(`${prefix}/my`, { withCredentials: true });
			return response.data;
		} catch (error) {
			console.error("내 포트폴리오 조회 실패:", error);
			throw error;
		}
	},

	getListForMain: async () => {
		try {
			const response = await axios.get(`${prefix}/main`);
			return response.data
		} catch (error) {
			console.error(error);
			throw error;
		}
	},

	checkPortfolioWriter: async (portfolioId) => {
		try {
			const res = await axiosInstance.get(`${prefix}/checkPortfolioWriter/${portfolioId}`);
			return res.data;
		} catch (error) {
			console.error(error);
		}
	}
};

export default portfolioApi;
