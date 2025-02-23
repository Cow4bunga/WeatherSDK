package com.ilevitsky.weatherapi.model;

import java.util.Map;
import lombok.Data;

@Data
public class GeocodingApiResponse {
    private String name;
    private Map<String, String> localNames;
    private double lat;
    private double lon;
    private String country;
    private String state;
}
