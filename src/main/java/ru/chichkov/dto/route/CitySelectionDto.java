package ru.chichkov.dto.route;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.chichkov.dto.CityDto;

import java.util.List;

@Data
@Getter
@Setter
public class CitySelectionDto {

    private List<CityDto> cities;
    private String message;

    public CitySelectionDto() {
    }
    public CitySelectionDto(List<CityDto> cities, String message) {
        this.cities = cities;
        this.message = message;
    }

    @Override
    public String toString() {
        return "CitySelectionDto{" +
                "cities=" + cities +
                ", message='" + message + '\'' +
                '}';
    }
}
