// Represents a bus, a passenger vehicle that requires a driver with license D.
package Vehicles;

import Employees.Driver;

public class Bus extends PassengerVehicle {
    private String driverName;

    public Bus(double tankSize, double maxSpeed, GPSPosition position, int seatCount) {
        super(tankSize, maxSpeed, position, seatCount);
    }

    @Override
    public void setDriver(Driver driver) {
        if (driver.getLicense() != 'D') throw new IllegalArgumentException("Bus driver must have license D");
        super.setDriver(driver);
    }

    public String getDriverName() { return driverName; }
}
