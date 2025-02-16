package mi.mi_clush.domain.event.service;

import lombok.RequiredArgsConstructor;
import mi.mi_clush.domain.event.dto.EventReqDto;
import mi.mi_clush.domain.event.dto.EventResDto;
import mi.mi_clush.domain.event.entity.Event;
import mi.mi_clush.domain.event.repository.EventRepository;
import mi.mi_clush.domain.notification.entity.Notification;
import mi.mi_clush.domain.notification.repository.NotificationRepository;
import mi.mi_clush.domain.user.entity.User;
import mi.mi_clush.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public EventResDto createEvent(Long id, EventReqDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Event event = Event.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .repeatType(request.getRepeat())
                .reminderTime(request.getReminderTime())
                .build();

        Event savedEvent = eventRepository.save(event);
        return EventResDto.from(savedEvent);
    }

    @Transactional(readOnly = true)
    public List<EventResDto> getEvents(Long id, LocalDateTime date) {
        return eventRepository.findByUserIdAndStartTime(id, date).stream()
                .map(EventResDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventResDto updateEvent(Long id, Long eventId, EventReqDto request) {
        Event event = getEventIfOwner(id, eventId);

        event.updateEvent(
                request.getTitle(),
                request.getDescription(),
                request.getStartTime(),
                request.getEndTime(),
                request.getRepeat(),
                request.getReminderTime()
        );

        return EventResDto.from(event);
    }

    @Transactional
    public void deleteEvent(Long id, Long eventId) {
        Event event = getEventIfOwner(id, eventId);
        List<Notification> notifications = notificationRepository.findByEvent(event);

        notificationRepository.deleteAll(notifications);
        eventRepository.delete(event);
    }

    private Event getEventIfOwner(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트입니다."));

        if (!event.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("이벤트를 수정할 수 있는 사용자가 아닙니다.");
        }

        return event;
    }

}
