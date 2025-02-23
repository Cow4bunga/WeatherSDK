package com.ilevitsky.weatherapi.controller;

import com.ilevitsky.weatherapi.exception.SdkNotFoundException;
import com.ilevitsky.weatherapi.model.ForecastResponse;
import com.ilevitsky.weatherapi.model.Mode;
import com.ilevitsky.weatherapi.sdk.WeatherSdk;
import com.ilevitsky.weatherapi.sdk.WeatherSdkFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sdk")
@RequiredArgsConstructor
public class WeatherSdkController {

    private final WeatherSdkFactory sdkFactory;
    private final Map<String, WeatherSdk> sdkInstances = new HashMap<>();

    @PostMapping("/{apiKey}/{mode}")
    public void createSdk(@PathVariable String apiKey, @PathVariable Mode mode) {
        if (!sdkInstances.containsKey(apiKey)) {
            WeatherSdk sdk = sdkFactory.createSdk(apiKey, mode);
            sdkInstances.put(apiKey, sdk);
        }
    }

    @GetMapping("/{apiKey}/forecast/{cityName}")
    public ForecastResponse getForecast(@PathVariable String apiKey, @PathVariable String cityName) {
        WeatherSdk sdk = sdkInstances.get(apiKey);
        if (Objects.isNull(sdk)) {
            throw new SdkNotFoundException(String.format("SDK not found for API key %s.", apiKey));
        }
        return sdk.forecastService().getForecastByCityName(cityName);
    }

    @DeleteMapping("/{apiKey}")
    public void deleteSdk(@PathVariable String apiKey) {
        sdkFactory.deleteSdk(apiKey);
        sdkInstances.remove(apiKey);
    }
}
