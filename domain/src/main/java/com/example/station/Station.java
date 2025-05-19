package com.example.station;


import com.example.common.Location;
import com.example.common.PersistableDomain;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

import static com.google.common.base.Preconditions.*;

@Getter
@SuperBuilder
public class Station extends PersistableDomain<String> {

    private String name;
    private Location location;

    public static Station create(String name, Location location) {
        checkNotNull(name, "Station name cannot be null");
        checkNotNull(location , "Location cannot be null");

        return Station.builder()
                .name(name)
                .location(location)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
}
