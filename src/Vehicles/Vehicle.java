package Vehicles;
import Employees.Driver;
import Utils.Logger;

public abstract class Vehicle {
    protected Logger logger = new Logger(getClass().getSimpleName());
    protected double tankSize;
    protected double tankLevel;
    protected double currentSpeed;
    protected double maxSpeed;
    protected boolean engineOn;
    protected GPSPosition position;
    protected Driver driver = new Driver(2000,"Default Driver", 15.0, 'B');

    public Vehicle(double tankSize, double maxSpeed, GPSPosition position) {
        setTankSize(tankSize);
        setTankLevel(0);
        setMaxSpeed(maxSpeed);
        setCurrentSpeed(0);
        setEngineOn(false);
        setPosition(position);
    }
    public Vehicle(double tankSize, double maxSpeed,Driver driver, GPSPosition position) {
        setTankSize(tankSize);
        setTankLevel(0);
        setMaxSpeed(maxSpeed);
        setCurrentSpeed(0);
        setEngineOn(false);
        setPosition(position);
        setDriver(driver);

    }

    public void setTankSize(double tankSize) {
        if (tankSize <= 0) throw new IllegalArgumentException("Tank size must be positive");
        this.tankSize = tankSize;
    }

    public void setTankLevel(double tankLevel) {
        if (tankLevel < 0 || tankLevel > tankSize) throw new IllegalArgumentException("Tank level must be between 0 and the tank size");
        this.tankLevel = tankLevel;
    }

    public void setMaxSpeed(double maxSpeed) {
        if (maxSpeed <= 0) throw new IllegalArgumentException("Max speed must be positive");
        this.maxSpeed = maxSpeed;
    }

    public void setCurrentSpeed(double currentSpeed) {
        if (currentSpeed < 0 || currentSpeed > maxSpeed) throw new IllegalArgumentException("Current speed must be between 0 and the max speed");
        this.currentSpeed = currentSpeed;
    }

    public void setEngineOn(boolean engineOn) {
        this.engineOn = engineOn;
    }

    public void setPosition(GPSPosition position) {
        if (position == null) throw new IllegalArgumentException("Position cannot be null");
        this.position = position;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void refuel(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Cannot refuel negative amount");
        logger.debug("Refueling vehicle with " + amount + " units");
        double overflow = (tankLevel + amount) - tankSize; //We dont need to do this but as some point i will surely use that value for something
        if (overflow > 0) {
            tankLevel = tankSize;
            logger.debug("Tank is full, overflow: " + overflow);
        } else {
            tankLevel += amount;
        }
        logger.debug("Tank level after refueling: " + tankLevel);
    }
    public void startEngine() { 
        engineOn = true; 
        logger.debug("Engine started");
    }
    public void stopEngine() { 
        engineOn = false; 
        currentSpeed = 0; 
        logger.debug("Engine stopped");
    }
    public void accelerate(double speed) {
        if (!engineOn) throw new IllegalStateException("Engine must be on");
        currentSpeed = Math.min(currentSpeed + speed, maxSpeed); //this is returns the smaller number. If MaxSpeed is smaller than the set speed, it will return maxSpeed max speed will be applied :D
    }
    public void brake(double speed) {
        currentSpeed = Math.max(currentSpeed - speed, 0); //this is returns the bigger number. If the speed is smaller than 0, it will return 0 so useful so remmeber that one
    }
    public void drive(GPSPosition newPosition) {
        if (!engineOn) throw new IllegalStateException("Engine must be on");
        if (tankLevel <= 0) throw new IllegalStateException("Tank is empty");
        double kilometers = GPSPosition.distanceInKm(
                position.getLatitude(), position.getLongitude(),
                newPosition.getLatitude(), newPosition.getLongitude()
        );
        double needed = kilometers / 10.0;
        if (tankLevel < needed) throw new IllegalStateException("Not enough fuel");
        tankLevel -= needed;
        position = newPosition;
    }


    public double getTankLevel() { return tankLevel; }
    public double getTankSize() { return tankSize; }
    public Driver getDriver() { return driver; }
    public GPSPosition getPosition() { return position; }
    public String getInfo() {
        return "Tank: " + tankLevel + "/" + tankSize + ", Speed: " + currentSpeed + "/" + maxSpeed + ", Engine: " + (engineOn ? "On" : "Off");
    }

    @Override
    public String toString() {
        return String.format("%s{tankSize=%.2f, tankLevel=%.2f, currentSpeed=%.2f, maxSpeed=%.2f, engineOn=%b, position=%s, driver=%s}",
                this.getClass().getSimpleName(), tankSize, tankLevel, currentSpeed, maxSpeed, engineOn, position, driver != null ? driver.getName() : "none");
    }
}
