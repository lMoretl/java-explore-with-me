package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EventDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.UpdateEventUserRequest;
import ru.practicum.ewm.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto create(@PathVariable Long userId,
                           @RequestBody NewEventDto dto) {
        return eventService.create(userId, dto);
    }

    @GetMapping
    public List<EventDto> getUserEvents(@PathVariable Long userId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventDto getUserEventById(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable Long userId,
                                @PathVariable Long eventId,
                                @RequestBody UpdateEventUserRequest request) {
        return eventService.updateEvent(userId, eventId, request);
    }
}