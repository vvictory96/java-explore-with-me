package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.server.model.Stats;
import ru.practicum.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.server.mapper.StatsMapper.STATS_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Transactional
    @Override
    public void postHit(EndpointHit hit) {
        Stats stats = STATS_MAPPER.endpointHitToStats(hit);
        log.info("Create new hit: {}", stats);
        statsRepository.save(STATS_MAPPER.endpointHitToStats(hit));
        log.info("Hit was create");
    }


    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Get stats by start [{}],\n end [{}],\n uris [{}],\n unique [{}]", start, end, uris, unique);
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return statsRepository.getAllUniqueStats(start, end);
            } else {
                return statsRepository.getAllStats(start, end);
            }
        } else {
            if (unique) {
                return statsRepository.getUniqueStatsByUris(start, end, uris);
            } else {
                return statsRepository.getStatsByUris(start, end, uris);
            }
        }
    }

}
