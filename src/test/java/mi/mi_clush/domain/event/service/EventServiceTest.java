package mi.mi_clush.domain.event.service;

import mi.mi_clush.domain.event.dto.EventReqDto;
import mi.mi_clush.domain.event.dto.EventResDto;
import mi.mi_clush.domain.event.entity.Event;
import mi.mi_clush.domain.event.entity.RepeatType;
import mi.mi_clush.domain.event.repository.EventRepository;
import mi.mi_clush.domain.user.entity.Role;
import mi.mi_clush.domain.user.entity.User;
import mi.mi_clush.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    private User testUser;
    private Event testEvent;
    private EventReqDto testEventReqDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
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

        testEventReqDto = EventReqDto.builder()
                .title("update title")
                .description("update description")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .repeat(RepeatType.DAILY)
                .reminderTime(10)
                .build();
    }

    @Test
    void createEvent_success() {
        // given
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // when
        EventResDto resDto = eventService.createEvent(testUser.getId(), testEventReqDto);

        // then
        assertNotNull(resDto);
        assertEquals(testEvent.getTitle(), resDto.getTitle());
        verify(userRepository, times(1)).findById(testUser.getId());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void getEvents_success() {
        // given
        when(eventRepository.findByUserIdAndStartTime(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(testEvent));

        // when
        List<EventResDto> events = eventService.getEvents(testUser.getId(), LocalDateTime.now());

        // then
        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
        assertEquals(testEvent.getTitle(), events.getFirst().getTitle());
        verify(eventRepository, times(1)).findByUserIdAndStartTime(anyLong(), any(LocalDateTime.class));
    }

    @Test
    void updateEvent_success() {
        // given
        when(eventRepository.findById(testEvent.getId())).thenReturn(Optional.of(testEvent));

        // when
        EventResDto eventReqDto = eventService.updateEvent(testUser.getId(), testEvent.getId(), testEventReqDto);

        // then
        assertNotNull(eventReqDto);
        assertEquals(testEvent.getTitle(), eventReqDto.getTitle());
        verify(eventRepository, times(1)).findById(testEvent.getId());
    }

    @Test
    void updateEvent_fail_event_not_found() {
        // given
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        lenient().when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> eventService.updateEvent(testUser.getId(), 999L, testEventReqDto));
    }

    @Test
    void updateEvent_fail_user_not_owner() {
        // given
        User anotherUser = User.builder()
                .id(2L)
                .email("test2@test.com")
                .role(Role.USER)
                .password("password2")
                .username("test2")
                .build();
        Event anotherUserEvent = Event.builder()
                .id(200L)
                .user(anotherUser)
                .title("test title2")
                .description("test description2")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .repeat(RepeatType.NONE)
                .reminderTime(1)
                .build();

        when(eventRepository.findById(anotherUserEvent.getId())).thenReturn(Optional.of(anotherUserEvent));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> eventService.updateEvent(testUser.getId(), anotherUserEvent.getId(), testEventReqDto));

        assertEquals("이벤트를 수정할 수 있는 사용자가 아닙니다.", exception.getMessage());
        verify(eventRepository, times(1)).findById(anotherUserEvent.getId());
    }

    @Test
    void deleteEvent_success() {
        // given
        when(eventRepository.findById(testEvent.getId())).thenReturn(Optional.of(testEvent));

        // when
        eventService.deleteEvent(testUser.getId(), testEvent.getId());

        // then
        verify(eventRepository, times(1)).delete(testEvent);
    }

    @Test
    void deleteEvent_fail_event_not_found() {
        // given
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> eventService.deleteEvent(testUser.getId(), 999L));

        assertEquals("존재하지 않는 이벤트입니다.", exception.getMessage());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventRepository, never()).delete(any(Event.class));
    }

    @Test
    void deleteEvent_fail_user_not_owner() {
        // given
        User anotherUser = User.builder()
                .id(2L)
                .email("test2@test.com")
                .role(Role.USER)
                .password("password2")
                .username("test2")
                .build();
        Event anotherUserEvent = Event.builder()
                .id(200L)
                .user(anotherUser)
                .title("test title2")
                .description("test description2")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .repeat(RepeatType.NONE)
                .reminderTime(1)
                .build();

        when(eventRepository.findById(anotherUserEvent.getId())).thenReturn(Optional.of(anotherUserEvent));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> eventService.deleteEvent(testUser.getId(), anotherUserEvent.getId()));

        assertEquals("이벤트를 수정할 수 있는 사용자가 아닙니다.", exception.getMessage());
        verify(eventRepository, times(1)).findById(anotherUserEvent.getId());
    }
}