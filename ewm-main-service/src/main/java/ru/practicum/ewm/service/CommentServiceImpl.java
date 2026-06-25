package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.dto.UpdateCommentDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto create(Long userId, Long eventId, NewCommentDto dto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getState() != EventState.PUBLISHED) {
            throw new IllegalArgumentException("Only published events can be commented");
        }

        Comment comment = CommentMapper.toEntity(dto, event, author);

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto update(Long userId, Long commentId, UpdateCommentDto dto) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));

        comment.setText(dto.getText());
        comment.setUpdated(LocalDateTime.now());

        return CommentMapper.toDto(comment);
    }

    @Override
    public void deleteByAuthor(Long userId, Long commentId) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() ->
                        new NotFoundException("Comment with id=" + commentId + " was not found"));

        commentRepository.delete(comment);
    }

    @Override
    public void deleteByAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new NotFoundException("Comment with id=" + commentId + " was not found"));

        commentRepository.delete(comment);
    }

    @Override
    public CommentDto getById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));

        return CommentMapper.toDto(comment);
    }

    @Override
    public List<CommentDto> getByEvent(Long eventId, int from, int size) {
        checkPageParams(from, size);

        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        return commentRepository.findAllByEventIdOrderByCreatedDesc(eventId, PageRequest.of(from / size, size))
                .stream()
                .map(CommentMapper::toDto)
                .toList();
    }

    @Override
    public List<CommentDto> getAllByAdmin(int from, int size) {
        checkPageParams(from, size);

        return commentRepository.findAllByOrderByCreatedDesc(PageRequest.of(from / size, size))
                .stream()
                .map(CommentMapper::toDto)
                .toList();
    }

    private void checkPageParams(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }
    }
}