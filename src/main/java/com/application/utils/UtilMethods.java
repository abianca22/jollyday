package com.application.utils;

import java.time.LocalDate;

public class UtilMethods {
    public static LocalDate transformDate(String dateStr) {
        var dateParts = dateStr.split("[./-]+");
        return LocalDate.of(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]));
    }
}
