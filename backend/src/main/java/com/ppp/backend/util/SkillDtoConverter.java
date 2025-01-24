package com.ppp.backend.util;

import java.util.HashMap;
import java.util.Map;

public class SkillDtoConverter {
	/**
	 * 정규 표현식으로 작성된 문자열을 Map으로 리턴해주는 변환 메서드
	 * @param skills 정규 표현식으로 #기술과 :숙련도로 이루어진 문자열
	 * @return key: 기술, value: 숙련도를 가지는 Map 자료구조 리턴
	 */
	public static Map<String, String> convertSkillDtoToMap(String skills) {

			String[] skillStrings = skills.split(",");
			Map<String, String> skillMap = new HashMap<>();
			for (String skillString : skillStrings) {
					skillString = skillString.trim(); // 앞뒤 공백 제거
					if (skillString.startsWith("#")) {
							skillString = skillString.substring(1); // # 문자 제거
					}

					String[] skillInfo = skillString.split(":");

					// skillInfo에는 스킬 이름과 기술 숙련도 두 데이터만 들어있어야 함.
					if (skillInfo.length == 2) {
							skillMap.put(skillInfo[0].trim(), skillInfo[1].trim());
					}
			}

			return skillMap;
	}
}
