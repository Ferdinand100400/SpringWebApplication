package ru.chichkov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.chichkov.dto.route.RouteRequestDto;
import ru.chichkov.dto.route.RouteResponseDto;

import java.util.List;

/*
1) поиск маршрутов по начальному, конечному городу и типом поиска передаваемым в запрос и сортировка по убыванию
2) вывести маршрут данного автобуса
*/
@RestController
@RequestMapping("/route")
public interface RouteController {

    @PostMapping
    @Operation(
            summary = "Поиск маршрутов",
            description = "Поиск маршрутов по начальному, конечному городу и типом поиска передаваемым в запрос и сортировка по убыванию"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Маршруты получены"),
            //   @ApiResponse(responseCode = "400", description = "Ошибка валидации — "),
            @ApiResponse(responseCode = "404", description = "Конечный или начальный город не найден"),
    })
    List<RouteResponseDto> findRoute(@RequestBody @Valid RouteRequestDto routeRequestDto);
}
