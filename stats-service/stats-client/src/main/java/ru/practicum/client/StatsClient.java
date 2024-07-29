package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatsClient {

    protected final RestTemplate rest;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void postHit(String app, String uri, String ip, LocalDateTime timestamp) {
        log.info("create hit app: {}, uri: {}, ip: {}, time: {}", app, uri, ip, timestamp);
        EndpointHit hit = EndpointHit.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();
        rest.postForEntity("/hit", new HttpEntity<>(hit), String.class);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<Long> id, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", (id.stream().map(eventId -> "/events/" + eventId).collect(Collectors.toList())),
                "unique", unique
        );
        return rest.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<ViewStats>>() {
                }, parameters).getBody();
    }

    public List<ViewStats> getStats(List<Long> id, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", LocalDateTime.now().minusYears(5).format(formatter),
                "end", LocalDateTime.now().plusYears(5).format(formatter),
                "uris", (id.stream().map(eventId -> "/events/" + eventId).collect(Collectors.toList())),
                "unique", unique
        );
        return rest.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<ViewStats>>() {
                }, parameters).getBody();
    }
}