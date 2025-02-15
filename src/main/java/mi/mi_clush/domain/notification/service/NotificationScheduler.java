package mi.mi_clush.domain.notification.service;

import lombok.RequiredArgsConstructor;
import mi.mi_clush.domain.event.entity.Event;
import mi.mi_clush.domain.notification.entity.Notification;
import mi.mi_clush.domain.notification.entity.NotificationMethod;
import mi.mi_clush.domain.notification.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationScheduler {
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 * * * * ?")
    public void sendNotifications() {
        LocalDateTime now = LocalDateTime.now();

        for (Notification notification : notificationRepository.findAll()) {
            Event event = notification.getEvent();
            LocalDateTime eventTime = event.getStartTime();

            LocalDateTime notificationTime = eventTime.minusMinutes(notification.getRelativeTime());

            if (now.isAfter(notificationTime) && now.isBefore(notificationTime.plusMinutes(1))){
                notificationService.sendNotifications(notification);
            }
        }
    }
}
