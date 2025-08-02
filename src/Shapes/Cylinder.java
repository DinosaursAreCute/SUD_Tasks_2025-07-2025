//a cylinder.
package Shapes;

/**
 * Represents a 3D cylinder shape.
 */
public class Cylinder extends Shape3D {
    private double height;
    private Shapes.Circle base;

    /**
     * Constructs a Cylinder with the given base radius and height.
     * @param radius the radius of the base
     * @param height the height of the cylinder
     */
    public Cylinder(double radius, double height) {
        super(radius,height,radius);
        this.base = new Circle(radius);
        setHeight(height);
    }

    /**
     * Sets the height of the cylinder.
     * @param height the new height
     * @throws IllegalArgumentException if height is not positive
     */
    public void setHeight(double height) {
        if (height <= 0) throw new IllegalArgumentException("Height must be positive");
        this.height = height;
    }

    /**
     * Calculates the volume of the cylinder.
     * @return the volume
     */
    @Override
    public double getVolume() {
        return base.getArea() * height;
    }

    /**
     * Calculates the surface area of the cylinder.
     * @return the surface area
     */
    @Override
    public double getSurfaceArea() {
        return 2 * base.getArea() + base.getPerimeter() * height;
    }
}
