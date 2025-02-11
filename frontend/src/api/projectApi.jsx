import axios from "axios";

// api서버를 호출하는 주소를 변수로 선언해서 사용.
// 서버가 바뀌는 경우 여기만 수정해주면 됨.
// TODO: .env 파일로 서버 주소 비공개 처리
export const API_SERVER_HOST = 'http://localhost:8080'
const prefix = `${API_SERVER_HOST}/api/project`

const axiosInstance = axios.create({
	baseURL: prefix,
	withCredentials: true, // ✅ 쿠키 자동 포함
  });
  
// tno로 글 하나 가져오는 비동기 통신 메서드
export const getOne = async (projectId) => {
	const res = await axiosInstance.get(`${prefix}/${projectId}`)

	return res.data
}

// 페이징 처리된 글 목록을 가져오는 비동기 통신 메서드
export const getList = async (pageParam) => {
	
	const {page, size} = pageParam

	const res = await axiosInstance.get(`${prefix}/list`, {params:{page, size}})
	// 비동기 통신에서 쿼리스트링을 사용하는 방법
	// 1. pageParam의 page와 size를 빼둔 후 두번째 아규먼트로  {params:{page, size}} 로 전달.
	// 2. pageParam을 그대로 전달. {params:{...pageParam}} => .3개임

	return res.data
}

// 등록 api를 호출하는 비동기 통신 메서드

export const postAdd = async (projectObj) => {
	const res = await axiosInstance.post(`${prefix}/`, projectObj);

	return res.data
}

// 수정 api를 호출하는 비동기 통신 메서드
export const putOne = async (projectObj) => {
	const res = await axiosInstance.put(`${prefix}/${projectObj.id}`, projectObj);

	return res.data
}

// 삭제 api를 호출하는 비동기 통신 메서드
export const deleteOne = async (projectId) => {

	const res = await axiosInstance.delete(`${prefix}/${projectId}`)

	return res.data
}