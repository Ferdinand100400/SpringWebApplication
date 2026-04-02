package ru.chichkov.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Data
public class CityDto {
    private Long id;
    @NotBlank(message = "название города должно быть не пустым")
    private String name;
    @NotNull(message = "долгота города должна быть не пустым")
    @DecimalMin(value = "-180.0", message = "долгота должна быть не менее -180")
    @DecimalMax(value = "180.0", message = "долгота должна быть не более 180")
    private Double longitude;
    @NotNull(message = "широта города должна быть не пустым")
    @DecimalMin(value = "-90.0", message = "широта должна быть не менее -90")
    @DecimalMax(value = "90.0", message = "широта должна быть не более 90")
    private Double latitude;

    public CityDto() {
    }

    public CityDto(Long id, String name, Double longitude, Double latitude) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "CityDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CityDto cityDto = (CityDto) o;

        if (!Objects.equals(id, cityDto.id)) return false;
        if (!Objects.equals(name, cityDto.name)) return false;
        if (!Objects.equals(longitude, cityDto.longitude)) return false;
        return Objects.equals(latitude, cityDto.latitude);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        return result;
    }
}
