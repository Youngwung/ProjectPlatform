package com.ppp.backend.dto.alert;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ppp.backend.domain.Project;
import com.ppp.backend.dto.ProjectDTO;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlertProjectDto {
    private Long id;
    private String senderName;
    private String receiverName;
    private ProjectDTO project;
    private String status;
    private String content;
    private String type;
    private Timestamp createdAt;
    @JsonProperty("isRead")
    private boolean isRead;
    private boolean isMyProject;
    //TODO 필요한 유저 데이터만 담아서 쏴주기....
}
