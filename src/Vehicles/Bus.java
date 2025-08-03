// Represents a bus, a passenger vehicle that requires a driver with license D.
package Vehicles;

import Employees.Driver;

public class Bus extends PassengerVehicle {
    
    public Bus(double tankSize, double maxSpeed, GPSPosition position, int seatCount, Driver driver) {
        super(tankSize, maxSpeed, driver, position, seatCount, 'D');
    }
    
    public Bus(double tankSize, double maxSpeed, GPSPosition position, int seatCount) {
        super(tankSize, maxSpeed, position, seatCount, 'D');
    }
}
