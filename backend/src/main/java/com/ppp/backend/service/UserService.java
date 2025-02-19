package com.ppp.backend.service;

import com.ppp.backend.domain.*;
import com.ppp.backend.dto.LinkDto;
import com.ppp.backend.dto.UserDto;
import com.ppp.backend.repository.ProviderRepository;
import com.ppp.backend.repository.UserRepository;
import com.ppp.backend.repository.SkillLevelRepository;
import com.ppp.backend.repository.SkillRepository;
import com.ppp.backend.repository.UserSkillRepository;
import com.ppp.backend.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserService extends AbstractSkillService<UserSkill, UserDto, UserSkillRepository, User> {

    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final PasswordEncoder passwordEncoder;
    private final LinkService linkService;
    private final JwtUtil jwtUtil;

    public UserService(
            UserSkillRepository repository,
            SkillRepository skillRepo,
            SkillLevelRepository skillLevelRepo,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ProviderRepository providerRepository,
            LinkService linkService,
            JwtUtil jwtUtil) {
        super(repository, skillRepo, skillLevelRepo);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.providerRepository = providerRepository;
        this.linkService = linkService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    UserSkill createSkillInstance(Long id, User parentEntity, Skill skill, SkillLevel skillLevel) {
        return UserSkill.builder()
                .id(id)
                .user(parentEntity)
                .skill(skill)
                .skillLevel(skillLevel)
                .build();
    }

    /** ✅ 회원 가입 */
    public UserDto createUser(UserDto userDto) {
        log.info("📝 회원 가입 요청: {}", userDto.getEmail());

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // ✅ 비밀번호 검증 (최소 6자)
        if (userDto.getPassword() == null || userDto.getPassword().length() < 6) {
            throw new IllegalArgumentException("비밀번호는 최소 6자 이상이어야 합니다.");
        }

        // ✅ 비밀번호 암호화 후 저장
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        Provider provider = providerRepository.findById(userDto.getProviderId())
                .orElseThrow(() -> new RuntimeException("Provider를 찾을 수 없습니다."));

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(encodedPassword)
                .phoneNumber(userDto.getPhoneNumber())
                .experience(userDto.getExperience())
                .provider(provider)
                .build();

        log.info("✅ 사용자 생성 완료: {}", user);
        User savedUser = userRepository.save(user);

        return convertToDto(savedUser);
    }

    public ResponseEntity<Map<String, Object>> login(String email, String password) {
        log.info("🔑 로그인 시도: {}", email);

        // ✅ 이메일 검증
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "존재하지 않는 이메일입니다."));
        }

        User user = optionalUser.get();

        // ✅ 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "비밀번호가 올바르지 않습니다."));
        }

        // ✅ JWT 발급 (예외 처리 추가)
        String token;
        try {
            token = jwtUtil.generateToken(user.getId(), user.getEmail());
        } catch (Exception e) {
            log.error("🚨 JWT 생성 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "JWT 생성 중 오류가 발생했습니다."));
        }

        // ✅ 응답 데이터 구성
        Map<String, Object> response = Map.of(
                "message", "로그인 성공",
                "accessToken", token,
                "userId", user.getId(),
                "email", user.getEmail()
        );

        return ResponseEntity.ok(response);
    }


    /** ✅ 이메일 존재 여부 확인 */
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * ✅ 전체 사용자 조회
     */
    public List<UserDto> getAllUsers() {
        log.info("📋 전체 사용자 목록 조회");

        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다. id=" + id)); // ✅ 404 처리

        UserDto dto = convertToDto(user);
        dto.setProviderName(user.getProvider().getName());
        dto.setPassword(user.getPassword());
        // ✅ 사용자의 링크 조회 후 DTO에 세팅
        List<LinkDto> userLinks = linkService.getUserLinks(user.getId());
        dto.setLinks(userLinks);

        return dto;
    }



    /** ✅ 사용자 정보 수정 (전화번호, 링크, 기술 스택 등) */
    public UserDto updateUserInfo(Long userId, UserDto updatedUser) {
        log.info("🔄 사용자 정보 수정 요청: userId={}, updatedUser={}", userId, updatedUser);

        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID가 필요합니다.");
        }

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        log.info("✅ 기존 사용자 정보 확인: {}", existingUser);

        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setExperience(updatedUser.getExperience());

        if (updatedUser.getLinks() == null) {
            updatedUser.setLinks(linkService.getUserLinks(userId)); // 기존 링크 유지
        } else {
            linkService.updateUserLinks(userId, updatedUser.getLinks());
        }
        log.info("📌 업데이트 요청된 링크 목록: {}", updatedUser.getLinks());

        User savedUser = userRepository.save(existingUser);
        log.info("✅ 사용자 정보 수정 완료: {}", savedUser);

        return convertToDto(savedUser);
    }
    public boolean verifyPassword(Long userId, String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("전달된 비밀번호 값이 null이거나 비어있습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다. id=" + userId));
        log.info("{},{}",password,user.getPassword());
        return passwordEncoder.matches(password, user.getPassword());
    }



    /** ✅ 사용자 비밀번호 변경 */
    public void updatePassword(Long userId, String nowPassword, String newPassword) {
        log.info("🔑 비밀번호 변경 요청: userId={}", userId);

        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("새 비밀번호는 비워둘 수 없습니다.");
        }

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId=" + userId));

        if (!passwordEncoder.matches(nowPassword, existingUser.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        existingUser.setPassword(encodedPassword);
        userRepository.save(existingUser);

        log.info("✅ 비밀번호 변경 완료: userId={}", userId);
    }

    /** ✅ 사용자 삭제 */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /** ✅ User → UserDto 변환 */
    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .experience(user.getExperience())
                .links(linkService.getUserLinks(user.getId()))
                .build();
    }

    public UserDto updateUserExperience(Long userId, String experience) {
        // 🔹 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다. id=" + userId));

        // 🔹 2. 기존 경험치 업데이트
        log.info("✅ 기존 경험치 업데이트: userId={}, 기존 경험치={}, 새로운 경험치={}",
                userId, user.getExperience(), experience);
        user.setExperience(experience);

        // 🔹 3. 저장 및 응답 반환
        User updatedUser = userRepository.save(user);
        log.info("✅ 경험치 업데이트 완료: userId={}, experience={}", userId, updatedUser.getExperience());

        return convertToDto(updatedUser);
    }


}
