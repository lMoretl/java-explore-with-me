package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long initiatorId);

    List<Event> findAllByState(EventState state, Pageable pageable);

    List<Event> findAllByState(EventState state);

    Optional<Event> findByIdAndState(Long eventId, EventState state);
}