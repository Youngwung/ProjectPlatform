package com.ppp.backend.controller;

import com.ppp.backend.domain.User;
import com.ppp.backend.dto.UserDto;
import com.ppp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto userDtoCreated = userService.createUser(userDto);
        return ResponseEntity.ok(userDtoCreated);
    }

    @PutMapping("/list/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        // URL의 id와 DTO의 id가 일치하도록 설정합니다.
        userDto.setId(id);
        UserDto updatedUser = userService.updateUser(userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/list/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/list/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exist = userService.isNotNullUserEmail(email);
        return ResponseEntity.ok(exist);
    }

    //TODO 비밀번호 변경
//    public ResponseEntity<UserDto> updataPassword(@RequestBody UserDto userDto) {
//
//    }
//
//    public ResponseEntity<UserDto>
}
