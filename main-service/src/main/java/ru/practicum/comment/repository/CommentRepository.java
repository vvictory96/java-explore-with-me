package ru.practicum.comment.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comment.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Long countAllByEventId(long eventId);

    List<Comment> findAllByEventIdOrderByCreatedOnDesc(Long eventId, PageRequest of);

    Optional<Comment> findByEventIdAndAuthorId(Long eventId, Long userId);
}
