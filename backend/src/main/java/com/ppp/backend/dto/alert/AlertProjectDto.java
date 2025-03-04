package com.ppp.backend.dto.alert;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ppp.backend.domain.Project;
import com.ppp.backend.domain.User;
import com.ppp.backend.dto.ProjectDTO;
import com.ppp.backend.dto.UserDto;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlertProjectDto {
    private Long id;
    private ProjectDTO project;
    private String status;
    private String content;
    private String type;
    private Timestamp createdAt;
    @JsonProperty("isRead")
    private boolean isRead;
    private boolean isMyProject;
    private int step; // 1 초대수락받기전 2 수락받고 원래 알람 상태업데이트 , 3 수락과 거절한뒤 새로운 알람 상태
    private UserDto receiverUserDto;
    private UserDto senderUserDto;
    private UserDto alertOwnerUserDto;
}
