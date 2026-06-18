package ru.practicum.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.mapper.EndpointHitMapper;
import ru.practicum.stats.server.repository.EndpointHitRepository;
import ru.practicum.stats.server.repository.ViewStatsProjection;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final EndpointHitRepository repository;

    @Override
    public void saveHit(EndpointHitDto hitDto) {
        repository.save(EndpointHitMapper.toEntity(hitDto));
    }

    @Override
    public List<ViewStatsDto> getStats(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            Boolean unique
    ) {

        List<ViewStatsProjection> stats;

        if (uris == null || uris.isEmpty()) {
            stats = unique
                    ? repository.getUniqueStats(start, end)
                    : repository.getStats(start, end);
        } else {
            stats = unique
                    ? repository.getUniqueStatsByUris(start, end, uris)
                    : repository.getStatsByUris(start, end, uris);
        }

        return stats.stream()
                .map(stat -> ViewStatsDto.builder()
                        .app(stat.getApp())
                        .uri(stat.getUri())
                        .hits(stat.getHits())
                        .build())
                .toList();
    }
}