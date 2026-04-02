package ru.chichkov.dto.route;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RouteRequestDto {

    private String startCity;
    private String endCity;
    private TypeFinder typeFinder;

    public RouteRequestDto(String startCity, String endCityId, TypeFinder typeFinder) {
        this.startCity = startCity;
        this.endCity = endCityId;
        this.typeFinder = typeFinder;
    }

    public RouteRequestDto() {}

    @Override
    public String toString() {
        return "RouteRequestDto{\n" +
                "  startCity='" + startCity +
                "\n  , endCityId='" + endCity +
                "\n  , typeFinder=" + typeFinder +
                "}\n";
    }

    public enum TypeFinder {
        FASTEST,
        LESS_PRICE,
        MIN_TRANSFERS
    }
}
