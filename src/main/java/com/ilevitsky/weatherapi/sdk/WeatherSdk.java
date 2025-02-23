package com.ilevitsky.weatherapi.sdk;

import com.ilevitsky.weatherapi.model.Mode;
import com.ilevitsky.weatherapi.service.ForecastService;

public record WeatherSdk(ForecastService forecastService, Mode mode) {
    public void deleteCity(String cityName) {
        forecastService.remove(cityName);
    }
}
