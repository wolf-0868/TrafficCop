package com.example.trafficcop.services;

import com.example.trafficcop.data.CarNumber;
import com.example.trafficcop.data.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CarNumbersCache {

    private static CarNumbersCache instance;

    private final Map<String, List<Integer>> carNumbers = new TreeMap<>(String::compareTo);

    private CarNumbersCache() {
        init();
    }

    private void init() {
        StringBuilder builder = new StringBuilder();
        builder.append("   ");
        for (Symbol v1 : Symbol.values()) {
            builder.setCharAt(0, v1.getDesc());
            for (Symbol v2 : Symbol.values()) {
                builder.setCharAt(1, v2.getDesc());
                for (Symbol v3 : Symbol.values()) {
                    builder.setCharAt(2, v3.getDesc());
                    carNumbers.put(builder.toString(), new ArrayList<>());
                }
            }
        }
    }

    public static CarNumbersCache getInstance() {
        if (instance == null) {
            instance = new CarNumbersCache();
        }
        return instance;
    }

    public void addCarNumber(CarNumber aCarNumber) {
        carNumbers.get(aCarNumber.getSeries()).add(aCarNumber.getNumber());
    }

    public Map<String, List<Integer>> getCarNumbers() {
        return carNumbers;
    }

    public Set<Map.Entry<String, List<Integer>>> getEntrySetCarNumbers() {
        return carNumbers.entrySet();
    }

    public List<CarNumber> getListCarNumbers() {
        return carNumbers.entrySet()
                .stream()
                .flatMap(entry -> entry.getValue()
                        .stream()
                        .map(num -> new CarNumber(entry.getKey(), num)))
                .collect(Collectors.toList());
    }

    public void clearCache() {
        carNumbers.values().forEach(List::clear);
    }

}
