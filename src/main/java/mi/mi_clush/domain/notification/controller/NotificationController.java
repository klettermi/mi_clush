package mi.mi_clush.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mi.mi_clush.domain.notification.dto.NotificationReqDto;
import mi.mi_clush.domain.notification.service.NotificationService;
import mi.mi_clush.global.dto.ApiResponse;
import mi.mi_clush.global.utils.ResponseUtils;
import org.springframework.web.bind.annotation.*;

@Tag(name = "알림 관련 API", description = "알림 관련 API")
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "해당 일정 알림 전송", description = "해당 일정 알림 전송")
    @PostMapping("/{eventId}/notifications")
    public ApiResponse<?> setNotification(@PathVariable Long eventId, @RequestBody NotificationReqDto request) {
        return ResponseUtils.success(notificationService.setNotification(eventId, request));
    }
}
