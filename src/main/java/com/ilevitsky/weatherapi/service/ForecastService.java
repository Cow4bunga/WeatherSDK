package com.ilevitsky.weatherapi.service;

import com.ilevitsky.weatherapi.model.ForecastResponse;

public interface ForecastService {
    ForecastResponse getForecastByCityName(String cityName, String apiKey);

    void remove(String cityName);

    Iterable<String> getAllCityNames();

    void addResponse(String cityName, ForecastResponse response);
}
