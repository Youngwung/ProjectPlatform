package com.ppp.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class UserDto {
    Long id;
    String name;
    String email;
    String phoneNumber;
    String experience;
    String skills;
}
