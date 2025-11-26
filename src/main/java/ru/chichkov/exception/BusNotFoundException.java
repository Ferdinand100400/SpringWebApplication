package ru.chichkov.exception;

import lombok.Getter;

@Getter
public class BusNotFoundException extends RuntimeException {
    private final Long id;

    public BusNotFoundException(Long id) {
        this.id = id;
    }
}
