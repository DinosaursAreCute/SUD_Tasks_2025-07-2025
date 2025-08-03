package Vehicles;

import Employees.Driver;
//Truck class represents a vehicle that can carry goods, with a specific loading area and loaded area.
public class Truck extends Vehicle {
    private double loadingArea;
    private double loadedArea;

    public Truck(double tankSize, double maxSpeed, GPSPosition position, double loadingArea) {
        super(tankSize, maxSpeed, position, 'C');
        if (loadingArea <= 0) throw new IllegalArgumentException("Loading area must be positive");
        this.loadingArea = loadingArea;
        this.loadedArea = 0;
    }

    public Truck(double tankSize, double maxSpeed, Driver driver, GPSPosition position, double loadingArea) {
        super(tankSize, maxSpeed, driver, position, 'C');
        if (loadingArea <= 0) throw new IllegalArgumentException("Loading area must be positive");
        this.loadingArea = loadingArea;
        this.loadedArea = 0;
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
    public double getLoadedArea() { return loadedArea; }
}
