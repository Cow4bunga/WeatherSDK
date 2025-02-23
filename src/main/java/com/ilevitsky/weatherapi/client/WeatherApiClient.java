package com.ilevitsky.weatherapi.client;

import com.ilevitsky.weatherapi.model.GeocodingApiResponse;
import com.ilevitsky.weatherapi.model.OpenWeatherMapResponse;
import com.ilevitsky.weatherapi.exception.DataRetrievalException;
import jakarta.annotation.PostConstruct;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherApiClient {
    private final RestTemplate restTemplate;
    @Value("${openweatherapi.api-key}")
    private String weatherApiKey;
    @Value("${openweatherapi.weatherApiUrl}")
    private String weatherApiUrl;
    @Value("${openweatherapi.geocodingApiUrl}")
    private String geocodingApiUrl;

    public OpenWeatherMapResponse getWeatherByCityName(String cityName) {
        String geocodingUrl = formGeoUrl(cityName);
        GeocodingApiResponse[] geocodingResponse =
                restTemplate.getForObject(geocodingUrl, GeocodingApiResponse[].class);

        if (Objects.isNull(geocodingResponse) || geocodingResponse.length == 0) {
            throw new DataRetrievalException("City not found");
        }

        double lat = geocodingResponse[0].getLat();
        double lon = geocodingResponse[0].getLon();

        String weatherUrl = formWeatherUrl(lat, lon);
        OpenWeatherMapResponse weatherResponse = restTemplate.getForObject(weatherUrl, OpenWeatherMapResponse.class);

        if (Objects.isNull(weatherResponse)) {
            throw new DataRetrievalException("Failed to fetch weather data");
        }

        return weatherResponse;
    }

    private String formGeoUrl(String cityName) {
        return UriComponentsBuilder.fromHttpUrl(geocodingApiUrl)
                .queryParam("q", cityName)
                .queryParam("limit", 1)
                .queryParam("appid", weatherApiKey)
                .toUriString();
    }

    private String formWeatherUrl(double lat, double lon) {
        return UriComponentsBuilder.fromHttpUrl(weatherApiUrl)
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", weatherApiKey)
                .toUriString();
    }
}
