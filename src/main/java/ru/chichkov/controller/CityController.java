package ru.chichkov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.chichkov.dto.CityDto;

import java.util.List;

@RestController
@RequestMapping("/city")
public interface CityController {

    @PostMapping
    @Operation(
            summary = "Добавление нового города",
            description = "Добавляет город в БД по переданным данным"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Город успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации — имя или координаты не заданы или указанные ширина, высота выходят за допустимый диапазон"),
            @ApiResponse(responseCode = "404", description = "Город уже существует"),
    })
    Long addCity(@RequestBody @Valid CityDto cityDto);

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление города",
            description = "Удаляет город из БД по переданному id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Город успешно удален"),
            @ApiResponse(responseCode = "404", description = "Города с указанным id нет"),
    })
    Long removeCity(@PathVariable("id") Long id);

    @PutMapping("/{id}")
    @Operation(
            summary = "Изменение города по id",
            description = "Изменяет существующий город в БД по id на переданные данные"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Город успешно изменен"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации — имя или координаты не заданы или указанные координаты выходят за допустимый диапазон"),
            @ApiResponse(responseCode = "404", description = "Города с указанным id нет или город уже существует"),
    })
    Long changeCity(@PathVariable("id") Long id, @RequestBody @Valid CityDto cityDto);

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить город по id",
            description = "Получаем город из БД по id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Город успешно получен"),
            @ApiResponse(responseCode = "404", description = "Города с указанным id нет"),
    })
    CityDto getCityById(@PathVariable("id") Long id);

    @GetMapping
    @Operation(
            summary = "Получить все города или по автобусу",
            description = "Получаем список всех городов из БД или список всех городом автобуса по указанному id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Города успешно получены"),
            @ApiResponse(responseCode = "404", description = "Автобуса с указанным id нет"),
    })
    List<CityDto> getAllCities(@RequestParam(value = "busId", required = false) Long busId);

}
