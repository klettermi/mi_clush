package mi.mi_clush.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import mi.mi_clush.domain.notification.dto.NotificationReqDto;
import mi.mi_clush.domain.notification.service.NotificationService;
import mi.mi_clush.global.dto.ApiResponse;
import mi.mi_clush.global.utils.ResponseUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/{eventId}/notifications")
    public ApiResponse<?> setNotification(@PathVariable Long eventId, @RequestBody NotificationReqDto request) {
        return ResponseUtils.success(notificationService.setNotification(eventId, request));
    }
}
