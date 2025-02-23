package com.ilevitsky.weatherapi.sdk;

import com.ilevitsky.weatherapi.model.ForecastResponse;
import com.ilevitsky.weatherapi.model.Mode;
import com.ilevitsky.weatherapi.service.ForecastService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@Getter
@RequiredArgsConstructor
public class WeatherSdk {
    private final ForecastService forecastService;
    private final Mode mode;
    private final String apiKey;

    public void deleteCity(String cityName) {
        forecastService.remove(cityName);
    }

    @Scheduled(fixedRate = 100000)
    public void scheduledTask() {
        if (mode.equals(Mode.POLLING)) {
            forecastService.getAllCityNames().forEach(cityName -> {
                ForecastResponse weatherResponse = forecastService.getForecastByCityName(cityName, apiKey);
                forecastService.addResponse(cityName, weatherResponse);
            });
        }
    }
}
