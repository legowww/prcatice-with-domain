package com.example.station;

import java.util.Optional;

public interface StationRepository {

    Station save(Station station);

    Optional<Station> findById(Long id);
}
