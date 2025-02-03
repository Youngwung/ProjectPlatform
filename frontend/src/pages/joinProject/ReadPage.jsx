import React from "react";
import { useParams } from "react-router-dom";
import ReadComponent from "../../components/joinProject/ReadComponent";
import useCustomMove from "../../hooks/useCustomMove";

export default function ReadPage() {
	// 경로 변수 추출 방법
	const { jpNo } = useParams();
	const{moveToList, moveToModify} = useCustomMove();


	return (
		<div>
			{/* props 전달 */}
			<ReadComponent jpNo = {jpNo}></ReadComponent>
		</div>
	);
}
