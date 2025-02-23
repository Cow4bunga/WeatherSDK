package com.ilevitsky.weatherapi.mapper;

import com.ilevitsky.weatherapi.model.ForecastResponse;
import com.ilevitsky.weatherapi.model.OpenWeatherMapResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    @Mapping(target = "temperature", source = "main")
    @Mapping(target = "wind", source = "wind")
    @Mapping(target = "visibility", source = "main.visibility")
    @Mapping(target = "datetime", source = "dt")
    @Mapping(target = "sys", source = "sys")
    @Mapping(target = "timezone", constant = "0") // Adjust timezone as necessary
    @Mapping(target = "name", source = "name")
    @Mapping(target = "weather", source = "weather", qualifiedByName = "mapWeather")
    ForecastResponse toForecastResponse(OpenWeatherMapResponse weatherResponse);

    // Map nested objects
    default ForecastResponse.Temperature mapToTemperature(OpenWeatherMapResponse.Main main) {
        return new ForecastResponse.Temperature(main.getTemp(), main.getFeels_like());
    }

    default ForecastResponse.Wind mapToWind(OpenWeatherMapResponse.Wind wind) {
        return new ForecastResponse.Wind(wind.getSpeed());
    }

    default ForecastResponse.Sys mapToSys(OpenWeatherMapResponse.Sys sys) {
        return new ForecastResponse.Sys(sys.getSunrise(), sys.getSunset());
    }

    // Custom mapping method for Weather array
    @Named("mapWeather")
    default ForecastResponse.Weather mapWeather(OpenWeatherMapResponse.Weather[] weatherArray) {
        if (weatherArray != null && weatherArray.length > 0) {
            OpenWeatherMapResponse.Weather weather = weatherArray[0]; // Take the first element
            return new ForecastResponse.Weather(weather.getMain(), weather.getDescription());
        }
        return null; // Return null if the array is empty
    }
}