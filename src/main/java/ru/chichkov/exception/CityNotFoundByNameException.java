package ru.chichkov.exception;

import lombok.Getter;

@Getter
public class CityNotFoundByNameException extends RuntimeException {
    private final String name;
    private final String message;

    public CityNotFoundByNameException(String name, String message) {
        this.name = name;
        this.message = message;
    }
}
