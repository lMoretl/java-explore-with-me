package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EventDto;
import ru.practicum.ewm.dto.NewEventDto;

import java.util.List;

public interface EventService {

    EventDto create(Long userId, NewEventDto dto);

    List<EventDto> getUserEvents(Long userId, int from, int size);

    EventDto getUserEventById(Long userId, Long eventId);
}