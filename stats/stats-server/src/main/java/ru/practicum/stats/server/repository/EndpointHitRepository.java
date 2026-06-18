package ru.practicum.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stats.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("""
            SELECT h.app AS app, h.uri AS uri, COUNT(h.id) AS hits
            FROM EndpointHit h
            WHERE h.timestamp BETWEEN :start AND :end
            GROUP BY h.app, h.uri
            ORDER BY COUNT(h.id) DESC
            """)
    List<ViewStatsProjection> getStats(LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT h.app AS app, h.uri AS uri, COUNT(DISTINCT h.ip) AS hits
            FROM EndpointHit h
            WHERE h.timestamp BETWEEN :start AND :end
            GROUP BY h.app, h.uri
            ORDER BY COUNT(DISTINCT h.ip) DESC
            """)
    List<ViewStatsProjection> getUniqueStats(LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT h.app AS app, h.uri AS uri, COUNT(h.id) AS hits
            FROM EndpointHit h
            WHERE h.timestamp BETWEEN :start AND :end
              AND h.uri IN :uris
            GROUP BY h.app, h.uri
            ORDER BY COUNT(h.id) DESC
            """)
    List<ViewStatsProjection> getStatsByUris(LocalDateTime start,
                                             LocalDateTime end,
                                             @Param("uris") List<String> uris);

    @Query("""
            SELECT h.app AS app, h.uri AS uri, COUNT(DISTINCT h.ip) AS hits
            FROM EndpointHit h
            WHERE h.timestamp BETWEEN :start AND :end
              AND h.uri IN :uris
            GROUP BY h.app, h.uri
            ORDER BY COUNT(DISTINCT h.ip) DESC
            """)
    List<ViewStatsProjection> getUniqueStatsByUris(LocalDateTime start,
                                                   LocalDateTime end,
                                                   @Param("uris") List<String> uris);
}