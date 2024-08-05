package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;
    private final RequestService requestService;
    private final CommentService commentService;

    @GetMapping
    public List<EventFullDto> getAllEvents(@PathVariable Long userId,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                           @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("---START GET ALL EVENTS---");
        return eventService.findAllEventsByUser(userId, PageRequest.of(from / size, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId,
                               @Valid @RequestBody NewEventDto newEventDto) {
        log.info("---START CREATE EVENT BY USER---");
        return eventService.addEventByUser(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto get(@PathVariable Long userId,
                            @PathVariable Long eventId) {
        log.info("---START FIND EVENT BY USER---");
        return eventService.findEventByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventUserRequest userRequest) {
        log.info("---START UPDATE EVENT BY USER---");
        return eventService.updateEventByUser(userId, eventId, userRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsEventByUser(@PathVariable Long userId,
                                                                @PathVariable Long eventId) {
        log.info("---START CREATE REQUEST EVENT BY USER---");
        return requestService.getRequestsEventByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@PathVariable Long userId,
                                                         @PathVariable Long eventId,
                                                         @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        log.info("---START UPDATE REQUEST EVENT BY USER---");
        return requestService.updateRequestsByUser(userId, eventId, updateRequest);
    }

    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId,
                                    @RequestParam Long eventId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.info("---START GET COMMENTS BY USER AND EVENT---");
        return commentService.createComment(userId, eventId, commentDto);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        log.info("---START DELETE COMMENT BY USER---");
        commentService.deleteCommentById(commentId, userId);
    }

    @PatchMapping("/comments/{commentId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.info("---START UPDATE COMMENT BY USER---");
        return commentService.updateCommentById(commentId, userId, commentDto);
    }
}