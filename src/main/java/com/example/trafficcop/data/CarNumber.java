package com.example.trafficcop.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class CarNumber implements Comparable<CarNumber> {

    private static final String REGION_CONSTANT = " 116 RUS";

    public static final int MIN_NUMBER = 1;
    public static final int MAX_NUMBER = 999;
    public static final String MIN_SERIAL = "ААА";
    public static final String MAX_SERIAL = "ХХХ";

    @JsonProperty(index = 0, value = "series")
    private String series;

    @JsonProperty(index = 1, value = "number")
    private int number;

    @Override
    public int compareTo(@NonNull CarNumber aObj) {
        return compare(this, aObj);
    }

    public static int compare(CarNumber aObj1, CarNumber aObj2) {
        int cmp = aObj1.series.compareTo(aObj2.series);
        if (cmp == 0) {
            cmp = Integer.compare(aObj1.number, aObj2.number);
        }
        return cmp;
    }

    public String getText() {
        return new StringBuilder()
                .append(series.charAt(0))
                .append(String.format("%03d", number))
                .append(series.charAt(1))
                .append(series.charAt(2))
                .append(REGION_CONSTANT)
                .toString();
    }

}
