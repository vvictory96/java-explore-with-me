package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void postHit(@RequestBody EndpointHit hit) {
        log.info("---START POST HIT ENDPOINT---");
        statsService.postHit(hit);
    }

    @GetMapping(name = "/stats")
    public List<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = FORMAT) LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = FORMAT) LocalDateTime end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("---START GET STATS ENDPOINT---");
        if (start.isAfter(end)) {
            throw new ValidationException(
                    String.format("Unexpected time interval: start %s; end %s", start, end));
        }
        return statsService.getStats(start, end, uris, unique);
    }
}
