package com.springboot.safescan.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static String format(LocalDateTime time) {
        return time != null ? time.format(FORMATTER) : null;
    }
}