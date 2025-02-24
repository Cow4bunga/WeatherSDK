package com.ilevitsky.weatherapi.sdk;

import com.ilevitsky.weatherapi.exception.SdkCreationException;
import com.ilevitsky.weatherapi.model.Mode;
import com.ilevitsky.weatherapi.service.ForecastService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
@RequiredArgsConstructor
public class WeatherSdkFactory {
    private static final Map<String, WeatherSdk> sdkInstances = new HashMap<>();
    private final ForecastService forecastService;
    private final List<String> pollingApiKeys = new ArrayList<>();

    public WeatherSdk createSdk(String apiKey, Mode mode) {
        if (sdkInstances.containsKey(apiKey)) {
            throw new SdkCreationException(String.format("An SDK instance with %s API key already exists.", apiKey));
        }

        WeatherSdk sdkInstance = new WeatherSdk(forecastService, mode, apiKey);
        sdkInstances.put(apiKey, sdkInstance);

        if (mode.equals(Mode.POLLING)) {
            pollingApiKeys.add(apiKey);
        }

        return sdkInstance;
    }

    public void deleteSdk(String apiKey) {
        sdkInstances.remove(apiKey);
        pollingApiKeys.remove(apiKey);
    }

    public WeatherSdk getSdk(String apiKey) {
        return sdkInstances.get(apiKey);
    }

    public List<String> getSdkApiKeysInPollingMode() {
        return new ArrayList<>(pollingApiKeys);
    }
}