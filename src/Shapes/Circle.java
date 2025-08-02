//a circle
package Shapes;
import Utils.Logger;
/**
 * Represents a 2D circle shape.
 */
public class Circle extends Shape2D {
    private double radius;
    private static Logger logger = new Logger("Shapes.Circle");
    /**
     * Constructs a Circle with the given radius.
     * @param radius the radius of the circle
     */
    public Circle(double radius) {
        super(radius,radius);
        setRadius(radius);
    }

    /**
     * Calculates the area of the circle.
     * @return the area
     */
    @Override
    public double getArea() { return Math.PI * radius * radius; }

    /**
     * Calculates the perimeter (circumference) of the circle.
     * @return the perimeter
     */
    @Override
    public double getPerimeter() { return 2 * Math.PI * radius; }

    /**
     * Gets the radius of the circle.
     * @return the radius
     */
    public double getRadius() { return radius; }

    /**
     * Sets the radius of the circle.
     * @param radius the new radius
     * @throws IllegalArgumentException if radius is not positive
     */
    public void setRadius(double radius) {
        if (radius <= 0){
            logger.error("Radius must be positive, received: " + radius);
            throw new IllegalArgumentException("Radius must be positive");
        }
        this.radius = radius;
    }
}
