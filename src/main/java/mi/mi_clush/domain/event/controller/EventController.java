package mi.mi_clush.domain.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mi.mi_clush.domain.event.dto.EventReqDto;
import mi.mi_clush.domain.event.service.EventService;
import mi.mi_clush.domain.user.entity.User;
import mi.mi_clush.global.dto.ApiResponse;
import mi.mi_clush.global.security.userdetails.UserDetailsImpl;
import mi.mi_clush.global.utils.ResponseUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "일정 관련 API", description = "일정 관련 API")
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @Operation(summary = "일정 생성", description = "일정 생성")
    @PostMapping
    public ApiResponse<?> createEvent(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody EventReqDto request) {
        User user = userDetails.getUser();
        return ResponseUtils.success(eventService.createEvent(user.getId(), request));
    }

    @Operation(summary = "해당 날짜 일정 가져오기", description = "해당 날짜 일정 가져오기")
    @GetMapping("/{date}")
    public ApiResponse<?> getEvents(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable LocalDateTime date) {
        User user = userDetails.getUser();
        return ResponseUtils.success(eventService.getEvents(user.getId(), date));
    }

    @Operation(summary = "해당 일정 수정하기", description = "해당 일정 수정하기")
    @PutMapping("/{eventId}")
    public ApiResponse<?> updateEvent(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long eventId, @RequestBody EventReqDto request) {
        User user = userDetails.getUser();
        return ResponseUtils.success(eventService.updateEvent(user.getId(), eventId, request));
    }

    @Operation(summary = "해당 일정 삭제하기", description = "해당 일정 삭제하기")
    @DeleteMapping("/{eventId}")
    public ApiResponse<?> deleteEvent(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long eventId) {
        User user = userDetails.getUser();
        eventService.deleteEvent(user.getId(), eventId);
        return ResponseUtils.success();
    }

}
