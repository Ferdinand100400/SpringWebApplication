package ru.chichkov.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.chichkov.dto.BusDto;
import ru.chichkov.dto.CityDto;
import ru.chichkov.exception.*;
import ru.chichkov.mapper.BusMapper;
import ru.chichkov.mapper.CityMapper;
import ru.chichkov.repository.BusRepo;
import ru.chichkov.repository.CityRepo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BusService {
    private final CityRepo cityRepo;
    private final BusRepo busRepo;

    public Long addBus(BusDto busDto) {
        if (busDto.getCities() == null)
            return saveWithTryCatch(busDto);
        List<CityDto> cityDtoList = addCities(busDto.getCities().iterator(), new ArrayList<>());
        BusDto busDtoRes = new BusDto(busDto.getId(), busDto.getNumber(), busDto.getPrice(), cityDtoList);
        return saveWithTryCatch(busDtoRes);
    }

    public Long removeBus(Long id) {
        if (!busRepo.existsById(id)) throw new BusNotFoundException(id);
        busRepo.deleteById(id);
        return id;
    }

    public Long changeBus(Long id, BusDto busDto) {
        if (!busRepo.existsById(id)) throw new BusNotFoundException(id);
        if (busDto.getCities() != null) throw new BusRouteNotEmpty(id);
        busDto.setId(id);
        List<CityDto> cities = CityMapper.ListEntityToListDto(busRepo.findById(id).get().getCities());
        busDto.setCities(cities);
        return saveWithTryCatch(busDto);
    }

    public Long addRoute(Long busId, List<CityDto> cityDtoList) {
        if (!busRepo.existsById(busId)) throw new BusNotFoundException(busId);
        BusDto busDto = BusMapper.entityToDto(busRepo.findById(busId).get());
        busDto.setCities(addCities(cityDtoList.iterator(), busDto.getCities()));
        return busRepo.save(BusMapper.dtoToEntity(busDto)).getId();
    }

    public Long deleteRoute(Long busId, List<CityDto> cityDtoList) {
        if (!busRepo.existsById(busId)) throw new BusNotFoundException(busId);
        BusDto busDto = BusMapper.entityToDto(busRepo.findById(busId).get());
        busDto.setCities(deleteCities(cityDtoList.iterator(), busDto.getCities()));
        busRepo.save(BusMapper.dtoToEntity(busDto));
        return busId;
    }

    public BusDto getBusById(Long id) {
        if (!busRepo.existsById(id)) throw new BusNotFoundException(id);
        return BusMapper.entityToDto(busRepo.findById(id).get());
    }

    public List<BusDto> getAllBuses(Long cityId) {
        if (cityId == null)
            return BusMapper.ListEntityToListDto(busRepo.findAll());
        if (!cityRepo.existsById(cityId)) throw new CityNotFoundException(cityId);
        return BusMapper.ListEntityToListDto(busRepo.findByCitiesId(cityId));
    }

    public List<CityDto> getRouteByBusId(Long id) {
        if (id == null || !busRepo.existsById(id)) throw new BusNotFoundException(id);
        return CityMapper.ListEntityToListDto(busRepo.findCitiesByBusId(id));
    }

    public List<CityDto> getRouteByBusNumber(String number) {
        Long id = busRepo.findIdByNumber(number);
        return getRouteByBusId(id);
    }

    private List<CityDto> addCities(Iterator<CityDto> iterator, List<CityDto> cityDtoList) {
        while(iterator.hasNext()) {
            Long cityId = iterator.next().getId();
            if (cityId == null || !cityRepo.existsById(cityId)) throw new CityNotFoundException(cityId);
            CityDto cityDto = CityMapper.entityToDto(cityRepo.findById(cityId).get());
            cityDtoList.add(cityDto);
        }
        return cityDtoList;
    }

    private List<CityDto> deleteCities(Iterator<CityDto> iterator, List<CityDto> cityDtoList) {
        while(iterator.hasNext()) {
            Long cityId = iterator.next().getId();
            if (cityId == null || !cityRepo.existsById(cityId)) throw new CityNotFoundException(cityId);
            CityDto cityDto = CityMapper.entityToDto(cityRepo.findById(cityId).get());
            if (!cityDtoList.contains(cityDto)) throw new CityNotFoundInRouteException(cityId);
            cityDtoList.remove(cityDto);
        }
        return cityDtoList;
    }

    private Long saveWithTryCatch(BusDto busDto) {
        try {
            Long id = busRepo.save(BusMapper.dtoToEntity(busDto)).getId();
            busRepo.flush();
            return id;
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("Нарушение уникального индекса или первичного ключа"))
                throw new DuplicateBusException(busDto.getNumber());
            if (e.getMessage().contains("Значение слишком длинное для поля \"NUMBER"))
                throw new ManyCharactersException(5, busDto.getNumber());
            throw e;
        }
    }
}
