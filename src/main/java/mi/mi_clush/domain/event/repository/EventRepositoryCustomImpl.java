package mi.mi_clush.domain.event.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mi.mi_clush.domain.event.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

import static mi.mi_clush.domain.event.entity.QEvent.event;

@RequiredArgsConstructor
public class EventRepositoryCustomImpl implements EventRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Event> findByUserIdAndStartTime(Long user_id, LocalDateTime startTime) {
        return queryFactory.selectFrom(event)
                .where(event.user.id.eq(user_id)
                        .and(event.startTime.goe(startTime))
                        .and(event.startTime.loe(startTime.plusDays(1))))
                .fetch();
    }

}
