package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EventDto;
import ru.practicum.ewm.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventDto> getPublishedEvents(@RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return eventService.getPublishedEvents(from, size);
    }

    @GetMapping("/{eventId}")
    public EventDto getPublishedEventById(@PathVariable Long eventId) {
        return eventService.getPublishedEventById(eventId);
    }
}