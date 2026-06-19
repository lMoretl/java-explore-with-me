package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EventDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.UpdateEventUserRequest;

import java.util.List;

public interface EventService {

    EventDto create(Long userId, NewEventDto dto);

    List<EventDto> getUserEvents(Long userId, int from, int size);

    EventDto getUserEventById(Long userId, Long eventId);

    EventDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest request);
}