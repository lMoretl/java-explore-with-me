package ru.practicum.ewm.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminUpdateEventRequest {

    private String annotation;

    private Long category;

    private String description;

    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private String title;

    private String stateAction;
}