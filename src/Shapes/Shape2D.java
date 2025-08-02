// Abstract base class for 2D shapes. Requires area and perimeter calculation.
package Shapes;

import Utils.Logger;

public abstract class Shape2D implements INamed {
    Logger logger = new Logger(getClass().getSimpleName());
    private double width;
    private double height;
    Shape2D(double width, double height) {
        logger.info("Creating shape with width: "+width+" Height: "+height);
        setHeight(height);
        setWidth(width);
    }

    @Override
    public String name() {
        return getClass().getName() + "_h" +height+"_w"+width;
    }
    public void printName(){
        System.out.println(name());
    }
    public void setWidth(double width) {
        if (width <= 0) throw new IllegalArgumentException("Width must be positive");
        this.width = width;
    }
    public void setSide(double newSide, double oldSide){
            if (newSide <= 0) throw new IllegalArgumentException("Side must be positive");
            oldSide = newSide;
    }
    public void setHeight(double height) {
        if (height <= 0) throw new IllegalArgumentException("Height must be positive");
        this.height = height;
    }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public abstract double getArea();
    public abstract double getPerimeter();
}
