package ru.chichkov.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class RemovingCityFindInRouteException extends RuntimeException {
    private final List<Long> busesId;

    public RemovingCityFindInRouteException(List<Long> busesId) {
        this.busesId = busesId;
    }
}
