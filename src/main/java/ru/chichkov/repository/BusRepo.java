package ru.chichkov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.chichkov.entity.Bus;
import ru.chichkov.entity.City;

import java.util.List;

@Repository
public interface BusRepo extends CrudRepository<Bus, Long>, JpaRepository<Bus, Long> {

    List<Bus> findByCitiesId(Long cityId);
    @Query("SELECT c FROM City c JOIN c.buses b WHERE b.id = :busId")
    List<City> findCitiesByBusId(@Param("busId") Long busId);

    @Query("SELECT b.id FROM Bus b WHERE b.number = :number")
    Long findIdByNumber(@Param("number") String number);
}
