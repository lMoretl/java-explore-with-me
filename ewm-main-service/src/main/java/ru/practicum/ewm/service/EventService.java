package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

public interface EventService {

    EventDto create(Long userId, NewEventDto dto);

    List<EventDto> getUserEvents(Long userId, int from, int size);

    EventDto getUserEventById(Long userId, Long eventId);

    EventDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest request);

    EventDto updateEventByAdmin(Long eventId, AdminUpdateEventRequest request);

    List<EventShortDto> getPublishedEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            String sort,
            int from,
            int size,
            HttpServletRequest request);

    EventFullDto getPublishedEventById(
            Long eventId,
            HttpServletRequest request);

    List<EventFullDto> getEventsByAdmin(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size);
}