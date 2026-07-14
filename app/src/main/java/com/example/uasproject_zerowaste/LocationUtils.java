package com.example.uasproject_zerowaste;

import java.util.HashMap;
import java.util.Map;

public class LocationUtils {

    // Mock Database Koordinat untuk simulasi geocoding di Tangerang/Sekitarnya
    // Membantu agar sistem bisa "menghitung" jarak secara otomatis berdasarkan input teks
    private static final Map<String, double[]> MOCK_COORDINATES = new HashMap<>();

    static {
        // Area Karawaci & Sekitarnya (Sangat Dekat)
        MOCK_COORDINATES.put("KARAWACI", new double[]{-6.2253, 106.6212});
        MOCK_COORDINATES.put("UPH", new double[]{-6.2263, 106.6112});
        MOCK_COORDINATES.put("LIPPO VILLAGE", new double[]{-6.2243, 106.6182});
        MOCK_COORDINATES.put("BINONG", new double[]{-6.2366, 106.5925});
        MOCK_COORDINATES.put("PERUM", new double[]{-6.2163, 106.6025});

        // Gading Serpong (Dekat, ~3-5km dari Karawaci)
        MOCK_COORDINATES.put("GADING SERPONG", new double[]{-6.2415, 106.6285});
        MOCK_COORDINATES.put("SUMMARECON MALL SERPONG", new double[]{-6.2425, 106.6275});
        
        // Alam Sutera (~7-10km dari Karawaci)
        MOCK_COORDINATES.put("ALAM SUTERA", new double[]{-6.2238, 106.6652});
        MOCK_COORDINATES.put("MALL ALAM SUTERA", new double[]{-6.2228, 106.6642});

        // Tangerang Kota (~5-8km)
        MOCK_COORDINATES.put("TANGERANG KOTA", new double[]{-6.1764, 106.6359});
        MOCK_COORDINATES.put("CIKOKOL", new double[]{-6.1963, 106.6432});

        // BSD (Lebih Jauh, ~12-15km)
        MOCK_COORDINATES.put("BSD", new double[]{-6.3021, 106.6621});
        MOCK_COORDINATES.put("AEON MALL", new double[]{-6.3051, 106.6641});
        
        // Jauh (>20km)
        MOCK_COORDINATES.put("JAKARTA", new double[]{-6.2088, 106.8456});
    }

    public static double[] getCoordinates(String address) {
        if (address == null || address.isEmpty()) {
            // Default center point (Karawaci) if address empty
            return MOCK_COORDINATES.get("KARAWACI");
        }
        
        String upperAddress = address.toUpperCase();
        for (String key : MOCK_COORDINATES.keySet()) {
            if (upperAddress.contains(key)) {
                return MOCK_COORDINATES.get(key);
            }
        }
        
        // Jika tidak ada di list, gunakan hash dari string untuk menentukan titik semi-konsisten
        // agar tidak berubah-ubah setiap dipanggil tapi tetap terasa "calculated"
        double hash = Math.abs(address.hashCode() % 100) / 1000.0;
        return new double[]{-6.2000 + hash, 106.6000 + hash};
    }

    public static double calculateDistance(String addr1, String addr2) {
        double[] coord1 = getCoordinates(addr1);
        double[] coord2 = getCoordinates(addr2);
        
        return haversine(coord1[0], coord1[1], coord2[0], coord2[1]);
    }

    // Formula Haversine untuk menghitung jarak antara 2 titik koordinat (KM)
    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius bumi dalam KM
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round((R * c) * 10.0) / 10.0; // Round to 1 decimal
    }
}