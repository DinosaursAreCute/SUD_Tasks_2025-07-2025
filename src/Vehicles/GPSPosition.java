// Represents a GPS position with longitude and latitude.
package Vehicles;

public class GPSPosition {
    private double longitude; // 0 to <360
    private double latitude;  // -90 to 90

    public GPSPosition(double longitude, double latitude) {
        setPosition(longitude, latitude);
    }

    public double getLongitude() { return longitude; }
    public double getLatitude() { return latitude; }
    public void setPosition(double longitude, double latitude) {
        if (longitude < 0 || longitude >= 360) {
            throw new IllegalArgumentException("Longitude must be between 0° and 360°.");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90° and 90°.");
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public static double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Erdradius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public String toString() {
        return String.format("GPSPosition{longitude=%.6f, latitude=%.6f}", longitude, latitude);
    }
}
