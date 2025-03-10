import axios from "axios";

// api서버를 호출하는 주소를 변수로 선언해서 사용.
// 서버가 바뀌는 경우 여기만 수정해주면 됨.
// TODO: .env 파일로 서버 주소 비공개 처리
export const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/project`;

const axiosInstance = axios.create({
	baseURL: prefix,
	withCredentials: true, // ✅ 쿠키 자동 포함
});

// tno로 글 하나 가져오는 비동기 통신 메서드
export const getOne = async (projectId) => {
	const res = await axiosInstance.get(`${prefix}/${projectId}`);

	return res.data;
};

// 페이징 처리된 글 목록을 가져오는 비동기 통신 메서드
export const getList = async (pageParam) => {
	const { page, size } = pageParam;

	const res = await axiosInstance.get(`${prefix}/list`, {
		params: { page, size },
	});
	// 비동기 통신에서 쿼리스트링을 사용하는 방법
	// 1. pageParam의 page와 size를 빼둔 후 두번째 아규먼트로  {params:{page, size}} 로 전달.
	// 2. pageParam을 그대로 전달. {params:{...pageParam}} => .3개임

	return res.data;
};

// 등록 api를 호출하는 비동기 통신 메서드

export const postAdd = async (projectObj) => {
	const res = await axiosInstance.post(`${prefix}/`, projectObj);

	return res.data;
};

// 수정 api를 호출하는 비동기 통신 메서드
export const putOne = async (projectObj) => {
	const res = await axiosInstance.put(`${prefix}/${projectObj.id}`, projectObj);

	return res.data;
};

// 삭제 api를 호출하는 비동기 통신 메서드
export const deleteOne = async (projectId) => {
	const res = await axiosInstance.delete(`${prefix}/${projectId}`);

	return res.data;
};

// 검색 api를 호출하는 비동기 통신 메서드
export const projectSearch = async (params) => {
	const { page, size, query, querySkills, type, sortOption } = params;
	let searchParams = new URLSearchParams();
	searchParams.append("page", page);
	searchParams.append("size", size);
	searchParams.append("query", query);
	searchParams.append("type", type || "all");
	searchParams.append("sortOption", sortOption);
	// console.log(querySkills);
	if (querySkills && querySkills.length > 0) {
		querySkills.forEach((skill) => {
			searchParams.append("querySkills", skill);
		});
	} else {
		searchParams.append("querySkills", '');
	}
	const res = await axiosInstance.get(`${prefix}/search?${searchParams}`);
	// 비동기 통신에서 쿼리스트링을 사용하는 방법
	// 1. pageParam의 page와 size를 빼둔 후 두번째 아규먼트로  {params:{page, size}} 로 전달.
	// 2. pageParam을 그대로 전달. {params:{...pageParam}} => .3개임

	return res.data;
};
// 내 프로젝트 조회 api (현재 로그인한 사용자가 생성한 프로젝트 목록)
export const getMyProjects = async () => {
	try {
	  const res = await axiosInstance.get(`${prefix}/my`, { withCredentials: true });
	  return res.data;
	} catch (error) {
	  console.error("🚨 내 프로젝트 조회 실패:", error);
	  throw error;
	}
};

// 메인에 표시할 인기 프로젝트 api
export const getProjectsForMain = async () => {
	try {
		const res =await axios.get(`${prefix}/main`);
		return res.data;
	} catch (error) {
		console.log(error);
		throw error;
	}
}

// 프로젝트의 작성자인 지 확인하는 api 호출
export const checkWriter = async (projectId) => {
	try {
		const res = await axiosInstance.get(`${prefix}/checkWriter/${projectId}`);
		return res.data;		
	} catch (error) {
		console.error(error);
	}
}