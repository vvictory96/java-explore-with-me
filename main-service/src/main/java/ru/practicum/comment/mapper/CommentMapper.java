package ru.practicum.comment.mapper;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder().id(comment.getId()).createdOn(comment.getCreatedOn()).event(comment.getEvent().getId()).text(comment.getText()).author(comment.getAuthor().getId()).build();
    }

    public static Comment fromDto(CommentDto commentDto, User user, Event event) {
        return Comment.builder().text(commentDto.getText()).author(user).event(event).createdOn(LocalDateTime.now()).build();
    }

    public static List<CommentDto> toDtoList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toDto).collect(Collectors.toList());
    }
}
