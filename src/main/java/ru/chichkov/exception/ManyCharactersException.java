package ru.chichkov.exception;

import lombok.Getter;

@Getter
public class ManyCharactersException extends RuntimeException {
    private final int maxCount;
    private final String value;

    public ManyCharactersException(int maxCount, String value) {
        this.maxCount = maxCount;
        this.value = value;
    }
}
