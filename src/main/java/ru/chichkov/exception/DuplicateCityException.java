package ru.chichkov.exception;

import lombok.Getter;

@Getter
public class DuplicateCityException extends RuntimeException {
    private final String name;
    private final Double longitude;
    private final Double latitude;

    public DuplicateCityException(String name, Double longitude, Double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
