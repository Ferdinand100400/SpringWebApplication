package ru.chichkov.mapper;

import ru.chichkov.dto.CityDto;
import ru.chichkov.entity.City;

import java.util.ArrayList;
import java.util.List;

public class CityMapper {
    public static City dtoToEntity(CityDto cityDto){
        return new City(cityDto.getId(), cityDto.getName(), cityDto.getLongitude(), cityDto.getLatitude());
    }
    public static CityDto entityToDto(City entity){
        return new CityDto(entity.getId(), entity.getName(), entity.getLongitude(), entity.getLatitude());
    }

    public static List<CityDto> ListEntityToListDto(List<City> entity) {
        List<CityDto> lstCityDto = new ArrayList<>();
        for (City city : entity) {
            lstCityDto.add(CityMapper.entityToDto(city));
        }
        return lstCityDto;
    }

    public static List<City> ListDtoToListEntity(List<CityDto> cityDtoList) {
        List<City> lstCity = new ArrayList<>();
        for (CityDto cityDto : cityDtoList) {
            lstCity.add(CityMapper.dtoToEntity(cityDto));
        }
        return lstCity;
    }
}
