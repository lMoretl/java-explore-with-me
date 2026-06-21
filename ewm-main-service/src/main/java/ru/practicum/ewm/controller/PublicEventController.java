package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getPublishedEvents(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return eventService.getPublishedEvents(from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getPublishedEventById(@PathVariable Long eventId) {
        return eventService.getPublishedEventById(eventId);
    }
}