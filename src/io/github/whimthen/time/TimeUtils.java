package io.github.whimthen.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimeUtils {

    private static final ArrayList<String> patterns;
    public static final String MILLISECONDS = "MILLISECONDS";
    public static final String SECONDS = "SECONDS";
    public static final String NANO_TIME = "NANO_TIME";

    static {
        patterns = new ArrayList<>();
        patterns.add(MILLISECONDS);
        patterns.add("yyyy-MM-hh HH:mm:ss");
        patterns.add("yyyy-MM-hh HH:mm:SSS");
        patterns.add("yyyy-MM-hh");
        patterns.add("yyyy");
        patterns.add("HH:mm:ss");
        patterns.add("yyyyMMhhHHmmss");
        patterns.add("yyyyMMhh");
        patterns.add(SECONDS);
        patterns.add(NANO_TIME);
    }

    public static List<String> getDefaultPatterns() {
        return patterns;
    }

    public static String getCurrentTime(String pattern) {
        switch (pattern) {
            case SECONDS:
                return String.valueOf(System.currentTimeMillis() / 1000);
            case NANO_TIME:
                return String.valueOf(System.nanoTime());
            case MILLISECONDS:
                return String.valueOf(System.currentTimeMillis());
            default:
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return formatter.format(LocalDateTime.now());
        }
    }

}
