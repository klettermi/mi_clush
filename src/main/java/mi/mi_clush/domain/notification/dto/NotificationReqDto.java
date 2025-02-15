package mi.mi_clush.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;
import mi.mi_clush.domain.notification.entity.NotificationMethod;

@Getter
@Builder
public class NotificationReqDto {
    private Integer relativeTime;
    private NotificationMethod method;
}
