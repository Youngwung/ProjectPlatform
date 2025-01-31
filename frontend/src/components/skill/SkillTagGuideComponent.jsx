import React from 'react'
import { getTailwindClassByLevel } from './SkillTagUtil'

export default function SkillTagGuideComponent() {
	return (
		<div>
			<div className="my-2">
				사용 기술 스택:
				<span className={`rounded p-1 m-1 ${getTailwindClassByLevel("초급")}`}>초급</span>
				<span className={`rounded p-1 m-1 ${getTailwindClassByLevel("중급")}`}>중급</span>
				<span className={`rounded p-1 m-1 ${getTailwindClassByLevel("고급")}`}>고급</span>
			</div>
		</div>
	)
}
