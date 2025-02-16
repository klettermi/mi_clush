package mi.mi_clush.domain.event.repository;

import mi.mi_clush.domain.event.entity.Event;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepositoryCustom {
    List<Event> findByUserIdAndStartTime(Long user_id, LocalDateTime startTime);
}
