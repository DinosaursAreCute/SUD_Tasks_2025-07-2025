package Vehicles;

import Employees.Driver;
import Utils.Logger;

public class Truck extends Vehicle {
    private Logger logger = new Logger(getClass().getSimpleName());
    private double loadingArea;
    private double loadedArea;
    private String brand;
    private String model;
    private int year;
    private int cargoCapacity;

    public Truck(double tankSize, double maxSpeed, GPSPosition position, double loadingArea) {
        super(tankSize, maxSpeed, position);
        setLoadingArea(loadingArea);
        setLoadedArea(0);
    }
    
    public Truck(String brand, String model, int year, int cargoCapacity) {
        super(50.0, 120.0, new GPSPosition(0.0, 0.0)); // Default values
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.cargoCapacity = cargoCapacity;
        setLoadingArea(cargoCapacity);
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
    public double getLoadingArea() { return loadingArea; }
    
    // Getters for CSV data
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public int getCargoCapacity() { return cargoCapacity; }
}
