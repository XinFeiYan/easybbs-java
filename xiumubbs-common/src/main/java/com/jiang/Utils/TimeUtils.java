package com.jiang.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;


public class TimeUtils {
    public static String format(LocalDateTime localDateTime, String formatter) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        String time = localDateTime.format(dateTimeFormatter);
        return time;
    }

    //字符串转日期
    public static LocalDateTime parse(String date, String formatter) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
            TemporalAccessor parse = dateTimeFormatter.parse(date);
            return LocalDateTime.from(parse);
        } catch (DateTimeParseException ignored) {
        }
        return null;
    }
}
