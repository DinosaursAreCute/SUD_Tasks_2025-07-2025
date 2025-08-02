package Shapes;

/**
 * Represents a 3D pyramid shape with a base and height.
 */
public class Pyramid extends Shape3D
{
    double height;
    Shape2D base;

    /**
     * Constructs a Pyramid with the given dimensions and number of sides.
     * @param width the width of the base
     * @param height the height of the pyramid
     * @param depth the depth of the base
     * @param sides the number of sides of the base
     */
    Pyramid(double width, double height, double depth,int sides) {
        super(width, height, depth);
        base = new Rectangle(width,depth);
    }

    /**
     * Calculates the volume of the pyramid.
     * @return the volume
     */
    @Override
    public double getVolume() {
        return getArea() * height / 3;
    }

    /**
     * Returns the area of the base (not implemented).
     * @return the area (currently always 0)
     */
    private double getArea() {
        return 0;
    }

    /**
     * Gets the base shape of the pyramid.
     * @return the base shape
     */
    public Shape2D getBase() {
        return base;
    }

    /**
     * Sets the base shape of the pyramid.
     * @param base the new base shape
     */
    public void setBase(Shape2D base) {
        this.base = base;
    }

    /**
     * Sets the height of the pyramid.
     * @param height the new height
     * @throws IllegalArgumentException if height is not positive
     */
    public void setHeight(double height) {
        if (height <= 0) throw new IllegalArgumentException("Height must be positive");
        this.height = height;
    }

    /**
     * Gets the height of the pyramid.
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Calculates the surface area of the pyramid.
     * @return the surface area
     */
    @Override
    public double getSurfaceArea() {
        double slantHeightA = Math.sqrt((getWidth() / 2) * (getWidth() / 2) + height * height);
        double slantHeightB = Math.sqrt((getHeight() / 2) * (getHeight() / 2) + height * height);
        double sideAreaA = getWidth() * slantHeightB;
        double sideAreaB = getHeight() * slantHeightA;
        return base.getArea() + sideAreaA + sideAreaB;
    }
}