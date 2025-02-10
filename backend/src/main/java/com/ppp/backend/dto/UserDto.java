package com.ppp.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class UserDto {
    Long id;
    String name;
    String password;
    String email;
    String phoneNumber;
    String experience;
    String skills;
    Long providerId;
}
