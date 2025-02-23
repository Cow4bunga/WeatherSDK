package com.ilevitsky.weatherapi.sdk;

import com.ilevitsky.weatherapi.exception.SdkCreationException;
import com.ilevitsky.weatherapi.model.Mode;
import com.ilevitsky.weatherapi.service.ForecastService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeatherSdkFactory {
    private static final Map<String, WeatherSdk> sdkInstances = new HashMap<>();
    private final ForecastService forecastService;

    public WeatherSdk createSdk(String apiKey, Mode mode) {
        if (sdkInstances.containsKey(apiKey)) {
            throw new SdkCreationException(String.format("An SDK instance with %s API key already exists.", apiKey));
        }

        WeatherSdk sdkInstance = new WeatherSdk(forecastService, mode);
        sdkInstances.put(apiKey, sdkInstance);
        return sdkInstance;
    }

    public void deleteSdk(String apiKey) {
        sdkInstances.remove(apiKey);
    }

    public WeatherSdk getSdk(String apiKey) {
        return sdkInstances.get(apiKey);
    }
}
