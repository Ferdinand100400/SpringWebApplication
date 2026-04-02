package ru.chichkov.controller.impl;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.chichkov.dto.route.CitySelectionDto;
import ru.chichkov.exception.*;

import java.awt.*;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handle(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(400).body(exception.toString());
    }
    @ExceptionHandler(BusNotFoundException.class)
    public ResponseEntity<String> handle(BusNotFoundException exception) {
        return ResponseEntity.status(404)
                .body("не найден автобус c id " + exception.getId());
    }
    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<String> handle(CityNotFoundException exception) {
        return ResponseEntity.status(404)
                .body("не найден город c id " + exception.getId());
    }

    @ExceptionHandler(BusRouteNotEmpty.class)
    public ResponseEntity<String> handle(BusRouteNotEmpty exception) {
        return ResponseEntity.status(404)
                .body("Не должно быть маршрута у автобуса id " + exception.getId());
    }

    @ExceptionHandler(CityNotFoundInRouteException.class)
    public ResponseEntity<String> handle(CityNotFoundInRouteException exception) {
        return ResponseEntity.status(404)
                .body("В маршруте автобуса не найден город c id " + exception.getId());
    }

    @ExceptionHandler(DuplicateCityException.class)
    public ResponseEntity<String> handle(DuplicateCityException exception) {
        return ResponseEntity.status(404)
                .body("Город с координатами: " + exception.getLatitude() + "/" + exception.getLongitude() + " уже существует: Невозможно добавить город \"" + exception.getName() + "\"");
    }

    @ExceptionHandler(DuplicateBusException.class)
    public ResponseEntity<String> handle(DuplicateBusException exception) {
        return ResponseEntity.status(404)
                .body("Автобус с номером: \"" + exception.getNumber() + "\" уже существует!");
    }

    @ExceptionHandler(ManyCharactersException.class)
    public ResponseEntity<String> handle(ManyCharactersException exception) {
        return ResponseEntity.status(404)
                .body("Автобус номер \"" + exception.getValue() + "\" не может быть добавлен, номер не должен быть длинее " + exception.getMaxCount() + " символов");
    }

    @ExceptionHandler(RemovingCityFindInRouteException.class)
    public ResponseEntity<String> handle(RemovingCityFindInRouteException exception) {
        return ResponseEntity.status(404)
                .body("Невозможно удалить город, город есть в маршрутах автобусов с id: " + exception.getBusesId());
    }

    @ExceptionHandler(CityNotFoundByNameException.class)
    public ResponseEntity<String> handle(CityNotFoundByNameException exception) {
        String message = exception.getMessage().equals("START") ? "Начальный" : "Конечный";
        return ResponseEntity.status(404)
                .body(message + " город c названием " + exception.getName() + " не найден");
    }

    @ExceptionHandler(CityByNameSeveralException.class)
    public ResponseEntity<?> handle(CityByNameSeveralException exception) {
        CitySelectionDto citySelectionDto = new CitySelectionDto(exception.getCities(), "Найдено несколько городов, выберите нужный");
        return ResponseEntity.status(HttpStatus.MULTIPLE_CHOICES)
                .body(citySelectionDto);
    }

    @ExceptionHandler(CityStartAndCityEndEqualsException.class)
    public ResponseEntity<String> handle(CityStartAndCityEndEqualsException exception) {
        return ResponseEntity.status(404)
                .body("Вы уже находитесь в городе " + exception.getCityName());
    }
}