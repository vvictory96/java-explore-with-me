package ru.practicum.event.dto;

import ru.practicum.event.enums.StateAction;
import ru.practicum.location.model.Location;

import java.time.LocalDateTime;

public interface UpdateEventRequest {
    String getAnnotation();

    Long getCategory();

    String getDescription();

    LocalDateTime getEventDate();

    Location getLocation();

    Boolean getPaid();

    Integer getParticipantLimit();

    Boolean getRequestModeration();

    StateAction getStateAction();

    String getTitle();
}
