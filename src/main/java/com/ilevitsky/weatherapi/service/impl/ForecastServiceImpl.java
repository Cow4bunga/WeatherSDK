package com.ilevitsky.weatherapi.service.impl;

import com.ilevitsky.weatherapi.cache.WeatherCache;
import com.ilevitsky.weatherapi.client.WeatherApiClient;
import com.ilevitsky.weatherapi.mapper.WeatherMapper;
import com.ilevitsky.weatherapi.model.ForecastResponse;
import com.ilevitsky.weatherapi.model.OpenWeatherMapResponse;
import com.ilevitsky.weatherapi.service.ForecastService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForecastServiceImpl implements ForecastService {
    private final WeatherApiClient weatherApiClient;
    private final WeatherMapper weatherMapper;
    private final Environment environment;

    private final WeatherCache weatherCache =new WeatherCache();
    private boolean isPollingEnabled;

    @Override
    public ForecastResponse getForecastByCityName(String cityName) {
        ForecastResponse cachedWeather = weatherCache.get(cityName);
        if (cachedWeather != null) {
            log.info("Returning cached weather data for city: {}", cityName);
            return cachedWeather;
        }

        OpenWeatherMapResponse weatherResponse = weatherApiClient.getWeatherByCityName(cityName);
        weatherCache.put(cityName, weatherMapper.toForecastResponse(weatherResponse));
        return weatherMapper.toForecastResponse(weatherResponse);
    }

    @Override
    public void remove(String cityName) {
        weatherCache.remove(cityName);
    }

    @PostConstruct
    public void init() {
        isPollingEnabled = "polling".equals(environment.getProperty("polling.mode"));
    }

    @Scheduled(fixedRate = 100000)
    public void scheduledTask() {
        if (isPollingEnabled) {
            weatherCache.getAllCityNames().forEach(cityName -> {
                ForecastResponse weatherResponse = getForecastByCityName(cityName);
                weatherCache.put(cityName, weatherResponse);
            });
        }
    }
}
