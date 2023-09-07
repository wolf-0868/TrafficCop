package com.example.trafficcop;

import com.example.trafficcop.data.CarNumber;
import com.example.trafficcop.services.CarNumbersCache;
import com.example.trafficcop.services.TrafficCopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class TrafficCopServiceTestCase {

    private final TrafficCopService trafficCopService;
    private final CarNumbersCache cache = CarNumbersCache.getInstance();

    @Autowired
    public TrafficCopServiceTestCase(TrafficCopService aTrafficCopService) {
        trafficCopService = aTrafficCopService;
    }

    @Test
    void testNextArFirstNumber() {
        clearCache();
        CarNumber number = trafficCopService.handleNext();
        assertEquals(CarNumber.MIN_SERIAL, number.getSeries());
        assertEquals(CarNumber.MIN_NUMBER, number.getNumber());
    }

    @Test
    void testNextAtSecondNumber() {
        clearCache();
        Map<String, List<Integer>> map = cache.getCarNumbers();
        map.get(CarNumber.MIN_SERIAL).add(1);
        map.get(CarNumber.MIN_SERIAL).add(2);

        CarNumber number = trafficCopService.handleNext();
        assertEquals(CarNumber.MIN_SERIAL, number.getSeries());
        assertEquals(3, number.getNumber());
    }

    @Test
    void testNextAtSecondSerial() {
        clearCache();
        Map<String, List<Integer>> map = cache.getCarNumbers();
        map.get(CarNumber.MIN_SERIAL)
                .addAll(IntStream.range(CarNumber.MIN_NUMBER, CarNumber.MAX_NUMBER)
                        .boxed()
                        .collect(Collectors.toList()));

        CarNumber number = trafficCopService.handleNext();
        assertEquals("ААВ", number.getSeries());
        assertEquals(1, number.getNumber());
    }

    @Test
    void testNextAtVoidInMiddle() {
        clearCache();
        Map<String, List<Integer>> map = cache.getCarNumbers();
        List<Integer> numbers = map.get(CarNumber.MIN_SERIAL);
        numbers.addAll(IntStream.range(CarNumber.MIN_NUMBER, CarNumber.MAX_NUMBER)
                        .boxed()
                        .collect(Collectors.toList()));
        numbers.removeIf(number -> number.equals(55));

        CarNumber number = trafficCopService.handleNext();
        assertEquals(CarNumber.MIN_SERIAL, number.getSeries());
        assertEquals(55, number.getNumber());
    }

    @Test
    void testNextAtFullCache() {
        clearCache();
        Map<String, List<Integer>> map = cache.getCarNumbers();
        List<Integer> list = IntStream.range(CarNumber.MIN_NUMBER, CarNumber.MAX_NUMBER + 1)
                .boxed()
                .collect(Collectors.toList());
        map.forEach((serial, numbers) -> numbers.addAll(List.copyOf(list)));

        CarNumber carNumber = trafficCopService.handleNext();
        assertNull(carNumber);
    }

    @Test
    void testRandomAtEmptyCache() {
        clearCache();
        CarNumber number = trafficCopService.handleRandom();
        assertNotNull(number);
    }

    @Test
    void testRandomAtAlmostCache() {
        clearCache();
        Map<String, List<Integer>> map = cache.getCarNumbers();
        List<Integer> list = IntStream.range(CarNumber.MIN_NUMBER, CarNumber.MAX_NUMBER)
                .boxed()
                .collect(Collectors.toList());
        map.forEach((serial, numbers) -> numbers.addAll(List.copyOf(list)));

        List<CarNumber> cacheList = cache.getListCarNumbers();
        List<CarNumber> randomNumbers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            randomNumbers.add(trafficCopService.handleRandom());
        }
        assertEquals(5, randomNumbers.size());
        assertFalse(cacheList.containsAll(randomNumbers));
    }

    @Test
    void testRandomAtFullCache() {
        clearCache();
        Map<String, List<Integer>> map = cache.getCarNumbers();
        List<Integer> list = IntStream.range(CarNumber.MIN_NUMBER, CarNumber.MAX_NUMBER + 1)
                .boxed()
                .collect(Collectors.toList());
        map.forEach((serial, numbers) -> numbers.addAll(List.copyOf(list)));
        CarNumber number = trafficCopService.handleRandom();
        assertNull(number);
    }

    private static void clearCache() {
        CarNumbersCache.getInstance().clearCache();
    }

}
