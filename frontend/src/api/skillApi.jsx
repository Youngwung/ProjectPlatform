import axios from "axios";

// TODO: .env 파일로 서버 주소 비공개 처리
export const API_SERVER_HOST = 'http://localhost:8080'
const prefix = `${API_SERVER_HOST}/api/skill`

export const getSkillList = async () => {
	const res = await axios.get(`${prefix}/category/list`);
	return res.data;
}

export const getSkillSearchResult = async (param) => {
	const {skillQuery} = param;
	const res = await axios.get(`${prefix}/search`, {params: {skillQuery}});
	return res.data
}