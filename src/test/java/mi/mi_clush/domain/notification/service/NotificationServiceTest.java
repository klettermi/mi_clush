package mi.mi_clush.domain.notification.service;

import mi.mi_clush.domain.event.entity.Event;
import mi.mi_clush.domain.event.entity.RepeatType;
import mi.mi_clush.domain.event.repository.EventRepository;
import mi.mi_clush.domain.notification.dto.NotificationReqDto;
import mi.mi_clush.domain.notification.entity.Notification;
import mi.mi_clush.domain.notification.entity.NotificationMethod;
import mi.mi_clush.domain.notification.repository.NotificationRepository;
import mi.mi_clush.domain.user.entity.Role;
import mi.mi_clush.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EmailService emailService;

    private NotificationService notificationService;

    private Event testEvent;
    private NotificationReqDto testNotificationReqDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationService = new NotificationService(notificationRepository, eventRepository, emailService);

        User testUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .role(Role.USER)
                .password("password")
                .username("test")
                .build();

        testEvent = Event.builder()
                .id(100L)
                .user(testUser)
                .title("test title")
                .description("test description")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .repeat(RepeatType.NONE)
                .reminderTime(1)
                .build();

        testNotificationReqDto = NotificationReqDto.builder()
                .relativeTime(10)
                .method(NotificationMethod.EMAIL)
                .build();
    }

    @Test
    void setNotification_success() {
        // given
        when(eventRepository.findById(testEvent.getId())).thenReturn(Optional.of(testEvent));

        // when
        String result = notificationService.setNotification(testEvent.getId(), testNotificationReqDto);

        // then
        assertEquals("알림을 설정하였습니다.", result);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void setNotification_fail_event_not_found() {
        // given
        when(eventRepository.findById(testEvent.getId())).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> notificationService.setNotification(testEvent.getId(), testNotificationReqDto));

        assertEquals("존재하지 않는 이벤트입니다.", exception.getMessage());
    }

    @Test
    void sendNotifications_success() {
        // given
        Notification notification = Notification.builder()
                .event(testEvent)
                .relativeTime(10)
                .method(NotificationMethod.EMAIL)
                .build();

        // when
        notificationService.sendNotifications(notification);

        // then
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());  // 이메일 서비스 호출 확인
    }
}