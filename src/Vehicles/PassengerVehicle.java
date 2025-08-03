package Vehicles;

import Employees.Driver;
import Employees.Employee;
import java.util.ArrayList;
import java.util.List;

public class PassengerVehicle extends Vehicle {
    private int seatCount;
    private List<Employee> passengers;

    public PassengerVehicle(double tankSize, double maxSpeed, GPSPosition position, int seatCount, char licenseReq) {
        super(tankSize, maxSpeed, position, licenseReq);
        if (seatCount <= 0) throw new IllegalArgumentException("Seat count must be positive");
        this.seatCount = seatCount;
        this.passengers = new ArrayList<>();
    }

    public PassengerVehicle(double tankSize, double maxSpeed, Driver driver, GPSPosition position, int seatCount, char licenseReq) {
        super(tankSize, maxSpeed, driver, position, licenseReq);
        if (seatCount <= 0) throw new IllegalArgumentException("Seat count must be positive");
        this.seatCount = seatCount;
        this.passengers = new ArrayList<>();
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

    @Override
    public String toString() {
        return String.format("PassengerVehicle{%s, seatCount=%d, passengers=%s}", super.toString(), seatCount, passengers);
    }
}
