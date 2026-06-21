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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

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
    public List<EventDto> getPublishedEvents(int from, int size) {
        return eventRepository
                .findAllByState(
                        EventState.PUBLISHED,
                        PageRequest.of(from / size, size))
                .stream()
                .map(EventMapper::toDto)
                .toList();
    }

    @Override
    public EventDto getPublishedEventById(Long eventId) {
        Event event = eventRepository
                .findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Event with id=" + eventId + " was not found"));

        return EventMapper.toDto(event);
    }
}