//a cuboid
package Shapes;

/**
 * Represents a 3D cuboid shape (rectangular prism).
 */
public class Cuboid extends Shape3D {
    private double width, height, depth;

    /**
     * Constructs a Cuboid with the given dimensions.
     * @param width the width of the cuboid
     * @param height the height of the cuboid
     * @param depth the depth of the cuboid
     */
    public Cuboid(double width, double height, double depth) {
        super(width,height,depth);
        setWidth(width);
        setHeight(height);
        setDepth(depth);
    }

    /**
     * Sets the width of the cuboid.
     * @param width the new width
     * @throws IllegalArgumentException if width is not positive
     */
    public void setWidth(double width) {
        if (width <= 0) throw new IllegalArgumentException("Width must be positive");
        this.width = width;
    }

    /**
     * Sets the height of the cuboid.
     * @param height the new height
     * @throws IllegalArgumentException if height is not positive
     */
    public void setHeight(double height) {
        if (height <= 0) throw new IllegalArgumentException("Height must be positive");
        this.height = height;
    }

    /**
     * Sets the depth of the cuboid.
     * @param depth the new depth
     * @throws IllegalArgumentException if depth is not positive
     */
    public void setDepth(double depth) {
        if (depth <= 0) throw new IllegalArgumentException("Depth must be positive");
        this.depth = depth;
    }

    /**
     * Calculates the volume of the cuboid.
     * @return the volume
     */
    @Override
    public double getVolume() { return width * height * depth; }

    /**
     * Calculates the surface area of the cuboid.
     * @return the surface area
     */
    @Override
    public double getSurfaceArea() { return 2 * (width * height + width * depth + height * depth); }
}
