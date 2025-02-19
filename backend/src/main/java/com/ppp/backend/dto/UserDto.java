package com.ppp.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ppp.backend.dto.skill.BaseSkillDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends BaseSkillDto {

    @JsonProperty("id") // ✅ JSON 필드명 명확히 설정
    private Long id;

    private String name;
    private String email;
    private String phoneNumber;
    private String experience;
    private String password;
    private Long providerId;
    private List<LinkDto> links;
    private String providerName;
    private String newPassword;

    @JsonCreator
    public UserDto(
            @JsonProperty("id") Object id, // ✅ 다양한 타입 지원
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("experience") String experience,
            @JsonProperty("password") String password,
            @JsonProperty("providerId") Long providerId,
            @JsonProperty("links") List<LinkDto> links,
            @JsonProperty("providerName") String providerName,
            @JsonProperty("newPassword") String newPassword) {

        this.id = parseId(id);
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.experience = experience;
        this.password = password;
        this.providerId = providerId;
        this.links = links;
        this.providerName = providerName;
        this.newPassword = newPassword;
    }

    /**
     * ✅ id 값을 다양한 타입에서 변환
     */
    private Long parseId(Object id) {
        if (id instanceof Number) {
            return ((Number) id).longValue();
        } else if (id instanceof String) {
            try {
                return Long.parseLong((String) id);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ID 값을 Long 타입으로 변환할 수 없습니다: " + id);
            }
        }
        return null;
    }
}
