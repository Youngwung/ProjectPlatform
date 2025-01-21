import React from "react";
import { useNavigate, useParams } from "react-router-dom";
import ReadComponent from "../../components/joinProject/ReadComponent";

export default function ReadPage() {
	// 경로 변수 추출 방법
	const { jPno } = useParams();
	// console.log(jPno);
	const navigate = useNavigate();

	const moveToModify = (jPno) => {
		navigate(
			{
				pathname: `/joinProject/modify/${jPno}`,
				// todo: 페이징 변수 쿼리스트링에 저장
			},
			[jPno]
		);
	};

	const moveToList = () => {
		navigate({
			pathname: `/joinProject/list`,
			// todo: 페이징 변수 쿼리스트링에 저장
		})
	}

	return (
		<div>
			Join Project Read Page 글 번호: {jPno}
			<button onClick={() => moveToModify(jPno)}>Test Modify</button>
			<button onClick={moveToList}>Test List</button>
			{/* props 전달 */}
			<ReadComponent jPno = {jPno}></ReadComponent>
		</div>
	);
}
