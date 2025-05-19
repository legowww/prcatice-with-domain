package com.example.turn.station;

import com.example.common.PersistableDomain;
import com.example.station.Station;
import com.example.turn.route.TurnRoute;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@SuperBuilder
public class TurnStation extends PersistableDomain<String> {

    private TurnRoute turnRoute;
    private Station station;
    private TurnStationOnOffType onOffType;
    private TurnStationPointType pointType;
    private int order;
    private OffsetDateTime expectedArrivalTime;

    public static TurnStation create(TurnRoute turnRoute, Station station, TurnStationOnOffType onOffType,
                               TurnStationPointType pointType, int order, OffsetDateTime expectedArrivalTime) {
        OffsetDateTime now = OffsetDateTime.now();
        now.truncatedTo(ChronoUnit.MINUTES);

        return TurnStation.builder()
                .turnRoute(turnRoute)
                .station(station)
                .onOffType(onOffType)
                .pointType(pointType)
                .order(order)
                .expectedArrivalTime(expectedArrivalTime)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
