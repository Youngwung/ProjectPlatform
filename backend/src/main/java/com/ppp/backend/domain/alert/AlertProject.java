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
@Setter
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

    // 알림 소유자: DB상에서 이 알림의 주체(관리자 또는 소유자)를 명확히 하기 위한 컬럼
    @ManyToOne
    @JoinColumn(name = "alert_owner_id", nullable = false)
    private User alertOwnerId;

    // 발신인: 알림을 발생시킨 사용자 (예: 초대를 보낸 사람, 신청자 등)
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User senderId;

    // 수신인: 알림이 전달될 사용자
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiverId;

    @Enumerated(EnumType.STRING) // ✅ Enum 값을 DB에 문자열로 저장
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING) // ✅ Enum 값을 DB에 문자열로 저장
    @Column(nullable = false)
    private Type type;

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

    private int step;

    public enum Status {
        신청,        // 프로젝트 신청 or 초대
        검토중,      // 신청자에게 신청이 접수된 상태
        합격,        // 신청 또는 초대 수락
        불합격,       // 신청 또는 초대 거절
        초대수락,
        초대거절
    }

    public enum Type {
        참가알림,
        초대알림
    }
    public void markAsRead() {
        this.isRead = true;
    }
}
