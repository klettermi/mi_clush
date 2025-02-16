package mi.mi_clush.domain.notification.repository;

import mi.mi_clush.domain.event.entity.Event;
import mi.mi_clush.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByEvent(Event event);
}
