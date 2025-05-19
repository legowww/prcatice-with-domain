package com.example.route;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RouteType {

    GO("출근"),

    BACK("퇴근");

    private final String desc;
}
