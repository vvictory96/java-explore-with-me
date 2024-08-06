package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;
import ru.practicum.util.ObjectCheckExistence;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ObjectCheckExistence objectCheckExistence;
    private final CommentMapper commentMapper;

    @Transactional
    @Override
    public CommentDto createComment(Long userId, Long eventId, CommentDto commentDto) {
        User user = objectCheckExistence.getUser(userId);
        Event event = objectCheckExistence.getEvent(eventId);

        objectCheckExistence.checkComment(event, user);

        Comment comment = commentMapper.fromDto(commentDto, user, event);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void deleteCommentById(Long commentId, Long userId) {
        Comment comment = objectCheckExistence.getComment(commentId);

        objectCheckExistence.checkUserIsAuthorComment(comment.getAuthor().getId(), userId, commentId);

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
        log.info("коммент найдет {}", foundComment.getId());

        objectCheckExistence.checkUserIsAuthorComment(foundComment.getAuthor().getId(), userId, commentId);

        String newText = commentDto.getText();
        if (StringUtils.hasLength(newText)) {
            foundComment.setText(newText);
        }

        Comment savedComment = commentRepository.save(foundComment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    public List<CommentDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size) {
        objectCheckExistence.getEvent(eventId);

        PageRequest pageRequest = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findAllByEventIdOrderByCreatedOnDesc(eventId, pageRequest);

        return commentMapper.toDtoList(comments);
    }
    
}
