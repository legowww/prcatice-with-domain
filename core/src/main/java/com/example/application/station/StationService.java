package com.example.application.station;

import com.example.application.station.dto.StationWriteRequest;
import com.example.common.Location;
import com.example.station.Station;
import com.example.station.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;

    public Station findById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException("Station not found with id: " + stationId));
    }

    public void create(StationWriteRequest request) {
        stationRepository.save(Station.of(request.name(), Location.of(request.latitude(), request.longitude())));
    }
}
