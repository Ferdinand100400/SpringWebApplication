package ru.chichkov.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class BusDto {
    private Long id;
    @NotBlank(message = "Номер автобуса не должен быть пустым")
    private String number;
    @NotNull(message = "Стоимость проезда не должна быть пустым")
    @PositiveOrZero(message = "Стоимость должна быть положительной или равно нулю")
    private Integer price;
    private List<CityDto> cities;

    public BusDto() {
    }
    public BusDto(Long id, String number, Integer price) {
        this.id = id;
        this.number = number;
        this.price = price;
    }

    public BusDto(Long id, String number, Integer price, List<CityDto> cities) {
        this.id = id;
        this.number = number;
        this.price = price;
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "BusDto{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", price=" + price +
                ", cities=" + cities +
                '}';
    }
}
