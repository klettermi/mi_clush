package mi.mi_clush.domain.event.dto;

import lombok.Builder;
import lombok.Getter;
import mi.mi_clush.domain.event.entity.RepeatType;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventReqDto {
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RepeatType repeat;
    private Integer reminderTime;
}
