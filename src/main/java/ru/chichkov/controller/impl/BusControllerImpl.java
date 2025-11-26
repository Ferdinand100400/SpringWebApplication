package ru.chichkov.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.chichkov.controller.BusController;
import ru.chichkov.dto.BusDto;
import ru.chichkov.dto.CityDto;
import ru.chichkov.service.BusService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BusControllerImpl implements BusController {

    private final BusService busService;
    @Override
    public Long addBus(BusDto busDto) {
        System.out.println("add Bus: " + busDto.toString());
        return busService.addBus(busDto);
    }

    @Override
    public Long removeBus(Long id) {
        System.out.println("delete Bus id: " + id);
        return busService.removeBus(id);
    }

    @Override
    public Long changeBus(Long id, BusDto busDto) {
        System.out.println("change Bus id: " + id + " on: " + busDto.toString());
        return busService.changeBus(id, busDto);
    }

    @Override
    public Long addRoute(Long busId, List<CityDto> cityDtoList) {
        System.out.println("add route " + cityDtoList + " for Bus id: " + busId);
        return busService.addRoute(busId, cityDtoList);
    }

    @Override
    public Long deleteRoute(Long busId, List<CityDto> cityDtoList) {
        System.out.println("delete route " + cityDtoList + " for Bus id: " + busId);
        return busService.deleteRoute(busId, cityDtoList);
    }

    @Override
    public BusDto getBusById(Long id) {
        System.out.println("get Bus by id: " + id);
        return busService.getBusById(id);
    }

    @Override
    public List<BusDto> getAllBuses(Long cityId) {
        System.out.println("get Bus all by City id: " + cityId);
        return busService.getAllBuses(cityId);
    }

    @Override
    public List<CityDto> getRouteByBusId(Long id) {
        System.out.println("get route by Bus id: " + id);
        return busService.getRouteByBusId(id);
    }

    @Override
    public List<CityDto> getRouteByBusNumber(String number) {
        System.out.println("get route by Bus number: " + number);
        return busService.getRouteByBusNumber(number);
    }
}
