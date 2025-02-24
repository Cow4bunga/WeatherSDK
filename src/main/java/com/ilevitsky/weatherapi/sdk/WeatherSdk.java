package com.ilevitsky.weatherapi.sdk;

import com.ilevitsky.weatherapi.model.ForecastResponse;
import com.ilevitsky.weatherapi.model.Mode;
import com.ilevitsky.weatherapi.service.ForecastService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@RequiredArgsConstructor
public class WeatherSdk {
    private final ForecastService forecastService;
    private final Mode mode;
    private final String apiKey;

    public void scheduledTask() {
        if (mode.equals(Mode.POLLING)) {
            List<String> cityNames = new ArrayList<>();
            forecastService.getAllCityNames().forEach(cityNames::add);

            cityNames.forEach(cityName -> {
                log.info("Extracting data for {}", cityName);
                ForecastResponse weatherResponse = forecastService.getForecastByCityName(cityName, apiKey);
                forecastService.addResponse(cityName, weatherResponse);
                log.info("Successfully updated data for {}", cityName);
            });
        }
    }
}