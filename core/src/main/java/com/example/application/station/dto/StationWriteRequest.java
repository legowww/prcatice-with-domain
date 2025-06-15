package com.example.application.station.dto;

public record StationWriteRequest(
        String name,
        double latitude,
        double longitude
) {
}
