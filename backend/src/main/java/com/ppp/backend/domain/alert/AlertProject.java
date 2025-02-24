package com.ppp.backend.domain.alert;

import com.ppp.backend.domain.Project;
import com.ppp.backend.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING) // ✅ Enum 값을 DB에 문자열로 저장
    @Column(nullable = false)
    private Status status;

    @Column(length = 1000)
    private String content;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean isRead = false;

    public enum Status {
        신청,        // 프로젝트 신청 or 초대
        검토중,      // 신청자에게 신청이 접수된 상태
        합격,        // 신청 또는 초대 수락
        불합격       // 신청 또는 초대 거절
    }
    public void markAsRead() {
        this.isRead = true;
    }
}
