package ru.practicum.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.EndpointHit;
import ru.practicum.server.model.Stats;

@Mapper
public interface StatsMapper {

    StatsMapper STATS_MAPPER = Mappers.getMapper(StatsMapper.class);

    Stats endpointHitToStats(EndpointHit hit);
}
