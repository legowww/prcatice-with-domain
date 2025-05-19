package com.example.turn.station;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TurnStationOnOffType {

    ON("승차"),
    OFF("하차");

    private final String desc;
}
