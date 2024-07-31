package ru.practicum.request.service;

import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> findByRequestorId(Long userId);

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getRequestsEventByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestsByUser(Long userId,
                                                        Long eventId,
                                                        EventRequestStatusUpdateRequest updateRequest);
}
