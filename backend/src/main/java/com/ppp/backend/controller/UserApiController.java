package com.ppp.backend.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.ppp.backend.dto.UserDto;
import com.ppp.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserApiController {
    // TODO: 응답 데이터 객체 말고 단순한 데이터로 변경

    private final UserService userService;
    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        try {
            UserDto userDto = userService.getUserById(id);
            return ResponseEntity.ok(userDto);
        } catch (ResponseStatusException e) {
            log.error("유저 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        userDto.setProviderId(4L);
        log.info("userDto===api====={}",userDto);
        UserDto userDtoCreated = userService.createUser(userDto);
        return ResponseEntity.ok(userDtoCreated);
    }

    @DeleteMapping("/list/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
