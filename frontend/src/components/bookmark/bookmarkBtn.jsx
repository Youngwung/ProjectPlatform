import React from 'react'

export default function bookmarkBtn({projectId, userId}) {
	const initstate = {
		id : 0,
		projectId: 0,
		userId: 0
	}

	// 버튼 클릭 시 해당 글 번호와 아이디를 전달받음
	// 버튼 모양 빈 별
	// 받은 정보로 등록 api를 호출함
	// 별을 노란색으로 칠함

	// 이미 등록되어있는 경우 삭제 api를 호출함
	// 버튼 모양을 빈 별로 변경
	
	return (
		<div>bookmarkBtn</div>
	)
}
