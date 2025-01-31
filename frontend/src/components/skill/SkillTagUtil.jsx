// 스킬 태그 스타일링 기능 구현을 위한 유틸리티 파일

export const validateSkillsString = (skillsString) => {
  const regex = /#([a-zA-Z]+):(초급|중급|고급)(, #[a-zA-Z]+:(초급|중급|고급))*/;
  return regex.test(skillsString);
};

export const parseSkills = (skillsString) => {
	if (!skillsString) {
		return [];
	}
  return skillsString.split(", ").map(skill => {
    const [language, level] = skill.split(":");
    return { language: language ? language.trim().replace('#', '') : '', level: level ? level.trim() : '' };
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