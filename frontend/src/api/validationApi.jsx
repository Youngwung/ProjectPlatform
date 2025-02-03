import axios from "axios";

// TODO: .env 파일로 서버 주소 비공개 처리
export const API_SERVER_HOST = 'http://localhost:8080'
const prefix = `${API_SERVER_HOST}/api/validation`

// 프로젝트 작성 객체를 넘겨 유효성 검사 결과를 가져오는 통신 메서드
/**
 * 
 * @param {*} projectObj 
 * @returns isValid: 검사 결과를 리턴, wrongString: DB에 없는 단어를 사용한 경우 해당 리스트를 toString 메서드를 이용해 변환 후 리턴, DB에 있는 단어만 사용한 경우 빈 문자열 리턴
 */
export const getProjectValid = async (skills) => {
	const res = await axios.post(`${prefix}/project`, skills, {
		// 자동 인코딩으로 인해 json 형태로 변환
		headers: {
			'Content-Type': 'application/json'
		}
	}).then((result) => {
		return result.data
	});

	return res;
}