package mi.mi_clush.domain.notification.service;

import lombok.RequiredArgsConstructor;
import mi.mi_clush.domain.event.entity.Event;
import mi.mi_clush.domain.event.repository.EventRepository;
import mi.mi_clush.domain.notification.dto.NotificationReqDto;
import mi.mi_clush.domain.notification.entity.Notification;
import mi.mi_clush.domain.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final EventRepository eventRepository;
    private final EmailService emailService;

    public String setNotification(Long eventId, NotificationReqDto request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트입니다."));

        Notification notification = Notification.builder()
                .event(event)
                .relativeTime(request.getRelativeTime())
                .method(request.getMethod())
                .build();

        notificationRepository.save(notification);

        return "알림을 설정하였습니다.";
    }

    public void sendNotifications(Notification notification) {
        Event event = notification.getEvent();
        String recipientEmail = event.getUser().getEmail(); // 이벤트 작성자의 이메일

        String subject = "이벤트 알림: " + event.getTitle();
        String body = "<h1>" + event.getTitle() + "</h1>"
                + "<p>" + event.getDescription() + "</p>"
                + "<p>이벤트가 곧 시작됩니다. 참여해주세요!</p>";

        emailService.sendEmail(recipientEmail, subject, body);
    }
}
