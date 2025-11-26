package ru.chichkov.exception;

import lombok.Getter;

@Getter
public class CityNotFoundInRouteException extends RuntimeException {
    private final Long id;

    public CityNotFoundInRouteException(Long id) {
        this.id = id;
    }
}
