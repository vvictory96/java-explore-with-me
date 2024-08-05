package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.exception.ConflictException;
import ru.practicum.user.model.User;
import ru.practicum.util.ObjectCheckExistence;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ObjectCheckExistence objectCheckExistence;

    @Transactional
    @Override
    public CommentDto createComment(Long userId, Long eventId, CommentDto commentDto) {
        User user = objectCheckExistence.getUser(userId);
        Event event = objectCheckExistence.getEvent(eventId);

        checkComment(event, user);

        Comment comment = CommentMapper.fromDto(commentDto, user, event);
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void deleteCommentById(Long commentId, Long userId) {
        Comment comment = objectCheckExistence.getComment(commentId);

        checkUserIsAuthorComment(comment.getAuthor().getId(), userId, commentId);

        commentRepository.deleteById(commentId);
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId) {
        objectCheckExistence.getComment(commentId);
        commentRepository.deleteById(commentId);
    }

    @Transactional
    @Override
    public CommentDto updateCommentById(Long commentId, Long userId, CommentDto commentDto) {
        Comment foundComment = objectCheckExistence.getComment(commentId);

        checkUserIsAuthorComment(foundComment.getAuthor().getId(), userId, commentId);

        String newText = commentDto.getText();
        if (StringUtils.hasLength(newText)) {
            foundComment.setText(newText);
        }

        Comment savedComment = commentRepository.save(foundComment);
        return CommentMapper.toDto(savedComment);
    }

    @Override
    public List<CommentDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size) {
        objectCheckExistence.getEvent(eventId);

        PageRequest pageRequest = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findAllByEventIdOrderByCreatedOnDesc(eventId, pageRequest);

        return CommentMapper.toDtoList(comments);
    }

    private void checkUserIsAuthorComment(Long authorId, Long userId, Long commentId) {
        if (!Objects.equals(authorId, userId)) {
            throw new ValidationException(String.format(
                    "User %d isn't owner of event %d",
                    userId, commentId));
        }
    }

    private void checkComment(Event event, User user) {
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Event status should be PUBLISHED");
        }

        if (!event.getInitiator().equals(user)) {
            objectCheckExistence.checkRequestExistenceByEventIdAndUserId(event.getId(), user.getId());
        }

        objectCheckExistence.checkCommentExistenceByAuthorIdAndEventId(event.getId(), user.getId());
    }
}
