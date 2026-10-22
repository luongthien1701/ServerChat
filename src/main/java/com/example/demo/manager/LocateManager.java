package com.example.demo.manager;

import com.example.demo.dto.noti.Locate;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public class LocateManager {
    private static final Map<Long, Locate> locate = new ConcurrentHashMap<>();

    public static Locate getLocate(Long userId) {
        return locate.get(userId);
    }
    public static List<Long> getUserIds() {
        return new ArrayList<>(locate.keySet());
    }
    public static void addLocate(Long userId, Double lat, Double lon) {
        if (locate.get(userId) != null) {
            locate.get(userId).setLat(lat);
            locate.get(userId).setLon(lon);
        } else {
            locate.put(userId, new Locate(lat,lon));
        }
        log.info("Added locate for user {}", userId);
    }

    public static void removeLocate(Long userId) {
        if (locate.get(userId) == null) return;
        locate.remove(userId);
    }
    public static double distanceKm(
            double lat1, double lng1,
            double lat2, double lng2
    ) {
        final double R = 6371.0; // bán kính Trái Đất (km)

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
