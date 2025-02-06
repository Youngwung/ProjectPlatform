
// api서버를 호출하는 주소를 변수로 선언해서 사용.
// 서버가 바뀌는 경우 여기만 수정해주면 됨.

import axios from "axios"

// TODO: .env 파일로 서버 주소 비공개 처리
export const API_SERVER_HOST = 'http://localhost:8080'
const prefix = `${API_SERVER_HOST}/api/bookmark/project`

// id로 북마크 하나 가져오는 비동기 통신 메서드
export const getBookmarkProjectOne = async (id) => {
	const res = await axios.get(`${prefix}/${id}`)

	return res.data
}

// 페이징 처리된 글 목록을 가져오는 비동기 통신 메서드
export const getBookmarkProjectList = async (pageParam) => {
	
	const {page, size} = pageParam

	const res = await axios.get(`${prefix}/list`, {params:{page, size}})
	// 비동기 통신에서 쿼리스트링을 사용하는 방법
	// 1. pageParam의 page와 size를 빼둔 후 두번째 아규먼트로  {params:{page, size}} 로 전달.
	// 2. pageParam을 그대로 전달. {params:{...pageParam}} => .3개임

	return res.data
}

// 등록 api를 호출하는 비동기 통신 메서드

export const postBookmarkProjectAdd = async (bookmarkProjectObj) => {
	const res = await axios.post(`${prefix}/`, bookmarkProjectObj);

	return res.data
}

// 삭제 api를 호출하는 비동기 통신 메서드
export const deleteBookmarkProjectOne = async (id) => {

	const res = await axios.delete(`${prefix}/${id}`)

	return res.data
}