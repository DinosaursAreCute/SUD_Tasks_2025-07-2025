//a circular cone.
package Shapes;

/**
 * Represents a 3D circular cone shape.
 */
public class Cone extends Shape3D {
    private Circle base;
    private double height;

    /**
     * Constructs a Cone with the given base radius and height.
     * @param radius the radius of the base
     * @param height the height of the cone
     */
    public Cone(double radius, double height) {
        super(radius,height,radius);
        this.base = new Circle(radius);
        setHeight(height);
    }

    /**
     * Sets the height of the cone.
     * @param height the new height
     * @throws IllegalArgumentException if height is not positive
     */
    public void setHeight(double height) {
        if (height <= 0) throw new IllegalArgumentException("Height must be positive");
        this.height = height;
    }

    /**
     * Calculates the volume of the cone.
     * @return the volume
     */
    @Override
    public double getVolume() {
        return (base.getArea() * height) / 3.0;
    }

    /**
     * Calculates the surface area of the cone.
     * @return the surface area
     */
    @Override
    public double getSurfaceArea() {
        double slant = Math.sqrt(base.getRadius() * base.getRadius() + height * height);
        return base.getArea() + Math.PI * base.getRadius() * slant;
    }
}
