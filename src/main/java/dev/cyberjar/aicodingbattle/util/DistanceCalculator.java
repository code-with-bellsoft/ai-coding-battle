package dev.cyberjar.aicodingbattle.util;

public final class DistanceCalculator {

    private DistanceCalculator() {
    }

    public static double calculateDistanceInKilometers(double userLatitude,
                                                       double userLongitude,
                                                       double doctorLatitude,
                                                       double doctorLongitude) {
        double deltaLatitude = userLatitude - doctorLatitude;
        double deltaLongitude = userLongitude - doctorLongitude;
        double distanceInDegrees = Math.sqrt((deltaLatitude * deltaLatitude) + (deltaLongitude * deltaLongitude));
        return distanceInDegrees * 111.0;
    }
}
