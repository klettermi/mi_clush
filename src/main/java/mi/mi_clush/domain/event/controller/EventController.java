package mi.mi_clush.domain.event.controller;

import lombok.RequiredArgsConstructor;
import mi.mi_clush.domain.event.dto.EventReqDto;
import mi.mi_clush.domain.event.service.EventService;
import mi.mi_clush.domain.user.entity.User;
import mi.mi_clush.global.dto.ApiResponse;
import mi.mi_clush.global.utils.ResponseUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ApiResponse<?> createEvent(@AuthenticationPrincipal User user, @RequestBody EventReqDto request) {
        return ResponseUtils.success(eventService.createEvent(user.getId(), request));
    }

    @GetMapping("/{date}")
    public ApiResponse<?> getEvents(@AuthenticationPrincipal User user, @PathVariable LocalDateTime date) {
        return ResponseUtils.success(eventService.getEvents(user.getId(), date));
    }

    @PutMapping("/{eventId}")
    public ApiResponse<?> updateEvent(@AuthenticationPrincipal User user, @PathVariable Long eventId, @RequestBody EventReqDto request) {
        return ResponseUtils.success(eventService.updateEvent(user.getId(), eventId, request));
    }

    @DeleteMapping("/{eventId}")
    public ApiResponse<?> deleteEvent(@AuthenticationPrincipal User user, @PathVariable Long eventId) {
        eventService.deleteEvent(user.getId(), eventId);
        return ResponseUtils.success();
    }

}
