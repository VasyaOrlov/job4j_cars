package ru.job4j.cars.util;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class UserTimeZone {

    public static List<TimeZone> getZones() {
        List<TimeZone> zones = new ArrayList<>();
        for (String timeId : TimeZone.getAvailableIDs()) {
            zones.add(TimeZone.getTimeZone(timeId));
        }
        return zones;
    }
}
