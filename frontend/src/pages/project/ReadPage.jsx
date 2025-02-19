import React from "react";
import { useParams } from "react-router-dom";
import ReadComponent from "../../components/project/ReadComponent";

export default function ReadPage() {
	// 경로 변수 추출 방법
	const { projectId } = useParams();

	return (
		<div>
			{/* props 전달 */}
			<ReadComponent projectId = {projectId}></ReadComponent>
		</div>
	);
}
