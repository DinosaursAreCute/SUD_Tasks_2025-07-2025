package Vehicles;

import Employees.Driver;
import Utils.Logger;

public class Truck extends Vehicle {
    private Logger logger = new Logger(getClass().getSimpleName());
    private double loadingArea;
    private double loadedArea;

    public Truck(double tankSize, double maxSpeed, GPSPosition position, double loadingArea) {
        super(tankSize, maxSpeed, position);
        setLoadingArea(loadingArea);
        setLoadedArea(0);
    }

    public void setLoadingArea(double loadingArea) {
        if (loadingArea <= 0) throw new IllegalArgumentException("Loading area must be positive");
        this.loadingArea = loadingArea;
    }

    public void setLoadedArea(double loadedArea) {
        if (loadedArea < 0 || loadedArea > loadingArea) throw new IllegalArgumentException("Loaded area must be between 0 and the maximum loading area");
        this.loadedArea = loadedArea;
    }

    public void load(double area) {
        if (loadedArea + area > loadingArea) throw new IllegalArgumentException("Exceeds max loading area");
        loadedArea += area;
        logger.debug("Loaded " + area + " units. Total loaded: " + loadedArea);
    }
    public void unload(double area) {
        if (area > loadedArea) throw new IllegalArgumentException("Cannot unload more than loaded");
        loadedArea -= area;
        logger.debug("Unloaded " + area + " units. Remaining: " + loadedArea);
    }
    @Override
    public void setDriver(Driver driver) {
        if (driver.getLicense() != 'C') throw new IllegalArgumentException("Truck driver must have license C");
        super.setDriver(driver);
    }
    public double getLoadedArea() { return loadedArea; }
}
