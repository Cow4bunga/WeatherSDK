package com.ilevitsky.weatherapi.service.impl;

import com.ilevitsky.weatherapi.cache.WeatherCache;
import com.ilevitsky.weatherapi.client.WeatherApiClient;
import com.ilevitsky.weatherapi.mapper.WeatherMapper;
import com.ilevitsky.weatherapi.model.ForecastResponse;
import com.ilevitsky.weatherapi.model.OpenWeatherMapResponse;
import com.ilevitsky.weatherapi.service.ForecastService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class ForecastServiceImpl implements ForecastService {
    private final WeatherApiClient weatherApiClient;
    private final WeatherMapper weatherMapper;
    private final WeatherCache weatherCache;

    @Override
    public ForecastResponse getForecastByCityName(String cityName, String apiKey) {
        ForecastResponse cachedWeather = weatherCache.get(cityName);
        if (cachedWeather != null) {
            log.info("Returning cached weather data for city: {}", cityName);
            return cachedWeather;
        }

        OpenWeatherMapResponse weatherResponse = weatherApiClient.getWeatherByCityName(cityName, apiKey);
        var res=weatherMapper.toForecastResponse(weatherResponse);
        weatherCache.put(cityName, res);
        return weatherMapper.toForecastResponse(weatherResponse);
    }

    @Override
    public void remove(String cityName) {
        weatherCache.remove(cityName);
    }

    @Override
    public Iterable<String> getAllCityNames() {
        return weatherCache.getAllCityNames();
    }

    @Override
    public void addResponse(String cityName, ForecastResponse response) {
        weatherCache.put(cityName, response);
    }
}
