
export default function useCustomString() {

	/**
	 * 
	 * @param {*} str status의 언더바(_)를 공백으로 만들어주는 커스텀 훅
	 * @returns 
	 */
	 const statusToString = (str) =>  {
		return str.replace(/_/g, ' ');
	}
	
	return {statusToString}
}