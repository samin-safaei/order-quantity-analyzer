package com.cleanhub.orderquantityanalyzer.testutil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static LocalDateTime yesterday() {
        LocalDateTime now = LocalDateTime.now();
        return now.minusDays(1);
    }

    public static LocalDateTime minusDays(long days) {
        LocalDateTime now = LocalDateTime.now();
        return now.minusDays(days);
    }

    public static LocalDateTime lastMonth() {
        LocalDateTime now = LocalDateTime.now();
        return now.minusMonths(1);
    }

    /**
     * @param dateTimeString in format "yyyy-MM-dd HH:mm:ss"
     * @return equivalent LocalDate
     */
    public static LocalDateTime fromString(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

}
