package mi.mi_clush.domain.event.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mi.mi_clush.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isAllDay;

    @Enumerated(EnumType.STRING)
    private RepeatType repeatType;

    private Integer reminderTime;

    public void updateEvent(String title, String description, LocalDateTime startTime, LocalDateTime endTime, RepeatType repeat, Integer reminderTime) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatType = repeat;
        this.reminderTime = reminderTime;
    }
}
