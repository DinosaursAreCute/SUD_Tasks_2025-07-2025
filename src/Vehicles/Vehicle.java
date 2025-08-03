package Vehicles;
import Employees.Driver;
import Utils.Logger;
import java.util.HashMap;

public abstract class Vehicle {
    protected Logger logger = new Logger(getClass().getSimpleName());
    protected double tankSize;
    protected double tankLevel;
    protected double currentSpeed;
    protected double maxSpeed;
    protected boolean engineOn;
    private char licenseReq;
    protected GPSPosition position;
    protected Driver driver;
    private HashMap<Character,Integer> licenseMap = new HashMap<>();
    {
        // Initialize license map with default values
        addToHashMap('A', 1);
        addToHashMap('B', 2);
        addToHashMap('C', 3);
        addToHashMap('D', 4);
    }

    //constructors
    public Vehicle(double tankSize, double maxSpeed, GPSPosition position) {
        if (tankSize <= 0) throw new IllegalArgumentException("Tank size must be positive");
        if (maxSpeed <= 0) throw new IllegalArgumentException("Max speed must be positive");
        if (position == null) throw new IllegalArgumentException("Position cannot be null");
        
        this.tankSize = tankSize;
        this.tankLevel = 0;
        this.maxSpeed = maxSpeed;
        this.currentSpeed = 0;
        this.engineOn = false;
        this.position = position;
    }

    public Vehicle(double tankSize, double maxSpeed,Driver driver, GPSPosition position, char licenseReq) {
        if (tankSize <= 0) throw new IllegalArgumentException("Tank size must be positive");
        if (maxSpeed <= 0) throw new IllegalArgumentException("Max speed must be positive");
        if (position == null) throw new IllegalArgumentException("Position cannot be null");
        if (licenseReq != 'A' && licenseReq != 'B' && licenseReq != 'C' && licenseReq != 'D') throw new IllegalArgumentException("Invalid license type. Must be A, B, C, or D.");
        if (driver == null) throw new NullPointerException("Driver cannot be null");
        
        this.tankSize = tankSize;
        this.tankLevel = 0;
        this.maxSpeed = maxSpeed;
        this.currentSpeed = 0;
        this.engineOn = false;
        this.position = position;
        this.licenseReq = licenseReq;
        
        // Validate driver license directly
        char driverLicense = driver.getLicense();
        if (driverLicense != 'A' && driverLicense != 'B' && driverLicense != 'C' && driverLicense != 'D') {
            throw new IllegalArgumentException("Driver has invalid license");
        }
        if (licenseMap.get(driverLicense) < licenseMap.get(licenseReq)) {
            throw new IllegalArgumentException("Driver must have license " + licenseReq);
        }
        this.driver = driver;
    }

    public Vehicle(double tankSize, double maxSpeed, GPSPosition position, char licenseReq) {
        if (tankSize <= 0) throw new IllegalArgumentException("Tank size must be positive");
        if (maxSpeed <= 0) throw new IllegalArgumentException("Max speed must be positive");
        if (position == null) throw new IllegalArgumentException("Position cannot be null");
        if (licenseReq != 'A' && licenseReq != 'B' && licenseReq != 'C' && licenseReq != 'D') throw new IllegalArgumentException("Invalid license type. Must be A, B, C, or D.");
        
        this.tankSize = tankSize;
        this.tankLevel = 0;
        this.maxSpeed = maxSpeed;
        this.currentSpeed = 0;
        this.engineOn = false;
        this.position = position;
        this.licenseReq = licenseReq;
    }
    // Setters and Getters
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
        try {
            logger.debug("Trying to set driver: " + (driver != null ? driver.getName() : "null"));
            if(driver == null) throw new NullPointerException("Driver cannot be null");
            if (!isLicenseReqMet(driver.getLicense())) {
                throw new IllegalArgumentException("Driver must have license " + getLicenseReq());
            }
            this.driver = driver;
            logger.debug("Driver set successfully: " + driver.getName());
        } catch (NullPointerException e) {
            logger.error("Error setting driver: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            logger.error("Error setting driver: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected char getLicenseReq() {
        return licenseReq;
    }

    protected void setLicenseReq(char licenseReq) {
        try {
            if (licenseReq != 'A' && licenseReq != 'B' && licenseReq != 'C' && licenseReq != 'D') throw new IllegalArgumentException("Invalid license type. Must be A, B, C, or D.");
            this.licenseReq = licenseReq;
        } catch (IllegalArgumentException e) {
            logger.error("Error setting license requirement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public HashMap<Character, Integer> getLicenseMap() {
        return licenseMap;
    }

    public void setLicenseMap(HashMap<Character, Integer> licenseMap) {
        try {
            if(licenseMap == null )throw new IllegalArgumentException("License map cannot be null");
            this.licenseMap = licenseMap;
        } catch (IllegalArgumentException e) {
            logger.error("Error setting license map: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void addToHashMap(char license, int value) {
        if (licenseMap.containsKey(license)) {
            licenseMap.put(license, licenseMap.get(license) + value);
        } else {
            licenseMap.put(license, value);
        }
    }
    // Methods
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


    public Boolean isLicenseValid(char lincense) {
        logger.debug("Checking license validity: " + lincense);
        return lincense == 'A' || lincense == 'B' || lincense == 'C' || lincense == 'D';
    }
    public Boolean isLicenseReqMet(char license) {
        logger.debug("Checking license requirement: " + license);
        return isLicenseValid(license) && licenseMap.get(license) >= licenseMap.get(licenseReq);
    }
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
