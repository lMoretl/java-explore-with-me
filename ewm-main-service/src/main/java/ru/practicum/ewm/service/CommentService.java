package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto create(Long userId, Long eventId, NewCommentDto dto);

    CommentDto update(Long userId, Long commentId, UpdateCommentDto dto);

    void deleteByAuthor(Long userId, Long commentId);

    void deleteByAdmin(Long commentId);

    CommentDto getById(Long commentId);

    List<CommentDto> getByEvent(Long eventId, int from, int size);

    List<CommentDto> getAllByAdmin(int from, int size);
}