package ru.chichkov.dto.route;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class RouteResponseDto {
    private String startCityName;
    private String endCityName;
    private List<SegmentRouteDto> route;
    private Integer transfersCount;
    private Integer generalPrice;
    private Integer generalTime;

    public RouteResponseDto() {
    }

    public RouteResponseDto(String startCityName, String endCityName, List<SegmentRouteDto> route, Integer transfersCount, Integer generalPrice, Integer generalTime) {
        this.startCityName = startCityName;
        this.endCityName = endCityName;
        this.route = route;
        this.transfersCount = transfersCount;
        this.generalPrice = generalPrice;
        this.generalTime = generalTime;
    }

    @Override
    public String toString() {
        return "RouteResponseDto{\n" +
                "\n  startCityName='" + startCityName +
                "\n  endCityName='" + endCityName +
                "\n  route=\n" + route +
                "\n  transfersCount=" + transfersCount +
                "\n  generalPrice=" + generalPrice +
                "\n  generalTime=" + generalTime +
                "}\n";
    }
}
