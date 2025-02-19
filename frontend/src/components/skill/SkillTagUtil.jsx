// 스킬 태그 스타일링 기능 구현을 위한 유틸리티 파일

export const validateSkillsString = (skillsString) => {
	const regex = /#([a-zA-Z]+):(초급|중급|고급)(, #[a-zA-Z]+:(초급|중급|고급))*/;
	return regex.test(skillsString);
};

export const parseSkills = (skillsString) => {
	if (!skillsString) {
		return [];
	}
	return skillsString.split(", ").map((skill) => {
		const [language, level] = skill.split(":");
		return {
			language: language ? language.trim().replace("#", "") : "",
			level: level ? level.trim() : "",
		};
	});
};

export const getTailwindClassByLevel = (level) => {
	switch (level) {
		case "초급":
			return "bg-blue-200";
		case "중급":
			return "bg-green-200";
		case "고급":
			return "bg-red-200";
		default:
			return "bg-gray-200";
	}
};

export const getDuplicatedString = (skill) => {
	const uniqueArray = [];
	const duplicateArray = [];

	const skillString = parseSkills(skill);
	skillString.forEach((item) => {
		const lowerItem = item.language.toLowerCase();
		console.log(lowerItem);
		if (
			new Set(
				uniqueArray.map((str) => {
					return str.language.toLowerCase();
				})
			).has(lowerItem)
		) {
			duplicateArray.push(item);
		} else {
			uniqueArray.push(item);
		}
	});

	return duplicateArray.map(item => item.language).join(', ');
};
