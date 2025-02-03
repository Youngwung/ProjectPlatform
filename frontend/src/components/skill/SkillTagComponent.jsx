import React from "react";
import { getTailwindClassByLevel, parseSkills } from "./SkillTagUtil";

export default function SkillTagComponent({ skills }) {

	// skills가 유효한지 확인
  if (!skills || skills.trim() === '') {
    return <div>설정한 태그가 없습니다.</div>;
	}
	const skillArray = parseSkills(skills);

	return (
		<div className="font-extrabold">
			{skillArray.map((skill, index) => (
				<span
					key={index}
					className={`skill-tag p-1 px-2 mx-1 rounded 
						${getTailwindClassByLevel(skill.level)}`}
				>
					<code>{skill.language}</code>
				</span>
			))}
		</div>
	);
}
