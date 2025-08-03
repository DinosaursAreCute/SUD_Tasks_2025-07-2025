// Represents a bus, a passenger vehicle that requires a driver with license D.
package Vehicles;

import Employees.Driver;

public class Bus extends PassengerVehicle {
    private String driverName;
    private String brand;
    private String model;
    private int year;

    public Bus(double tankSize, double maxSpeed, GPSPosition position, int seatCount) {
        super(tankSize, maxSpeed, position, seatCount);
    }
    
    public Bus(String brand, String model, int year, int seats) {
        super(100.0, 100.0, new GPSPosition(0.0, 0.0), seats); // Default values
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    @Override
    public void setDriver(Driver driver) {
        if (driver.getLicense() != 'D') throw new IllegalArgumentException("Bus driver must have license D");
        super.setDriver(driver);
    }

    public String getDriverName() { return driverName; }
    public int getSeats() { return super.getSeats(); } // Delegate to parent class
    
    // Getters for CSV data
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
}
