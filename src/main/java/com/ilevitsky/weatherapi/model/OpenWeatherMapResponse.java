package com.ilevitsky.weatherapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OpenWeatherMapResponse {
    private Coord coord;
    private Weather[] weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private long dt;
    private Sys sys;
    private int timezone;
    private long id;
    private String name;
    private int cod;

    @Data
    @NoArgsConstructor
    public static class Coord {
        private double lon;
        private double lat;
    }

    @Data
    @NoArgsConstructor
    public static class Main {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;
        private int sea_level;
        private int grnd_level;
        private int visibility;
    }

    @Data
    @NoArgsConstructor
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;
    }

    @Data
    @NoArgsConstructor
    public static class Wind {
        private double speed;
        private int deg;
        private double gust;
    }

    @Data
    @NoArgsConstructor
    public static class Clouds {
        private int all;
    }

    @Data
    @NoArgsConstructor
    public static class Sys {
        private String country;
        private long sunrise;
        private long sunset;
    }
}