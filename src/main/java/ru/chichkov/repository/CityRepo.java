package ru.chichkov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.chichkov.entity.City;

import java.util.List;

@Repository
public interface CityRepo extends CrudRepository<City, Long>, JpaRepository<City, Long> {
    List<City> findByBusesId(Long busId);
}
