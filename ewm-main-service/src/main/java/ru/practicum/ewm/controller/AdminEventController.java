package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.dto.EventDto;
import ru.practicum.ewm.service.EventService;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public EventDto updateEventByAdmin(
            @PathVariable Long eventId,
            @RequestBody AdminUpdateEventRequest request) {

        return eventService.updateEventByAdmin(eventId, request);
    }
}