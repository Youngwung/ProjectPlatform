package com.ppp.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "joinproject_id")
    private JoinProject joinProject;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(length = 1000)
    private String content;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    public enum Status {
        초대, 접수, 불합격, 합격
    }
}
