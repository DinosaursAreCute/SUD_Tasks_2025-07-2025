// Represents a passenger vehicle with seat management.
package Vehicles;

import Employees.Driver;
import Employees.Employee;
import Utils.Logger;
import java.util.ArrayList;
import java.util.List;

public class PassengerVehicle extends Vehicle {
    private Logger logger = new Logger(getClass().getSimpleName());
    private int seatCount;
    private List<Employee> passengers;
    private String brand;
    private String model;
    private int year;

    public PassengerVehicle(double tankSize, double maxSpeed, GPSPosition position, int seatCount) {
        super(tankSize, maxSpeed, position);
        setPosition(position);
        setSeatCount(seatCount);
        setPassengers(new ArrayList<>());
    }
    
    public PassengerVehicle(String brand, String model, int year, int seats) {
        super(50.0, 150.0, new GPSPosition(0.0, 0.0)); // Default values
        this.brand = brand;
        this.model = model;
        this.year = year;
        setSeatCount(seats);
        setPassengers(new ArrayList<>());
    }

    @Override
    public void setDriver(Driver driver) {
        if (driver.getLicense() != 'B') throw new IllegalArgumentException("Truck driver must have license B");
        super.setDriver(driver);
    }

    public void setSeatCount(int seatCount) {
        if (seatCount <= 0) throw new IllegalArgumentException("Seat count must be positive");
        this.seatCount = seatCount;
    }

    public void setPassengers(List<Employee> passengers) {
        if (passengers == null) throw new IllegalArgumentException("Passengers list cannot be null");
        this.passengers = passengers;
    }

    public boolean boardPassenger(Employee e) {
        if (passengers.size() < seatCount) {
            passengers.add(e);
            return true;
        }
        return false;
    }

    public boolean unboardPassenger(Employee e) {
        return passengers.remove(e);
    }

    public int getPassengerCount() { return passengers.size(); }
    public int getSeats() { return seatCount; }
    
    // Getters for CSV data
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }

    @Override
    public String toString() {
        return String.format("PassengerVehicle{%s, seatCount=%d, passengers=%s}", super.toString(), seatCount, passengers);
    }
}
