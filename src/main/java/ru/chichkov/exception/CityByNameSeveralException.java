package ru.chichkov.exception;

import lombok.Getter;
import ru.chichkov.dto.CityDto;

import java.util.List;

@Getter
public class CityByNameSeveralException extends RuntimeException {
    private final List<CityDto> cities;

    public CityByNameSeveralException(List<CityDto> cities) {
        this.cities = cities;
    }
}
