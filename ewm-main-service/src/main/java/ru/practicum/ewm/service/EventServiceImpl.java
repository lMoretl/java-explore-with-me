package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.dto.EventDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.UpdateEventUserRequest;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.model.RequestStatus;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import jakarta.servlet.http.HttpServletRequest;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository requestRepository;
    private final StatsClient statsClient;
    private void checkPageParams(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }
    }

    private void saveHit(HttpServletRequest request) {
        EndpointHitDto hit = EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();

        statsClient.saveHit(hit);
    }

    @Override
    public EventDto create(Long userId, NewEventDto dto) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category with id=" + dto.getCategory() + " was not found"));

        if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException(
                    "Event date must be at least two hours after current time");
        }

        Event event = EventMapper.toEntity(dto, initiator, category);

        return EventMapper.toDto(eventRepository.save(event));
    }

    @Override
    public List<EventDto> getUserEvents(Long userId, int from, int size) {
        checkPageParams(from, size);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        return eventRepository
                .findAllByInitiatorId(userId, PageRequest.of(from / size, size))
                .stream()
                .map(EventMapper::toDto)
                .toList();
    }

    @Override
    public EventDto getUserEventById(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        return EventMapper.toDto(event);
    }

    @Override
    @Transactional
    public EventDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getLocation() != null) {
            event.setLat(request.getLocation().getLat());
            event.setLon(request.getLocation().getLon());
        }

        if (request.getCategory() != null) {
            Category category = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new NotFoundException(
                            "Category with id=" + request.getCategory() + " was not found"));

            event.setCategory(category);
        }

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException(
                    "Event date must be at least two hours after current time");
        }

        event.setState(EventState.PENDING);

        return EventMapper.toDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventDto updateEventByAdmin(Long eventId, AdminUpdateEventRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getLocation() != null) {
            event.setLat(request.getLocation().getLat());
            event.setLon(request.getLocation().getLon());
        }

        if (request.getCategory() != null) {
            Category category = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new NotFoundException(
                            "Category with id=" + request.getCategory() + " was not found"));

            event.setCategory(category);
        }

        if (request.getStateAction() != null) {
            if ("PUBLISH_EVENT".equals(request.getStateAction())) {
                if (event.getState() != EventState.PENDING) {
                    throw new ConflictException("Only pending events can be published");
                }

                if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                    throw new ConflictException(
                            "Event date must be at least one hour after publication");
                }

                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if ("REJECT_EVENT".equals(request.getStateAction())) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ConflictException("Published event cannot be rejected");
                }

                event.setState(EventState.CANCELED);
            }
        }

        return EventMapper.toDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size) {
        checkPageParams(from, size);

        return eventRepository.findAll()
                .stream()
                .filter(event -> users == null || users.isEmpty()
                        || users.contains(event.getInitiator().getId()))
                .filter(event -> states == null || states.isEmpty()
                        || states.contains(event.getState().name()))
                .filter(event -> categories == null || categories.isEmpty()
                        || categories.contains(event.getCategory().getId()))
                .filter(event -> rangeStart == null || event.getEventDate().isAfter(rangeStart))
                .filter(event -> rangeEnd == null || event.getEventDate().isBefore(rangeEnd))
                .skip(from)
                .limit(size)
                .map(event -> EventMapper.toFullDto(
                        event,
                        requestRepository.countByEventIdAndStatus(
                                event.getId(),
                                RequestStatus.CONFIRMED
                        )
                ))
                .toList();
    }

    @Override
    public List<EventShortDto> getPublishedEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            String sort,
            int from,
            int size,
            HttpServletRequest request) {
        checkPageParams(from, size);

        LocalDateTime start = rangeStart != null ? rangeStart : LocalDateTime.now();

        saveHit(request);

        return eventRepository.findAllByState(EventState.PUBLISHED)
                .stream()
                .filter(event -> event.getEventDate().isAfter(start))
                .filter(event -> rangeEnd == null || event.getEventDate().isBefore(rangeEnd))
                .filter(event -> text == null || text.isBlank()
                        || event.getAnnotation().toLowerCase().contains(text.toLowerCase())
                        || event.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(event -> categories == null || categories.isEmpty()
                        || categories.contains(event.getCategory().getId()))
                .filter(event -> paid == null || event.getPaid().equals(paid))
                .filter(event -> {
                    if (!onlyAvailable) {
                        return true;
                    }

                    if (event.getParticipantLimit() == 0) {
                        return true;
                    }

                    long confirmedRequests =
                            requestRepository.countByEventIdAndStatus(
                                    event.getId(),
                                    RequestStatus.CONFIRMED
                            );

                    return confirmedRequests < event.getParticipantLimit();
                })
                .map(event -> {
                    EventShortDto dto =
                            EventMapper.toShortDto(
                                    event,
                                    requestRepository.countByEventIdAndStatus(
                                            event.getId(),
                                            RequestStatus.CONFIRMED
                                    )
                            );

                    dto.setViews(getViews(event.getId()));

                    return dto;
                })
                .sorted((e1, e2) -> {
                    if ("VIEWS".equals(sort)) {
                        return Long.compare(e2.getViews(), e1.getViews());
                    }

                    return e1.getEventDate().compareTo(e2.getEventDate());
                })
                .skip(from)
                .limit(size)
                .toList();
    }

    @Override
    public EventFullDto getPublishedEventById(
            Long eventId,
            HttpServletRequest request) {

        Event event = eventRepository
                .findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Event with id=" + eventId + " was not found"));

        saveHit(request);

        long confirmedRequests = requestRepository.countByEventIdAndStatus(
                event.getId(),
                RequestStatus.CONFIRMED
        );

        EventFullDto dto =
                EventMapper.toFullDto(event, confirmedRequests);

        dto.setViews(getViews(event.getId()));

        return dto;
    }

    private long getViews(Long eventId) {

        String uri = "/events/" + eventId;

        List<ViewStatsDto> stats = statsClient.getStats(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.now().plusSeconds(1),
                List.of(uri),
                true
        );

        if (stats.isEmpty()) {
            return 0L;
        }

        return stats.get(0).getHits();
    }
}