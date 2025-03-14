// api서버를 호출하는 주소를 변수로 선언해서 사용.
// 서버가 바뀌는 경우 여기만 수정해주면 됨.

import axios from "axios";

const projectPrefix = `${process.env.REACT_APP_API_URL}/api/bookmark/project`;
const portfolioPrefix = `${process.env.REACT_APP_API_URL}/api/bookmark/portfolio`;

const axiosInstance = axios.create({
	baseURL: portfolioPrefix,
	withCredentials: true, // ✅ 쿠키 자동 포함
});

// 페이징 처리된 글 목록을 가져오는 비동기 통신 메서드
export const getBookmarkProjectList = async (pageParam) => {
	const { page, size } = pageParam;

	const res = await axiosInstance.get(`${projectPrefix}/list`, {
		params: { page, size },
	});
	// 비동기 통신에서 쿼리스트링을 사용하는 방법
	// 1. pageParam의 page와 size를 빼둔 후 두번째 아규먼트로  {params:{page, size}} 로 전달.
	// 2. pageParam을 그대로 전달. {params:{...pageParam}} => .3개임

	return res.data;
};

// 등록 api를 호출하는 비동기 통신 메서드
export const postBookmarkProjectAdd = async (projectId) => {
	const res = await axiosInstance.post(`${projectPrefix}/`, { projectId });

	return res.data;
};

// 삭제 api를 호출하는 비동기 통신 메서드
export const deleteBookmarkProjectOne = async (id) => {
	const res = await axiosInstance.delete(`${projectPrefix}/${id}`, {
		withCredentials: true, // ✅ 쿠키 포함 설정
	});

	return res.data;
};

// 체크 api를 호출하는 비동기 통신 메서드
export const checkBookmarkProject = async (projectId) => {
	const res = await axiosInstance.post(`${projectPrefix}/check`, null, {
		params: { projectId },
	});
	return res.data;
};

export const getUserBookmarkPortfolioList = async () => {
	try {
		const res = await axios.get(`${portfolioPrefix}/user/list`, {
			withCredentials: true, // ✅ 쿠키 포함 설정
		});
		// console.log(res);
		return res.data;
	} catch (error) {
		console.error("유저의 전체 북마크 조회 실패", error);
		throw error;
	}
};

export const deleteBookmarkPortfolioOne = async (id) => {
	const res = await axios.delete(`${portfolioPrefix}/${id}`, {
		withCredentials: true, // ✅ 쿠키 포함 설정
	});

	return res.data;
};

export const getUserBookmarkProjectList = async () => {
	try {
		const res = await axios.get(`${projectPrefix}/user/list`, {
			withCredentials: true, // ✅ 쿠키 포함 설정
		});
		// console.log(res);
		return res.data;
	} catch (error) {
		console.error("유저의 전체 북마크 조회 실패", error);
		throw error;
	}
};
