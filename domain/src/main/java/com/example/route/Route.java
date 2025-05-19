package com.example.route;

import com.example.common.PersistableDomain;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

import static com.google.common.base.Preconditions.checkNotNull;

@SuperBuilder
@Getter
public class Route extends PersistableDomain<String> {

    private String name;
    private RouteType type;


    public static Route create(String name, RouteType type) {
        checkNotNull(name, "Route name cannot be null");
        checkNotNull(type, "Route type cannot be null");

        return Route.builder()
                .name(name)
                .type(type)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
}

