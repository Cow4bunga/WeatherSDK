package com.ilevitsky.weatherapi.cache;

import com.ilevitsky.weatherapi.model.ForecastResponse;
import java.time.Instant;
import lombok.Getter;

@Getter
public class WeatherCacheEntry {
    private final ForecastResponse weatherData;
    private final Instant timestamp;

    public WeatherCacheEntry(ForecastResponse weatherData) {
        this.weatherData = weatherData;
        this.timestamp = Instant.now();
    }

    public boolean isOutdated() {
        return Instant.now().isAfter(timestamp.plusSeconds(600));
    }
}