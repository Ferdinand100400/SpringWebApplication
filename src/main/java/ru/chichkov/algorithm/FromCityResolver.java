package ru.chichkov.algorithm;

import ru.chichkov.dto.CityDto;

@FunctionalInterface
public interface FromCityResolver {

    CityDto resolve(CityDto nextCity, CityDto prevCity, Object context);
}
