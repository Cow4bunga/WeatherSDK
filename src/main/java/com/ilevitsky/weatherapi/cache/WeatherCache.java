package com.ilevitsky.weatherapi.cache;

import com.ilevitsky.weatherapi.model.ForecastResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class WeatherCache {
    @Value("${cache-size.maxSize:10}")
    private int maxSize;
    private final Map<String, WeatherCacheEntry> cache;

    public WeatherCache() {
        this.cache = new LinkedHashMap<String, WeatherCacheEntry>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, WeatherCacheEntry> eldest) {
                return size() > maxSize;
            }
        };
    }

    public ForecastResponse get(String cityName) {
        WeatherCacheEntry entry = cache.get(cityName);
        if (entry != null && !entry.isOutdated()) {
            return entry.getWeatherData();
        }
        return null;
    }

    public void put(String cityName, ForecastResponse weatherData) {
        if (cache.containsKey(cityName)) {
            cache.remove(cityName);
        }
        cache.put(cityName, new WeatherCacheEntry(weatherData));
    }

    public void remove(String cityName) {
        cache.remove(cityName);
    }

    public Set<String> getAllCityNames() {
        return cache.keySet();
    }

    public void clear() {
        cache.clear();
    }
}
