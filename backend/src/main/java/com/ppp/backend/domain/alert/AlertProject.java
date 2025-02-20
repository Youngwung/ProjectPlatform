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

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(length = 1000)
    private String content;

    @CreationTimestamp // 🔥 생성 시 자동 저장 (CURRENT_TIMESTAMP)
    @Column(updatable = false) // 생성 이후 변경되지 않도록 설정
    private Timestamp createdAt;

    @UpdateTimestamp // 🔥 업데이트 시 자동 변경 (ON UPDATE CURRENT_TIMESTAMP)
    private Timestamp updatedAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean isRead = false;

    public enum Status {
        초대, 접수, 불합격, 합격
    }

    /**
     * 알림을 읽음 처리하는 메서드
     */
    public void markAsRead() {
        this.isRead = true;
    }
}
