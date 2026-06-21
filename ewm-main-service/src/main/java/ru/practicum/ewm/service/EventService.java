package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.*;

import java.util.List;

public interface EventService {

    EventDto create(Long userId, NewEventDto dto);

    List<EventDto> getUserEvents(Long userId, int from, int size);

    EventDto getUserEventById(Long userId, Long eventId);

    EventDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest request);

    EventDto updateEventByAdmin(Long eventId, AdminUpdateEventRequest request);

    List<EventShortDto> getPublishedEvents(int from, int size);

    EventFullDto getPublishedEventById(Long eventId);
}