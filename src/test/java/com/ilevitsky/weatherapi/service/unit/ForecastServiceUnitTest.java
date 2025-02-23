package com.ilevitsky.weatherapi.service.unit;

import com.ilevitsky.weatherapi.client.WeatherApiClient;
import com.ilevitsky.weatherapi.mapper.WeatherMapper;
import com.ilevitsky.weatherapi.cache.WeatherCache;
import com.ilevitsky.weatherapi.model.ForecastResponse;
import com.ilevitsky.weatherapi.model.OpenWeatherMapResponse;
import com.ilevitsky.weatherapi.service.impl.ForecastServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ForecastServiceUnitTest {

    @Mock
    private WeatherApiClient weatherApiClient;

    @Mock
    private WeatherMapper weatherMapper;

    @Mock
    private WeatherCache weatherCache;

    @InjectMocks
    private ForecastServiceImpl forecastService;

    private ForecastResponse cachedResponse;
    private ForecastResponse forecastResponse;
    private OpenWeatherMapResponse apiResponse;
    private final String cityName = "Minsk";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cachedResponse = new ForecastResponse(
                new ForecastResponse.Weather("Clear", "clear sky"),
                new ForecastResponse.Temperature(271.44, 267.49),
                10000,
                new ForecastResponse.Wind(3.11),
                1740311799,
                new ForecastResponse.Sys(1740287530, 1740324864),
                10800,
                cityName
        );

        forecastResponse = new ForecastResponse(
                new ForecastResponse.Weather("Clear", "clear sky"),
                new ForecastResponse.Temperature(271.44, 267.49),
                10000,
                new ForecastResponse.Wind(3.11),
                1740311799,
                new ForecastResponse.Sys(1740287530, 1740324864),
                10800,
                cityName
        );

        apiResponse = new OpenWeatherMapResponse();
    }

    @Test
    void testGetForecastByCityName_Cached() {
        when(weatherCache.get(cityName)).thenReturn(cachedResponse);

        ForecastResponse result = forecastService.getForecastByCityName(cityName, anyString());

        assertEquals(cachedResponse, result);
        verify(weatherCache, times(1)).get(cityName);
        verify(weatherApiClient, never()).getWeatherByCityName(anyString(), anyString());
    }

    @Test
    void testGetForecastByCityName_NotCached() {
        when(weatherCache.get(cityName)).thenReturn(null);
        when(weatherApiClient.getWeatherByCityName(eq(cityName), anyString())).thenReturn(apiResponse);
        when(weatherMapper.toForecastResponse(apiResponse)).thenReturn(forecastResponse);

        ForecastResponse result = forecastService.getForecastByCityName(cityName, anyString());

        assertEquals(forecastResponse, result);
        verify(weatherCache, times(1)).put(cityName, forecastResponse);
    }

    @Test
    void testRemove() {
        forecastService.remove(cityName);

        verify(weatherCache, times(1)).remove(cityName);
    }

    @Test
    void testGetAllCityNames() {
        forecastService.getAllCityNames();

        verify(weatherCache, times(1)).getAllCityNames();
    }

    @Test
    void testAddResponse() {
        forecastService.addResponse(cityName, forecastResponse);

        verify(weatherCache, times(1)).put(cityName, forecastResponse);
    }
}