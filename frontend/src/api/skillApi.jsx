import axios from "axios";

// TODO: .env 파일로 서버 주소 비공개 처리
export const API_SERVER_HOST = 'http://localhost:8080'
const prefix = `${API_SERVER_HOST}/api/skill`
const axiosInstance = axios.create({
  baseURL: prefix,
  withCredentials: true, // ✅ 쿠키 자동 포함
});
export const getSkillList = async () => {
	const res = await axios.get(`${prefix}/category/list`);
	return res.data;
}

export const getSkillSearchResult = async (param) => {
	const {skillQuery} = param;
	const res = await axios.get(`${prefix}/search`, {params: {skillQuery}});
	return res.data;
}

export const getUserSkill = async () => {
	const res = await axiosInstance.get(`${prefix}/user`);
	return res.data;
}

export const putUserSkill = async (skills) => {
	const res = await axiosInstance.put(`${prefix}/user`, null, {params: {skills}});
	return res.data;
}