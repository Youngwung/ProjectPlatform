
// api서버를 호출하는 주소를 변수로 선언해서 사용.
// 서버가 바뀌는 경우 여기만 수정해주면 됨.

import axios from "axios";

// TODO: .env 파일로 서버 주소 비공개 처리
export const API_SERVER_HOST = 'http://localhost:8080';
const portfolioPrefix = `${API_SERVER_HOST}/api/bookmark/portfolio`;
const axiosInstance = axios.create({
  baseURL: portfolioPrefix,
  withCredentials: true, // ✅ 쿠키 자동 포함
});

// 등록 api를 호출하는 비동기 통신 메서드

export const postBookmarkPortfolioAdd = async (portfolioId) => {
	
	const res = await axiosInstance.post(`${portfolioPrefix}/`, {portfolioId});

	return res.data
}

// 삭제 api를 호출하는 비동기 통신 메서드
export const deleteBookmarkPortfolioOne = async (id) => {

	const res = await axiosInstance.delete(`${portfolioPrefix}/${id}`,{
		withCredentials: true // ✅ 쿠키 포함 설정
	})

	return res.data
}

// 체크 api를 호출하는 비동기 통신 메서드
export const checkBookmarkPortfolio = async (portfolioId) => {
	const res = await axiosInstance.post(`${portfolioPrefix}/check`, null, {params: {portfolioId}})
	return res.data
}
	