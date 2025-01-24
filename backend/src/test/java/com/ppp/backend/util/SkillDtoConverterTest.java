package com.ppp.backend.util;

import java.util.Map;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("local")
@Slf4j
public class SkillDtoConverterTest {
	
	// @Test
	public void convertSkillDtoToMapTest() {
		String skills = "#React:초급, #Java:중급";
		Map<String, String> convertSkillDtoToMap = SkillDtoConverter.convertSkillDtoToMap(skills);

		convertSkillDtoToMap.forEach(((key, value) -> {
			System.out.println("key = " + key + ", value = " + value);
		}));
	}
}
