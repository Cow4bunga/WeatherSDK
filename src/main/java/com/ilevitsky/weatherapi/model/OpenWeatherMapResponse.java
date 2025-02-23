package com.ilevitsky.weatherapi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OpenWeatherMapResponse {
    private Main main;
    private Weather[] weather;
    private Wind wind;
    private long dt;
    private String name;
    private Sys sys;

    @Data
    @Builder
    public static class Main {
        private double temp;
        private double feels_like;
        private int visibility;
    }

    @Data
    @Builder
    public static class Weather {
        private String main;
        private String description;
    }

    @Data
    @Builder
    public static class Wind {
        private double speed;
    }

    @Data
    @Builder
    public static class Sys {
        private long sunrise;
        private long sunset;
    }
}
