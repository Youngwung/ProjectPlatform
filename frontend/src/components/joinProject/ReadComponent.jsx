import React, { useEffect, useState } from "react";
import { getOne } from "../../api/joinProjectApi";

// 조회 기능을 구현하기 위한 컴포넌트

// 기본값 설정
const initState = {
	jpNo: 0,
	userId: 0,
	title: "",
	description: "",
	maxPeople: 0,
	jpStatus: "",
	isPublic: false,
	createdAt: "",
	updatedAt: ""
};

export default function ReadComponent({ jPno }) {
	const [joinProject, setJoinProject] = useState(initState);

	useEffect(() => {
		getOne(jPno).then(data => {
			console.log(data)

			setJoinProject(data)
		})
	
		return () => {
		}
	}, [jPno])
	
	
	return <div>ReadComponent</div>;
}
