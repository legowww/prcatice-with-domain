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
public class TurnStation extends PersistableDomain<Long> {

    private TurnRoute turnRoute;
    private Station station;
    private TurnStationOnOffType onOffType;
    private TurnStationType pointType;
    private int order;
    private OffsetDateTime expectedArrivalTime;

    public static TurnStation of(TurnRoute turnRoute, Station station, TurnStationOnOffType onOffType,
                                     TurnStationType pointType, int order, OffsetDateTime expectedArrivalTime) {
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
