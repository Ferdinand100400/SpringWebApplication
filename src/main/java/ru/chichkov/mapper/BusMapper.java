package ru.chichkov.mapper;

import ru.chichkov.dto.BusDto;
import ru.chichkov.entity.Bus;

import java.util.ArrayList;
import java.util.List;

public class BusMapper {
    public static Bus dtoToEntity(BusDto busDto){
        if (busDto.getCities() == null) return new Bus(busDto.getId(), busDto.getNumber(), busDto.getPrice());
        return new Bus(busDto.getId(), busDto.getNumber(), busDto.getPrice(), CityMapper.ListDtoToListEntity(busDto.getCities()));
    }
    public static BusDto entityToDto(Bus entity){
        if (entity.getCities() == null) return new BusDto(entity.getId(), entity.getNumber(), entity.getPrice());
        return new BusDto(entity.getId(), entity.getNumber(), entity.getPrice(), CityMapper.ListEntityToListDto(entity.getCities()));
    }

    public static List<BusDto> ListEntityToListDto(List<Bus> entity) {
        List<BusDto> lstBusDto = new ArrayList<>();
        for (Bus bus : entity) {
            lstBusDto.add(BusMapper.entityToDto(bus));
        }
        return lstBusDto;
    }
}
