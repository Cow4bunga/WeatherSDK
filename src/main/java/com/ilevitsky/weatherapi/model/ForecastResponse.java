package com.ilevitsky.weatherapi.model;

import lombok.Builder;

@Builder
public record ForecastResponse(
        Weather weather,
        Temperature temperature,
        int visibility,
        Wind wind,
        long datetime,
        Sys sys,
        int timezone,
        String name
) {
    public record Weather(String main, String description) {
    }

    public record Temperature(double temp, double feelsLike) {
    }

    public record Wind(double speed) {
    }

    public record Sys(long sunrise, long sunset) {
    }
}
