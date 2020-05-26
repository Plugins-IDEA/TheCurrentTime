package io.github.whimthen.time;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimeUtils {

    private static final ArrayList<String> patterns;
    public static final String MILLISECONDS = "Milliseconds";
    public static final String SECONDS = "Seconds";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    static {
        patterns = new ArrayList<>();
        patterns.add(MILLISECONDS);
        patterns.add(YYYY_MM_DD_HH_MM_SS);
        patterns.add("yyyy-MM-dd HH:mm:ss:SSS");
        patterns.add("yyyy-MM-dd");
        patterns.add("yyyy");
        patterns.add("HH:mm:ss");
        patterns.add("yyyyMMddHHmmss");
        patterns.add("yyyyMMdd");
        patterns.add(SECONDS);
    }

    public static List<String> getDefaultPatterns() {
        return patterns;
    }

    public static String getCurrentTime(String pattern) {
        switch (pattern) {
            case SECONDS:
                return String.valueOf(System.currentTimeMillis() / 1000);
            case MILLISECONDS:
                return String.valueOf(System.currentTimeMillis());
            default:
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return formatter.format(LocalDateTime.now(ZoneId.systemDefault()));
        }
    }

    @NotNull
    public static String getCurrent() {
        return getCurrentTime(YYYY_MM_DD_HH_MM_SS);
    }

    @NotNull
    public static String timestampToDate(String timestamp, String fromType) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        switch (fromType) {
            case MILLISECONDS: {
                return formatter.format(new Timestamp(Long.parseLong(timestamp)).toLocalDateTime());
            }
            case SECONDS: {
                return formatter.format(new Timestamp(Long.parseLong(timestamp) * 1000).toLocalDateTime());
            }
        }
        return "";
    }

    @NotNull
    public static String dateToTimestamp(String date, String pattern) {
        return String.valueOf(LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern))
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

}
