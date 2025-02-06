package com.ppp.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ppp.backend.domain.Provider;
import com.ppp.backend.domain.Skill;
import com.ppp.backend.domain.SkillLevel;
import com.ppp.backend.domain.User;
import com.ppp.backend.domain.UserSkill;
import com.ppp.backend.dto.UserDto;
import com.ppp.backend.repository.ProviderRepository;
import com.ppp.backend.repository.SkillLevelRepository;
import com.ppp.backend.repository.SkillRepository;
import com.ppp.backend.repository.UserRepository;
import com.ppp.backend.repository.UserSkillRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService extends AbstractSkillService<UserSkill, UserDto, UserSkillRepository, User> {

    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserSkillRepository repository,
            SkillRepository skillRepo,
            SkillLevelRepository skillLevelRepo,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ProviderRepository providerRepository) {
        super(repository, skillRepo, skillLevelRepo);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.providerRepository = providerRepository;
    }

    // TOOOOOOODo login boolean형으로 바꾸기 그리고 패스워드 엔코딩 해슁 매칭 확인해서 로그인까지

    public boolean login(String email, String password) {
        // 1️⃣ 이메일로 사용자 조회
        Optional<User> optionalUser = userRepository.findByEmail(email);

        // 2️⃣ 사용자 존재 여부 확인
        if (optionalUser.isEmpty()) {
            return false; // 로그인 실패 (이메일 없음)
        }
        User user = optionalUser.get();
        // 3️⃣ 비밀번호 검증 (암호화된 비밀번호 비교)
        return passwordEncoder.matches(password, user.getPassword()); // 성공
    }

    public List<UserDto> getAllUsers() {
        List<UserDto> dtos = userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // dto에 스킬을 초기화하는 로직 추가
        dtos.forEach(dto -> {
            dto.setSkills(getSkill(dto.getId()));
        });
        return dtos;
    }

    /**
     * 사용자 정보를 생성하는 메서드입니다.
     * 
     * @param userDto 클라이언트로부터 전달받은 사용자 정보(DTO)
     * @return 저장된 사용자 정보를 DTO로 반환
     */
    public UserDto createUser(UserDto userDto) {
        Long providerId = 4L;// 기본값 로컬 TODO 소셜로그인을 해결한뒤에 수정
        Provider provider = providerRepository.findById(providerId).orElseThrow();
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(encodedPassword)
                .phoneNumber(userDto.getPhoneNumber())
                .experience(userDto.getExperience())
                .password(encodedPassword)
                .provider(provider)
                .build();

        // ------------------ 스킬 관련 로직 구현 부분
        // 스킬 유효성 검사
        boolean existingSkill = existingSkill(userDto.getSkills());
        if (!existingSkill) {
            // DB에 존재하지 않는 기술일 때 null 리턴
            return null;
        }

        // 변환한 User 엔티티를 데이터베이스에 저장합니다.
        User savedUser = userRepository.save(user);

        // 유저 스킬 저장 메서드 호출
        saveParentEntity(userDto, savedUser);
        // 저장된 User 엔티티를 다시 DTO로 변환하여 반환합니다.
        return convertToDto(savedUser);
    }

    /**
     * 사용자 ID로 사용자 정보를 조회하는 메서드입니다.
     * 
     * @param id 사용자 ID
     * @return 조회된 사용자 정보를 DTO로 반환
     */
    public UserDto getUserById(Long id) {
        // ID로 User 엔티티를 조회합니다. 존재하지 않으면 예외를 발생시킵니다.
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // ----------- 스킬 관련 로직 추가
        String skill = getSkill(id);
        UserDto dto = convertToDto(user);
        dto.setSkills(skill);
        return dto;
    }

    public Boolean isNotNullUserEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 사용자 정보를 업데이트하는 메서드입니다.
     * 
     * @param userDto 업데이트할 정보가 담긴 DTO (ID가 반드시 포함되어야 합니다.)
     * @return 업데이트된 사용자 정보를 DTO로 반환
     */
    public UserDto updateUser(UserDto userDto) {
        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userDto.getId()));
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        existingUser.setExperience(userDto.getExperience());

        User updatedUser = userRepository.save(existingUser);

        // 유저 스킬 업데이트 메서드 호출
        modifySkill(userDto.getId(), userDto, existingUser);
        return convertToDto(updatedUser);
    }

    /**
     * 사용자 정보를 삭제하는 메서드입니다.
     * 
     * @param id 삭제할 사용자의 ID
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * User 엔티티를 UserDto로 변환하는 헬퍼 메서드입니다.
     * 
     * @param user 변환할 User 엔티티
     * @return 변환된 UserDto
     */
    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .password(user.getPassword())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .experience(user.getExperience())
                // TODO skills 연동 필요
                .skills(null)
                .build();
    }

    @Override
    UserSkill createSkillInstance(Long id, User parentEntity, Skill skill, SkillLevel skillLevel) {
        return UserSkill.builder()
                .id(id)
                .skill(skill)
                .skillLevel(skillLevel)
                .user(parentEntity)
                .build();
    }
}
