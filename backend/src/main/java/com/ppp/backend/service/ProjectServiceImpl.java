package com.ppp.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ppp.backend.domain.Project;
import com.ppp.backend.domain.ProjectSkill;
import com.ppp.backend.domain.Skill;
import com.ppp.backend.domain.SkillLevel;
import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.dto.ProjectDTO;
import com.ppp.backend.dto.skill.BaseSkillDto;
import com.ppp.backend.repository.ProjectRepository;
import com.ppp.backend.repository.ProjectSkillRepository;
import com.ppp.backend.repository.SkillLevelRepository;
import com.ppp.backend.repository.SkillRepository;
import com.ppp.backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class ProjectServiceImpl extends
		AbstractSkillService<ProjectSkill, BaseSkillDto, ProjectSkillRepository, Project> implements ProjectService {
	private final ProjectRepository projectRepo;
	private final UserRepository userRepo;

	// Lombok의 애너테이션으로는 부모클래스의 final field을 초기화할 수 없음
	public ProjectServiceImpl(
			ProjectRepository projectRepo, 
			UserRepository userRepo, 
			SkillRepository skillRepo,
			SkillLevelRepository skillLevelRepo, 
			ProjectSkillRepository projectSkillRepo) {
		super(projectSkillRepo, skillRepo, skillLevelRepo);
		this.projectRepo = projectRepo;
		this.userRepo = userRepo;
	}

	@Override
	public ProjectDTO get(Long projectId) {
		Project project = projectRepo.findById(projectId).orElseThrow();
		ProjectDTO dto = fromEntity(project);
		// AbstractSkillService의 메서드 호출
		String skills = getSkill(projectId);
		dto.setSkills(skills);
		return dto;
	}

	@Override
	public Long register(ProjectDTO dto) {
		Project project = toEntity(dto, userRepo);

		// DB에 없는 스킬을 입력받은 경우 -1리턴
		if (!existingSkill(dto.getSkills()))
			return -1L;

		Project result = projectRepo.save(project);
		saveParentEntity(dto, project);

		// 등록된 프로젝트 번호 리턴
		return result.getId();

		// // !등록된 글 번호로 ProjectSkill 객체를 DB에 등록
		// // 정규 표현식으로 작성된 문자열
		// String skills = dto.getSkills();

		// // 문자열을 Map 형식으로 변환
		// Map<String, String> skillMap =
		// skillDtoConverter.convertSkillDtoToMap(skills);

		// // 프론트엔드에서 api호출로 유효성 검사를 하지만 여기에서도 한번 더 검사

		// // 유효성 검사 리스트
		// List<String> invalidList = new ArrayList<>();
		// List<ProjectSkill> projectSkills = new ArrayList<>();

		// skillMap.forEach((key, value) -> {
		// Skill skill = skillRepo.findByNameIgnoreCase(key);
		// SkillLevel skillLevel = skillLevelRepo.findByNameIgnoreCase(value);
		// if (skill == null) {
		// invalidList.add("Invalid skill: " + key);
		// }
		// if (skillLevel == null) {
		// invalidList.add("Invalid skill level: " + value);
		// }
		// // 유효한 데이터인 경우 ProjectSkill 객체 생성 후 리스트에 추가
		// ProjectSkill projectSkill = ProjectSkill.builder()
		// .project(project)
		// .skill(skill)
		// .skillLevel(skillLevel)
		// .build();

		// projectSkills.add(projectSkill);
		// });

		// // 유효하지 않은 데이터가 있는 경우
		// if (!invalidList.isEmpty()) {
		// // 예외 처리: -1을 리턴하여 등록 실패를 클라이언트에 알림
		// return -1L;
		// }

		// // 유효성 검사를 통과한 경우 프로젝트 데이터 저장
		// Project result = projectRepo.save(project);

		// // Skill과 SkillLevel을 ProjectSkill 테이블에 저장
		// projectSkills.forEach((projectSkill) -> {
		// projectSkillRepo.save(projectSkill);
		// });

	}

	@Override
	public void modify(ProjectDTO dto) {
		// 반드시 업데이트 메서드 작성 시 기존 엔터티를 가져올 필요는 없지만
		// 업데이트 시 공개 여부 변수(isPublic)이 false로 초기화되는 문제를 해결하기 위해
		// 기존 엔터티의 데이터를 가져와서 삽입해 주었음.
		Project entity = projectRepo.findById(dto.getId()).orElseThrow();
		entity.update(dto.getTitle(), dto.getDescription(), dto.getMaxPeople(), dto.getStatus(), dto.isPublic());

		projectRepo.save(entity);

		modifySkill(dto.getId(), dto, entity);

		// // 유효성 검사 리스트
		// List<String> invalidList = new ArrayList<>();

		// // dto의 스킬이 수정되었는 지 검사
		// String dtoSkills = dto.getSkills();
		// Map<String, String> dtoSkillMap =
		// skillDtoConverter.convertSkillDtoToMap(dtoSkills);

		// // 현재 스킬맵의 각 항목에 대해 검사
		// dtoSkillMap.forEach((key, value) -> {
		// Skill skill = skillRepo.findByNameIgnoreCase(key);
		// SkillLevel skillLevel = skillLevelRepo.findByNameIgnoreCase(value);

		// // 받아온 스킬과 숙련도가 DB에 존재하는 지 검사
		// if (skill == null) {
		// invalidList.add("Invalid skill: " + key);
		// }
		// if (skillLevel == null) {
		// invalidList.add("Invalid skill level: " + value);
		// }
		// });

		// // 유효하지 않은 데이터가 있는 경우
		// if (!invalidList.isEmpty()) {
		// // TODO: 예외 처리 필요
		// }

		// // 기존 스킬 리스트를 가져옴
		// List<ProjectSkill> existingSkills =
		// projectSkillRepo.findByProjectId(dto.getId());
		// log.info("existingSkills = {}", existingSkills);

		// List<ProjectSkill> updateList = new ArrayList<>();
		// // 기존 스킬로 초기화
		// List<ProjectSkill> removeList = new ArrayList<>();

		// // 기존 스킬 리스트를 순회하면서 비교
		// existingSkills.forEach(existingSkill -> {
		// String skillName = existingSkill.getSkill().getName();
		// String skillLevel = existingSkill.getSkillLevel().getName();

		// // 새로운 스킬 맵에 해당 스킬이 있는지 확인
		// if (dtoSkillMap.containsKey(skillName)) {
		// String newLevel = dtoSkillMap.get(skillName);
		// if (!skillLevel.equals(newLevel)) {
		// // 레벨이 다른 경우 객체를 생성해 updateList에 추가
		// SkillLevel newLevelObj = skillLevelRepo.findByNameIgnoreCase(newLevel);
		// ProjectSkill updateSkill = ProjectSkill.builder()
		// // 이 경우 update이므로 고유 키를 명시해서 객체를 생성
		// .id(existingSkill.getId())
		// .project(project)
		// .skill(existingSkill.getSkill())
		// .skillLevel(newLevelObj)
		// .build();
		// updateList.add(updateSkill);
		// }
		// // 비교가 끝난 스킬은 새로운 스킬 맵에서 제거
		// dtoSkillMap.remove(skillName);
		// } else {
		// // 새로운 스킬 맵에 없는 경우 removeList에 추가
		// removeList.add(existingSkill);
		// }
		// });

		// // 새로운 스킬 맵에 남아있는 스킬들은 새로 추가될 스킬들
		// if (!dtoSkillMap.isEmpty()) {
		// dtoSkillMap.forEach((skillName, skillLevel) -> {
		// Skill newSkill = skillRepo.findByNameIgnoreCase(skillName);
		// SkillLevel newLevel = skillLevelRepo.findByNameIgnoreCase(skillLevel);

		// // update가 아니라 insert이므로 번호를 안 넣어줘도 됨.
		// ProjectSkill skillObj = ProjectSkill.builder()
		// .project(project)
		// .skill(newSkill)
		// .skillLevel(newLevel)
		// .build();
		// updateList.add(skillObj);
		// });
		// }

		// // log.info("removeList = {}", removeList);
		// // log.info("updateList = {}", updateList);
		// // 제거 리스트에 있는 객체를 제거
		// removeList.forEach(remove -> {
		// projectSkillRepo.delete(remove);
		// });
		// // 업데이트 리스트에 있는 객체를 추가
		// updateList.forEach(update -> {
		// projectSkillRepo.save(update);
		// });

	}

	@Override
	public void remove(Long jpNo) {
		int result = repository.deleteByProjectId(jpNo);
		log.info("result = {}", result);
		projectRepo.deleteById(jpNo);
	}

	@Override
	public PageResponseDTO<ProjectDTO> getList(PageRequestDTO PageRequestDTO) {
		// JPA
		Page<Project> result = projectRepo.searchString(PageRequestDTO);

		// ! Project List => ProjectDTO List
		List<ProjectDTO> dtoList = result.get().map(project -> {
			ProjectDTO dto = fromEntity(project);

			String skillString = getSkill(project.getId());

			// Map<String, String> skillMap =
			// projectSkillRepo.findByProjectId(project.getId())
			// .stream()
			// .collect(Collectors.toMap(
			// projectSkill -> projectSkill.getSkill().getName(),
			// projectSkill -> projectSkill.getSkillLevel().getName()));

			// // Map을 String으로 변환하는 메서드를 호출한 후 dto의 skills 필드를 초기화
			// String skillString = skillDtoConverter.convertMapToSkillDto(skillMap);
			dto.setSkills(skillString);
			return dto;
		}).collect(Collectors.toList());

		// 페이징 처리
		PageResponseDTO<ProjectDTO> pageResponseDTO = new PageResponseDTO<>(dtoList, PageRequestDTO,
				result.getTotalElements());

		return pageResponseDTO;
	}

	@Override
	ProjectSkill createSkillInstance(Long id, Project parentEntity, Skill skill, SkillLevel skillLevel) {
		return ProjectSkill.builder()
				.id(id)
				.project(parentEntity)
				.skill(skill)
				.skillLevel(skillLevel)
				.build();
	}
}
