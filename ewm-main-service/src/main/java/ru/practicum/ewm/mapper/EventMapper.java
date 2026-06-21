package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.EventDto;
import ru.practicum.ewm.dto.LocationDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;

public final class EventMapper {

    private EventMapper() {
    }

    public static Event toEntity(NewEventDto dto, User initiator, Category category) {
        LocationDto location = dto.getLocation();

        return Event.builder()
                .annotation(dto.getAnnotation())
                .category(category)
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .lat(location.getLat())
                .lon(location.getLon())
                .paid(dto.getPaid() != null ? dto.getPaid() : false)
                .participantLimit(dto.getParticipantLimit() != null ? dto.getParticipantLimit() : 0)
                .requestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration() : true)
                .title(dto.getTitle())
                .initiator(initiator)
                .createdOn(LocalDateTime.now())
                .state(EventState.PENDING)
                .views(0L)
                .build();
    }

    public static EventDto toDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory().getId())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(LocationDto.builder()
                        .lat(event.getLat())
                        .lon(event.getLon())
                        .build())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .initiator(event.getInitiator().getId())
                .views(event.getViews())
                .state(event.getState().name())
                .publishedOn(event.getPublishedOn())
                .build();
    }
}