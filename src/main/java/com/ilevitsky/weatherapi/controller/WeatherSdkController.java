package com.ilevitsky.weatherapi.controller;

import com.ilevitsky.weatherapi.config.RestPoint;
import com.ilevitsky.weatherapi.exception.SdkNotFoundException;
import com.ilevitsky.weatherapi.model.ForecastResponse;
import com.ilevitsky.weatherapi.model.Mode;
import com.ilevitsky.weatherapi.sdk.WeatherSdk;
import com.ilevitsky.weatherapi.sdk.WeatherSdkFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(path = RestPoint.SDK, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "SDK controller", description = "Controller providing SDK functionality")
public class WeatherSdkController {

    private final WeatherSdkFactory sdkFactory;
    private final Map<String, WeatherSdk> sdkInstances = new HashMap<>();

    @PostMapping("/{apiKey}/{mode}")
    @Operation(summary = "Create SDK", description = "Creates a new SDK instance for the provided API key and mode.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SDK created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters")
    })
    public void createSdk(
            @Parameter(description = "API key for the SDK", required = true) @PathVariable String apiKey,
            @Parameter(description = "Mode for the SDK", required = true) @PathVariable Mode mode) {
        if (!sdkInstances.containsKey(apiKey)) {
            WeatherSdk sdk = sdkFactory.createSdk(apiKey, mode);
            sdkInstances.put(apiKey, sdk);
        }
    }

    @GetMapping("/{apiKey}/forecast/{cityName}")
    @Operation(summary = "Get forecast", description = "Retrieves the weather forecast for a specified city.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Forecast retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ForecastResponse.class))),
            @ApiResponse(responseCode = "404", description = "SDK not found for the provided API key")
    })
    public ForecastResponse getForecast(
            @Parameter(description = "API key for the SDK", required = true) @PathVariable String apiKey,
            @Parameter(description = "City name for the forecast", required = true) @PathVariable String cityName) {
        WeatherSdk sdk = sdkInstances.get(apiKey);
        if (Objects.isNull(sdk)) {
            throw new SdkNotFoundException(String.format("SDK not found for API key %s.", apiKey));
        }
        return sdk.getForecastService().getForecastByCityName(cityName, apiKey);
    }

    @DeleteMapping("/{apiKey}")
    @Operation(summary = "Delete SDK", description = "Deletes the SDK instance associated with the provided API key.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SDK deleted successfully"),
            @ApiResponse(responseCode = "404", description = "SDK not found for the provided API key")
    })
    public void deleteSdk(
            @Parameter(description = "API key for the SDK", required = true) @PathVariable String apiKey) {
        sdkFactory.deleteSdk(apiKey);
        sdkInstances.remove(apiKey);
    }
}