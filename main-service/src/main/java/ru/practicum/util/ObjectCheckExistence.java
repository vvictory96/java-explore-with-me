package ru.practicum.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ObjectCheckExistence {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final CompilationRepository compilationRepository;
    private final CommentRepository commentRepository;


    public Event getEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Event %d not found", id))
        );
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("User id %d not found", id))
        );
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Category id %d not found", id))
        );
    }

    public ParticipationRequest getRequest(Long id) {
        return requestRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Request id %d not found", id))
        );
    }

    public Compilation getCompilation(Long id) {
        return compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Compilation id %d not found", id))
        );
    }

    public void getDateTime(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new ValidationException("Start can't be after the end");
        }
    }

    public void checkCategoryExistence(NewCategoryDto category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new ConflictException(String.format("Category %s already exists",
                    category.getName()));
        }
    }

    public Comment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id %d not found", id)));
    }

    public Long getCommentsCount(Long eventId) {
        return commentRepository.countAllByEventId(eventId);
    }

    public void checkUserIsAuthorComment(Long authorId, Long userId, Long commentId) {
        if (!Objects.equals(authorId, userId)) {
            throw new ValidationException(String.format(
                    "User %d isn't owner of comment %d",
                    userId, commentId));
        }
    }

    public void checkComment(Event event, User user) {
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Event status should be PUBLISHED");
        }

        if (!Objects.equals(event.getInitiator().getId(), user.getId())) {
            ParticipationRequest result = requestRepository.findByRequesterIdAndEventId(user.getId(), event.getId())
                    .orElseThrow(() -> new ValidationException(String.format("User %d doesn't participate in event %d",
                            user.getId(), event.getId())));
            if (result.getStatus() != RequestStatus.CONFIRMED) {
                throw new ValidationException(String.format("User %d doesn't participate in event %d",
                        user.getId(), event.getId()));
            }
        }

        Optional<Comment> foundComment = commentRepository.findByEventIdAndAuthorId(event.getId(), user.getId());
        if (foundComment.isPresent()) {
            throw new ConflictException(String.format("Пользователь с id='%s' уже оставлял комментарий к событию " +
                    "с id='%s'", user.getId(), event.getId()));
        }
    }

}
