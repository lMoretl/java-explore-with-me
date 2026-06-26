package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByEventIdOrderByCreatedDesc(Long eventId, Pageable pageable);

    Page<Comment> findAllByOrderByCreatedDesc(Pageable pageable);

    Optional<Comment> findByIdAndAuthorId(Long commentId, Long authorId);
}