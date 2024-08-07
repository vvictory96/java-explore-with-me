package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(Long userId, Long eventId, CommentDto commentDto);

    void deleteCommentById(Long commentId, Long userId);

    void deleteComment(Long commentId);

    CommentDto updateCommentById(Long commentId, Long userId, CommentDto commentDto);

    List<CommentDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size);
}