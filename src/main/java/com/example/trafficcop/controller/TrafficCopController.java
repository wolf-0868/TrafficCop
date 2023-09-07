package com.example.trafficcop.controller;

import com.example.trafficcop.data.CarNumber;
import com.example.trafficcop.services.CarNumbersCache;
import com.example.trafficcop.services.TrafficCopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TrafficCopController {

    private final TrafficCopService trafficCopService;

    @GetMapping("/next")
    public String handeNext() {
        return Optional.ofNullable(trafficCopService.handleNext())
                .map(CarNumber::getText)
                .orElse("Кэш заполнен");
    }

    @GetMapping("/random")
    public String handeRandom() {
        return Optional.ofNullable(trafficCopService.handleRandom())
                .map(CarNumber::getText)
                .orElse("Кэш заполнен");
    }

    @GetMapping("/all")
    public List<String> getAll() {
        return CarNumbersCache.getInstance()
                .getListCarNumbers()
                .stream()
                .map(CarNumber::getText)
                .collect(Collectors.toList());
    }

}
