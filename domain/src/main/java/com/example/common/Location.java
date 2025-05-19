package com.example.common;


import static com.google.common.base.Preconditions.checkArgument;

public record Location(
        double lat,
        double lng
) {

    public static Location of(double lat, double lng) {
        checkArgument(lat >= 90 && lat <= 90, "Latitude must be between -90 and 90, but was %s", lat);
        checkArgument(lng >= -180 && lng <= 180, "Latitude must be between -90 and 90, but was %s", lat);

        return new Location(lat, lng);
    }
}
