package com.ppp.backend.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.ppp.backend.repository.SkillLevelRepository;
import com.ppp.backend.repository.SkillRepository;
import com.ppp.backend.service.ValidationService;

import lombok.extern.slf4j.Slf4j;

// @SpringBootTest
@ActiveProfiles("local")
@Slf4j
public class ValidationServiceTest {

	@Autowired
	private ValidationService validService;

	@Autowired
	private SkillRepository skillRepo;

	@Autowired
	private SkillLevelRepository skillLevelRepo;

	// @Test
	public void validateSkillsTest() {
		String skills = "#으아아:초급, #Java:초월급, #에베베:신급";
		List<String> validateSkills = validService.validateSkills(skills);
		System.out.println(validateSkills);
	}
}
