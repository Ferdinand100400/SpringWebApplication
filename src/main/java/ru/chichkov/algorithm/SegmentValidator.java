package ru.chichkov.algorithm;

import ru.chichkov.dto.CityDto;

@FunctionalInterface
public interface SegmentValidator {
    boolean isValid(CityDto nextCity, CityDto prevCity, Object context);
}
