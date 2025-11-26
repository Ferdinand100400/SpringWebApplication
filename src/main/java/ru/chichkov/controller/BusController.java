package ru.chichkov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.chichkov.dto.BusDto;
import ru.chichkov.dto.CityDto;

import java.util.List;
@RestController
@RequestMapping("/bus")
public interface BusController {
    @PostMapping
    @Operation(
            summary = "Добавление нового автобуса",
            description = "Добавляет автобус в БД по переданным данным: можно добавить с маршрутом (список городов по id) или без него"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Автобус успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации — номер или стоимость не заданы"),
            @ApiResponse(responseCode = "404", description = "Автобус с указанным номером уже существует или номер длинный или города с указанным id нет"),
    })
    Long addBus(@RequestBody @Valid BusDto busDto);

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление автобуса",
            description = "Удаляет автобус из БД по переданному id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Автобус успешно удален"),
            @ApiResponse(responseCode = "404", description = "Автобуса с указанным id нет"),
    })
    Long removeBus(@PathVariable("id") Long id);

    @PutMapping("/{id}")
    @Operation(
            summary = "Изменение автобуса по id",
            description = "Изменяет существующий автобус в БД по id на переданные данные (маршрут не изменяет)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Автобус успешно изменен"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации — номер или стоимость не заданы"),
            @ApiResponse(responseCode = "404", description = "Автобус с указанным номером уже существует или номер длинный или города с указанным id нет"),
    })
    Long changeBus(@PathVariable("id") Long id, @RequestBody @Valid BusDto busDto);

    @PutMapping("/addRoute/{busId}")
    @Operation(
            summary = "Добавление маршрута автобуса с переданным id",
            description = "Добавлет к существуещему автобусу с id список городов (маршрут - города по id)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Маршрут успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации для добавляемого города — имя или координаты не заданы или указанные координаты выходят за допустимый диапазон"),
            @ApiResponse(responseCode = "404", description = "Автобуса с указанным id нет"),
    })
    Long addRoute(@PathVariable("busId") Long busId, @RequestBody List<@Valid CityDto> cityDtoList);

    @PutMapping("/delRoute/{busId}")
    @Operation(
            summary = "Удаление маршрута автобуса с переданным id",
            description = "Удаляет из существуещего автобуса с id список городов (маршрут)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Маршрут успешно удален"),
            @ApiResponse(responseCode = "404", description = "Автобуса с указанным id нет"),
    })
    Long deleteRoute(@PathVariable("busId") Long busId, @RequestBody List<@Valid CityDto> cityDtoList);

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить автобус по id",
            description = "Получаем автобус из БД по id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Автобус успешно получен"),
            @ApiResponse(responseCode = "404", description = "Автобуса с указанным id нет"),
    })
    BusDto getBusById(@PathVariable("id") Long id);

    @GetMapping
    @Operation(
            summary = "Получить все автобусы или по городу",
            description = "Получаем список всех автобусов из БД или список всех автобусов в городе по указанному id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Автобусы успешно получены"),
            @ApiResponse(responseCode = "404", description = "Города с указанным id нет"),
    })
    List<BusDto> getAllBuses(@RequestParam(value = "CityId", required = false) Long cityId);

    @GetMapping("/route/{id}")
    @Operation(
            summary = "Получить маршрут автобуса по id",
            description = "Получаем список городов, которые проезжает автобус по указанному id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Маршрут успешно получен"),
            @ApiResponse(responseCode = "404", description = "Автобуса с указанным id нет"),
    })
    List<CityDto> getRouteByBusId(@PathVariable("id") Long id);

    @GetMapping("/route")
    @Operation(
            summary = "Получить маршрут автобуса по номеру автобуса",
            description = "Получаем список городов, которые проезжает автобус по указанному номеру автобуса"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Маршрут успешно получен"),
            @ApiResponse(responseCode = "404", description = "Автобуса с указанным номером нет"),
    })
    List<CityDto> getRouteByBusNumber(@RequestParam(value = "number") String number);
}
