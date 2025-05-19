package com.example.turn.route;

import com.example.common.PersistableDomain;
import com.example.route.Route;
import com.example.turn.station.TurnStation;
import com.example.turn.station.TurnStations;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@SuperBuilder
public class TurnRoute extends PersistableDomain<String> {

    private String name;
    private Route route;
    private TurnStations turnStations;

    public static TurnRoute create(Route route, List<TurnStation> turnStations) {
        return TurnRoute.builder()
                .route(route)
                .turnStations(TurnStations.of(turnStations))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
}
