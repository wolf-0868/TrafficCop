package com.example.trafficcop.services;

import com.example.trafficcop.data.CarNumber;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TrafficCopService {

    public CarNumber handleNext() {
        String serial = null;
        Integer number = null;

        CarNumbersCache cache = CarNumbersCache.getInstance();

        for (Map.Entry<String, List<Integer>> entry : cache.getEntrySetCarNumbers()) {
            if (entry.getValue().isEmpty()) {
                serial = entry.getKey();
                number = 1;
                break;
            }
            if (entry.getValue().size() < CarNumber.MAX_NUMBER) {
                serial = entry.getKey();
                number = findSmallestNumber(entry.getValue());
                if (number < CarNumber.MAX_NUMBER) {
                    break;
                } else {
                    serial = null;
                    number = null;
                }
            }
        }
        CarNumber result = null;
        if ((serial != null) && (number != null)) {
            result = new CarNumber(serial, number);
            cache.addCarNumber(result);
        }
        return result;
    }

    public CarNumber handleRandom() {
        CarNumbersCache cache = CarNumbersCache.getInstance();
        List<Map.Entry<String, List<Integer>>> entries = cache.getEntrySetCarNumbers()
                .stream()
                .filter(entry -> entry.getValue().size() < CarNumber.MAX_NUMBER)
                .collect(Collectors.toList());

        if (entries.isEmpty()) {
            return null;
        }

        Map.Entry<String, List<Integer>> entry = entries.get(randomInt(entries.size()));
        List<Integer> list = IntStream.range(CarNumber.MIN_NUMBER, CarNumber.MAX_NUMBER + 1)
                .boxed()
                .collect(Collectors.toList());
        for (Integer num : entry.getValue()) {
            list.remove(num);
        }
        CarNumber result = new CarNumber(entry.getKey(), list.get(randomInt(list.size())));
        cache.addCarNumber(result);

        return result;
    }

    public static int randomInt(int aInt) {
        return new Random().nextInt(aInt);
    }

    private static int findSmallestNumber(List<Integer> aInts) {
        boolean[] dp = new boolean[aInts.size() + 1];
        for (int number : aInts) {
            if (number < dp.length) {
                dp[number] = true;
            }
        }
        for (int i = 1; i < dp.length; i++) {
            if (!dp[i]) {
                return i;
            }
        }
        return dp.length;
    }

}
