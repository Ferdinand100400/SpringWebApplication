package ru.chichkov.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.chichkov.controller.CityController;
import ru.chichkov.dto.CityDto;
import ru.chichkov.service.CityService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CityControllerImpl implements CityController {

    private final CityService cityService;
    @Override
    public Long addCity(CityDto cityDto) {
        System.out.println("add City: " + cityDto.toString());
        return cityService.addCity(cityDto);
    }

    @Override
    public Long removeCity(Long id) {
        System.out.println("delete City id: " + id);
        return cityService.removeCity(id);
    }

    @Override
    public Long changeCity(Long id, CityDto cityDto) {
        System.out.println("change City id: " + id + " on: " + cityDto.toString());
        return cityService.changeCity(id, cityDto);
    }

    @Override
    public CityDto getCityById(Long id) {
        System.out.println("get City by id: " + id);
        return cityService.getCityByID(id);
    }

    @Override
    public List<CityDto> getAllCities(Long busId) {
        System.out.println("get City all by bus id: " + busId);
        return cityService.getAllCities(busId);
    }
}
