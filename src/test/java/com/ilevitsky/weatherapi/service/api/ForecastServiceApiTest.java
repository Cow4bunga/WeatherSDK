package com.ilevitsky.weatherapi.service.api;

import com.ilevitsky.weatherapi.cache.WeatherCache;
import com.ilevitsky.weatherapi.client.WeatherApiClient;
import com.ilevitsky.weatherapi.mapper.WeatherMapper;
import com.ilevitsky.weatherapi.model.ForecastResponse;
import com.ilevitsky.weatherapi.model.OpenWeatherMapResponse;
import com.ilevitsky.weatherapi.exception.DataRetrievalException;
import com.ilevitsky.weatherapi.service.impl.ForecastServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForecastServiceApiTest {

    @InjectMocks
    private ForecastServiceImpl forecastService;

    @Mock
    private WeatherApiClient weatherApiClient;

    @Mock
    private WeatherMapper weatherMapper;

    @Mock
    private WeatherCache weatherCache;

    private final String cityName = "Minsk";
    private final OpenWeatherMapResponse weatherApiResponse = new OpenWeatherMapResponse();
    private ForecastResponse forecastResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
        when(weatherMapper.toForecastResponse(weatherApiResponse)).thenReturn(forecastResponse);
    }

    @Test
    void testGetForecastByCityName_CachedData() {
        when(weatherCache.get(cityName)).thenReturn(forecastResponse);

        ForecastResponse result = forecastService.getForecastByCityName(cityName, anyString());

        assertNotNull(result);
        assertEquals(forecastResponse, result);
        verify(weatherCache, times(1)).get(cityName);
        verify(weatherApiClient, never()).getWeatherByCityName(anyString(), anyString());
    }

    @Test
    void testGetForecastByCityName_NotFound() {
        when(weatherCache.get(cityName)).thenReturn(null);
        when(weatherApiClient.getWeatherByCityName(eq(cityName), anyString())).thenThrow(new DataRetrievalException("City not found"));

        Exception exception = assertThrows(DataRetrievalException.class, () -> {
            forecastService.getForecastByCityName(cityName, anyString());
        });

        assertEquals("City not found", exception.getMessage());
        verify(weatherCache, times(1)).get(cityName);
        verify(weatherApiClient, times(1)).getWeatherByCityName(eq(cityName), anyString());
    }

    @Test
    void testGetForecastByCityName_Success() {
        when(weatherCache.get(cityName)).thenReturn(null);
        when(weatherApiClient.getWeatherByCityName(eq(cityName), anyString())).thenReturn(weatherApiResponse);

        ForecastResponse result = forecastService.getForecastByCityName(cityName, anyString());

        assertNotNull(result);
        assertEquals(forecastResponse, result);
        verify(weatherCache, times(1)).put(cityName, forecastResponse);
    }
}
