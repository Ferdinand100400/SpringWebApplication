package ru.chichkov.exception;

import lombok.Getter;

@Getter
public class DuplicateBusException extends RuntimeException {
    private final String number;

    public DuplicateBusException(String number) {
        this.number = number;
    }
}
