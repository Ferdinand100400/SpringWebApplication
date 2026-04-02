package ru.chichkov.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chichkov.algorithm.FromCityResolver;
import ru.chichkov.algorithm.SegmentValidator;
import ru.chichkov.dto.BusDto;
import ru.chichkov.dto.CityDto;
import ru.chichkov.dto.route.RouteRequestDto;
import ru.chichkov.dto.route.RouteResponseDto;
import ru.chichkov.dto.route.SegmentRouteDto;
import ru.chichkov.entity.City;
import ru.chichkov.exception.CityByNameSeveralException;
import ru.chichkov.exception.CityNotFoundByNameException;
import ru.chichkov.exception.CityStartAndCityEndEqualsException;
import ru.chichkov.mapper.CityMapper;
import ru.chichkov.repository.CityRepo;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteService {
    private final CityRepo cityRepo;
    private final BusService busService;
    private final CityService cityService;

    public List<RouteResponseDto> findRoute(RouteRequestDto routeRequestDto) {
        CityDto startCityDto = findCityByName(routeRequestDto.getStartCity(), "START");
        CityDto endCityDto = findCityByName(routeRequestDto.getEndCity(), "END");
        if (startCityDto.equals(endCityDto)) throw new CityStartAndCityEndEqualsException(endCityDto.getName());

        List<RouteResponseDto> listRouteResponseDto = new ArrayList<>();
        List<SegmentRouteDto> route = new ArrayList<>(); // Список граней в минимальном графе
        Map<CityDto, Integer> nodes = new HashMap<>();  // Все вершины с их значением
        List<CityDto> markedCityDto = new ArrayList<>();
        int infinity = Integer.MAX_VALUE;
        // Инициализация всех вершин бесконечностью
        for (CityDto cityDto : cityService.getAllCities(null)) {
            nodes.put(cityDto, infinity);
        }
        nodes.put(startCityDto, 0);
        markedCityDto.add(startCityDto);

        // Пока не закрасили конечный город реализуем алгоритм Дейкстры
        while (!markedCityDto.contains(endCityDto))
            algorithmDijkstra(nodes, markedCityDto, routeRequestDto.getTypeFinder(), infinity, route);

        printListSegmentRouteDto(route); // вывод в консоль

        // В результате route содержит дерево кратчайших путей, получаем сам путь
        getMinimumWay(route);

        printListSegmentRouteDto(route); // вывод в консоль

        // Если получилось, что идут подряд ребра одно и того же автобуса, то объединяем в один
        unionSegmentRouteWithOneBus(route);

        listRouteResponseDto.add(createRoute(startCityDto, endCityDto, route));

        return sortedListRouteResponse(listRouteResponseDto, routeRequestDto.getTypeFinder());
    }

    // Алгоритм Дейкстры
    private void algorithmDijkstra(Map<CityDto, Integer> nodes, List<CityDto> markedCityDto, RouteRequestDto.TypeFinder typeFinder, int infinity, List<SegmentRouteDto> route) {
        // Определение закрашиваемого города в этой итерации, т.е. тот который является кратчайшим от предыдущего закрашенного города
        CityDto previousCityDto = markedCityDto.get(markedCityDto.size() - 1);
        List<SegmentRouteDto> segmentsRouteDto = new ArrayList<>();
        for (CityDto currrentCityDto : cityService.getAllCities(null)) {
            if (!markedCityDto.contains(currrentCityDto)) {
                Integer lengthShotWayForCurCity = nodes.get(currrentCityDto);
                // Прямой маршрут из предыдущего закрашенного города в текущей (с минимальным значением грани)
                SegmentRouteResult segmentRouteResult = findRouteSegment(typeFinder, previousCityDto, currrentCityDto, currrentCityDto, (next, prev, context) -> {
                            CityDto target = (CityDto) context;
                            return next.equals(target) || prev.equals(target);
                        },
                        (next, prev, context) -> previousCityDto);
                SegmentRouteDto segmentRouteDto = segmentRouteResult.segmentRouteDto;
                int edgeValue = segmentRouteResult.edgeValue;
                if (segmentRouteDto != null) segmentsRouteDto.add(segmentRouteDto);

                // Если пути нет, значит грань равна бесконечности
                if (edgeValue == -1) edgeValue = infinity;
                // Нахождение минимального кратчайшего пути (текущий или новый) и обновление вершины
                int lengthShotWayForEdge = infinity;
                if (nodes.get(previousCityDto) != infinity && edgeValue != infinity)
                    lengthShotWayForEdge = nodes.get(previousCityDto) + edgeValue;
                lengthShotWayForCurCity = Math.min(lengthShotWayForCurCity, lengthShotWayForEdge);
                nodes.put(currrentCityDto, lengthShotWayForCurCity);
            }
        }

        // Если не нашлись сегменты, значит нет маршрутов в не закрашенные города, поэтому это тупиковый граф -> меняем местами закрашенные города, возвращаемся назад на один город
        if (segmentsRouteDto.isEmpty()) {
            int size = markedCityDto.size();
            CityDto tmpCityDto = markedCityDto.get(size - 1);
            markedCityDto.set(size - 1, markedCityDto.get(size - 2));
            markedCityDto.set(size - 2, tmpCityDto);
            return;
        }

        // Добавление закрашиваемого города как с минимальным значением и добавление ребра к этому городу
        CityDto newMarkedCity = detectionNewMarkedCity(nodes, markedCityDto);
        markedCityDto.add(newMarkedCity);
        addEdgeToRoute(route, markedCityDto, segmentsRouteDto, typeFinder);

        printListSegmentRouteDto(route); // вывод в консоль
    }

    // Прямой маршрут из предыдущего закрашенного города в текущей (с минимальным значением грани)
    private SegmentRouteResult findRouteSegment(RouteRequestDto.TypeFinder typeFinder, CityDto previousCityDto, CityDto currrentCityDto, Object context, SegmentValidator validator, FromCityResolver fromCityResolver) {
        SegmentRouteDto segmentRouteDto = null;
        int edgeValue = -1;
        for (BusDto busDto : busService.getAllBuses(previousCityDto.getId())) {
            int priceSegment = busDto.getPrice() / (busDto.getCities().size() - 1);  // стоимость ребра (общая стоимость делится на кол-во городов в маршруте)
            int timeSegment = 0; // Пока функционал не заведен
            int paramSegmentLikeValueEdge;
            if (typeFinder == RouteRequestDto.TypeFinder.LESS_PRICE) {
                paramSegmentLikeValueEdge = priceSegment;
            } else if (typeFinder == RouteRequestDto.TypeFinder.FASTEST) paramSegmentLikeValueEdge = timeSegment;
            else paramSegmentLikeValueEdge = 0; // Для кол-ва пересадок тоже пока функционал не заведен
            int idxPrevCity = busDto.getCities().indexOf(previousCityDto);
            int idxCurCity1 = idxPrevCity + 1;
            int idxCurCity2 = idxPrevCity - 1;
            if (idxCurCity1 > busDto.getCities().size() - 1) idxCurCity1 = idxPrevCity;
            if (idxCurCity2 < 0) idxCurCity2 = idxPrevCity;

            // Если (в зависимости от валидации (следующая или предыдущая точка это текущий город) vs (в списке закрашенных городов найден следующий или предыдущий относительно последнего закрашиваемого)) и параметр по чему ищем (стоимость, время) меньше, то обновляем стоимость и сегмент маршрута
            if (validator.isValid(busDto.getCities().get(idxCurCity1), busDto.getCities().get(idxCurCity2), context) && (edgeValue > paramSegmentLikeValueEdge || edgeValue == -1)) {
                CityDto fromCity = fromCityResolver.resolve(busDto.getCities().get(idxCurCity1), busDto.getCities().get(idxCurCity2), context);
                edgeValue = paramSegmentLikeValueEdge;
                segmentRouteDto = new SegmentRouteDto(busDto, fromCity, currrentCityDto, priceSegment, timeSegment);
            }
        }
        return new SegmentRouteResult(segmentRouteDto, edgeValue);
    }

    // Закрашивание города с минимальным значением
    private CityDto detectionNewMarkedCity(Map<CityDto, Integer> nodes, List<CityDto> markedCityDto) {
        return nodes.entrySet()
                .stream()
                .filter(x -> !markedCityDto.contains(x.getKey()))
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .get();
    }

    // Добавление ребра к минимальному графу
    private void addEdgeToRoute(List<SegmentRouteDto> minimumGraph, List<CityDto> markedCityDto, List<SegmentRouteDto> segmentsRouteDto, RouteRequestDto.TypeFinder typeFinder) {
        CityDto newMarkedCity = markedCityDto.get(markedCityDto.size() - 1);
        try {
            minimumGraph.add(segmentsRouteDto.stream()
                    .filter(x -> x.getToCity().equals(newMarkedCity))
                    .findFirst()
                    .get());
        } catch (NoSuchElementException e) {
            SegmentRouteDto segmentRouteDto = findRouteSegment(typeFinder, newMarkedCity, newMarkedCity, markedCityDto, (next, prev, context) -> {
                        List<CityDto> target = (List<CityDto>) context;
                        return target.contains(next) || target.contains(prev);
                    },
                    (next, prev, context) -> {
                        List<CityDto> markedCities = (List<CityDto>) context;
                        if (markedCities.contains(prev)) return prev;
                        if (markedCities.contains(next)) return next;
                        return null;
                    }).segmentRouteDto;
            if (segmentRouteDto != null) minimumGraph.add(segmentRouteDto);
        }
    }

    // Получение из минимального графа итогового минимального пути
    private void getMinimumWay(List<SegmentRouteDto> route) {
        if (route.size() > 1) {
            int routeSize = route.size();
            for (int i = routeSize - 2; i >= 0; i--) {
                if (!route.get(i).getToCity().equals(route.get(i + 1).getFromCity())) route.remove(i);
            }
        }
    }

    // Если получилось, что идут подряд ребра одно и того же автобуса, то объединяем в один
    private void unionSegmentRouteWithOneBus(List<SegmentRouteDto> route) {
        for (int i = 0; i < route.size() - 1; i++) {
            SegmentRouteDto curSegmentRouteDto = route.get(i);
            SegmentRouteDto nextSegmentRouteDto = route.get(i + 1);
            if (curSegmentRouteDto.getBusDto().equals(nextSegmentRouteDto.getBusDto())) {
                SegmentRouteDto newSegmentRouteDto = new SegmentRouteDto(curSegmentRouteDto.getBusDto(), curSegmentRouteDto.getFromCity(), nextSegmentRouteDto.getToCity(), curSegmentRouteDto.getPrice() + nextSegmentRouteDto.getPrice(), 0);
                route.set(i, newSegmentRouteDto);
                route.remove(i + 1);
                i--;
            }
        }
    }

    private RouteResponseDto createRoute(CityDto fromCity, CityDto toCity, List<SegmentRouteDto> route) {
        Integer price = route.stream()
                .mapToInt(SegmentRouteDto::getPrice)
                .sum();
        Integer time = route.stream()
                .mapToInt(SegmentRouteDto::getTime)
                .sum();
        return new RouteResponseDto(fromCity.getName(), toCity.getName(), route, route.size() - 1, price, time);
    }

    private CityDto findCityByName(String nameCity, String message) {
        List<City> lstCity = cityRepo.findByName(nameCity);
        if (lstCity.isEmpty()) {
            throw new CityNotFoundByNameException(nameCity, message);   // город не найден
        }
        if (lstCity.size() == 1) {
            return CityMapper.entityToDto(lstCity.get(0));
        }
        // найдено несколько городов
        throw new CityByNameSeveralException(lstCity.stream()
                .map(CityMapper::entityToDto)
                .collect(Collectors.toList()));
    }

    private List<RouteResponseDto> sortedListRouteResponse(List<RouteResponseDto> listRouteResponseDto, RouteRequestDto.TypeFinder typeFinder) {
        if (typeFinder == RouteRequestDto.TypeFinder.FASTEST) return listRouteResponseDto.stream()
                .sorted(Comparator.comparing(RouteResponseDto::getGeneralTime))
                .collect(Collectors.toList());
        if (typeFinder == RouteRequestDto.TypeFinder.LESS_PRICE)
            return listRouteResponseDto.stream()
                    .sorted(Comparator.comparing(RouteResponseDto::getGeneralPrice))
                    .collect(Collectors.toList());
        if (typeFinder == RouteRequestDto.TypeFinder.MIN_TRANSFERS)
            return listRouteResponseDto.stream()
                    .sorted(Comparator.comparing(RouteResponseDto::getTransfersCount))
                    .collect(Collectors.toList());
        return listRouteResponseDto;
    }

    private void printListSegmentRouteDto(List<SegmentRouteDto> route) {
        System.out.println("route:\n");
        for (SegmentRouteDto r : route) {
            if (r != null)
                System.out.println(r + "\n");
            else System.out.println("null\n\n");
        }
    }

    private record SegmentRouteResult(SegmentRouteDto segmentRouteDto, int edgeValue) {
    }
}
