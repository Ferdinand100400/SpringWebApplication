package ru.chichkov.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.chichkov.dto.BusDto;
import ru.chichkov.dto.CityDto;
import ru.chichkov.exception.BusNotFoundException;
import ru.chichkov.exception.CityNotFoundException;
import ru.chichkov.exception.DuplicateCityException;
import ru.chichkov.exception.RemovingCityFindInRouteException;
import ru.chichkov.mapper.BusMapper;
import ru.chichkov.mapper.CityMapper;
import ru.chichkov.repository.BusRepo;
import ru.chichkov.repository.CityRepo;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CityService {
    private final CityRepo cityRepo;
    private final BusRepo busRepo;

    public Long addCity(CityDto cityDto) {
        return saveWithTryCatch(cityDto);
    }

    public Long removeCity(Long id) {
        if (!cityRepo.existsById(id))
            throw new CityNotFoundException(id);
        List<BusDto> busDtoList = BusMapper.ListEntityToListDto(busRepo.findAll());
        List<Long> busIdWithRemoveCity = new ArrayList<>();
        for (BusDto busDto : busDtoList) {
            CityDto cityDtoRemove = CityMapper.entityToDto(cityRepo.findById(id).get());
            if (busDto.getCities().contains(cityDtoRemove))
                busIdWithRemoveCity.add(busDto.getId());
        }
        if (busIdWithRemoveCity.isEmpty()) {
            cityRepo.deleteById(id);
            return id;
        } else
            throw new RemovingCityFindInRouteException(busIdWithRemoveCity);
    }

    public Long changeCity(Long id, CityDto cityDto) {
        if (!cityRepo.existsById(id))
            throw new CityNotFoundException(id);
        cityDto.setId(id);
        return saveWithTryCatch(cityDto);
    }

    public CityDto getCityByID(Long id) {
        if (!cityRepo.existsById(id)) {
            throw new CityNotFoundException(id);
        }
        return CityMapper.entityToDto(cityRepo.findById(id).get());
    }

    public List<CityDto> getAllCities(Long busId) {
        if (busId == null) {
            return CityMapper.ListEntityToListDto(cityRepo.findAll());
        }
        if (!busRepo.existsById(busId))
            throw new BusNotFoundException(busId);
        return CityMapper.ListEntityToListDto(cityRepo.findByBusesId(busId));
    }

    private Long saveWithTryCatch(CityDto cityDto) {
        try {
            Long id = cityRepo.save(CityMapper.dtoToEntity(cityDto)).getId();
            cityRepo.flush();
            return id;
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("Нарушение уникального индекса или первичного ключа"))
                throw new DuplicateCityException(cityDto.getName(), cityDto.getLongitude(), cityDto.getLatitude());
            throw e;
        }
    }
}
