package mi.mi_clush.domain.event.dto;

import lombok.Builder;
import lombok.Getter;
import mi.mi_clush.domain.event.entity.Event;
import mi.mi_clush.domain.event.entity.RepeatType;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventResDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RepeatType repeat;
    private Integer reminderTime;

    public static EventResDto from(Event savedEvent) {
        return EventResDto.builder()
                .id(savedEvent.getId())
                .title(savedEvent.getTitle())
                .description(savedEvent.getDescription())
                .startTime(savedEvent.getStartTime())
                .endTime(savedEvent.getEndTime())
                .repeat(savedEvent.getRepeat())
                .reminderTime(savedEvent.getReminderTime())
                .build();
    }
}
