package com.example.route;


import org.junit.jupiter.api.Test;

class RouteTest {

    @Test
    void route를_생성할_수_있다() {
        Route route = Route.of("route", RouteType.GO);
    }
}
