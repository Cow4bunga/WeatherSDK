package com.ilevitsky.weatherapi.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(String errorMessage) {}
