package ru.chichkov.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.chichkov.controller.RouteController;
import ru.chichkov.dto.route.RouteRequestDto;
import ru.chichkov.dto.route.RouteResponseDto;
import ru.chichkov.service.RouteService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RouteControllerImpl implements RouteController {
    private final RouteService routeService;

    @Override
    public List<RouteResponseDto> findRoute(RouteRequestDto routeRequestDto) {
        System.out.println("Find route");
        return routeService.findRoute(routeRequestDto);
    }
}
