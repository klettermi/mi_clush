package mi.mi_clush.domain.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mi.mi_clush.domain.event.entity.Event;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private Integer relativeTime; // 이벤트 기준 몇 분전에 알림 보낼지

    @Enumerated(EnumType.STRING)
    private NotificationMethod method;
}
