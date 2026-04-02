package ru.chichkov.exception;

import lombok.Getter;

@Getter
public class CityStartAndCityEndEqualsException extends RuntimeException {
    private final String cityName;

    public CityStartAndCityEndEqualsException(String cityName) {
        this.cityName = cityName;
    }
}
