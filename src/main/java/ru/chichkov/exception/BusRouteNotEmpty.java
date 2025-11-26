package ru.chichkov.exception;

import lombok.Getter;

@Getter
public class BusRouteNotEmpty extends RuntimeException {
    private final Long id;

    public BusRouteNotEmpty(Long id) {
        this.id = id;
    }
}
