package ru.chichkov.exception;

import lombok.Getter;

@Getter
public class CityNotFoundException extends RuntimeException {
    private final Long id;

    public CityNotFoundException(Long id) {
        this.id = id;
    }
}
