package ru.practicum.ewm.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventSpecification {

    public static Specification<Event> adminFilter(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd) {

        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();

            if (users != null && !users.isEmpty()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        root.get("initiator").get("id").in(users)
                );
            }

            if (states != null && !states.isEmpty()) {
                List<EventState> eventStates = states.stream()
                        .map(EventState::valueOf)
                        .toList();

                predicate = criteriaBuilder.and(
                        predicate,
                        root.get("state").in(eventStates)
                );
            }

            if (categories != null && !categories.isEmpty()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        root.get("category").get("id").in(categories)
                );
            }

            if (rangeStart != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("eventDate"),
                                rangeStart
                        )
                );
            }

            if (rangeEnd != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("eventDate"),
                                rangeEnd
                        )
                );
            }

            return predicate;
        };
    }
}