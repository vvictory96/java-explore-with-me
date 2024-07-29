package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.enums.Sort;
import ru.practicum.event.enums.State;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto findEventById(Long id, HttpServletRequest request);

    List<EventFullDto> findAllEventsByUser(Long userId, Pageable pageable);

    EventFullDto addEventByUser(Long userId, NewEventDto newEventDto);

    EventFullDto findEventByUser(Long userId, Long eventId);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest request);


    List<EventFullDto> findAllEventsByAdmin(List<Long> userIds,
                                            List<State> states,
                                            List<Long> categoryIds,
                                            LocalDateTime start,
                                            LocalDateTime end,
                                            Pageable pageable);

    List<EventShortDto> findAllEventsByPublic(String text,
                                              List<Long> categories,
                                              Boolean paid,
                                              LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd,
                                              Boolean onlyAvailable,
                                              Sort sort,
                                              Pageable pageable,
                                              HttpServletRequest request);

    EventFullDto updateEventByUser(Long userId,
                                   Long eventId,
                                   UpdateEventUserRequest userRequest);
}
