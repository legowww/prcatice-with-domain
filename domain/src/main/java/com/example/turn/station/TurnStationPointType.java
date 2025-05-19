package com.example.turn.station;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TurnStationPointType {

    BEGIN("출발 정류장"),
    MIDDLE("중간 정류장"),
    END("도착 정류장");

    private final String desc;
}
