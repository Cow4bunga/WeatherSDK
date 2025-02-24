package com.ilevitsky.weatherapi.sdk;

import com.ilevitsky.weatherapi.model.Mode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class WeatherScheduler {
    private final WeatherSdkFactory sdkFactory;

    public WeatherScheduler(WeatherSdkFactory sdkFactory) {
        this.sdkFactory = sdkFactory;
    }

    @Scheduled(fixedRate = 1000)
    public void runScheduledTasks() {
        for (String apiKey : sdkFactory.getSdkApiKeysInPollingMode()) {
            WeatherSdk sdk = sdkFactory.getSdk(apiKey);
            if (sdk != null && sdk.getMode().equals(Mode.POLLING)) {
                sdk.scheduledTask();
            }
        }
    }
}