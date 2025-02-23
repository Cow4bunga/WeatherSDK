package com.ilevitsky.weatherapi.service;

import com.ilevitsky.weatherapi.model.ForecastResponse;

public interface ForecastService {
    ForecastResponse getForecastByCityName(String cityName);
    void remove(String cityName);
}
