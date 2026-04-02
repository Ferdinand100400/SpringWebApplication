package ru.chichkov.dto.route;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.chichkov.dto.BusDto;
import ru.chichkov.dto.CityDto;

import java.util.Objects;

@Data
@Getter
@Setter
public class SegmentRouteDto {

    private BusDto busDto;
    private CityDto fromCity;
    private CityDto toCity;
    private Integer price;
    private Integer time;

    public SegmentRouteDto() {}

    public SegmentRouteDto(BusDto busDto, CityDto fromCity, CityDto toCity, Integer price, Integer time) {
        this.busDto = busDto;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.price = price;
        this.time = time;
    }

    @Override
    public String toString() {
        return "  SegmentRouteDto{\n" +
                "    busDto=" + busDto +
                "\n    fromCity=" + fromCity +
                "\n    toCity=" + toCity +
                "\n    price=" + price +
                "\n    time=" + time +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SegmentRouteDto that = (SegmentRouteDto) o;

        if (!Objects.equals(busDto, that.busDto)) return false;
        if (!Objects.equals(fromCity, that.fromCity)) return false;
        if (!Objects.equals(toCity, that.toCity)) return false;
        if (!Objects.equals(price, that.price)) return false;
        return Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        int result = busDto != null ? busDto.hashCode() : 0;
        result = 31 * result + (fromCity != null ? fromCity.hashCode() : 0);
        result = 31 * result + (toCity != null ? toCity.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    @Override
    public SegmentRouteDto clone() {
        try {
            return (SegmentRouteDto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException();
        }
    }

}
