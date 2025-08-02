// Abstract base class for 3D shapes. Requires volume and surface area calculation.
package Shapes;

/**
 * Abstract base class for 3D shapes. Requires implementation of volume and surface area calculations.
 */
public abstract class Shape3D implements INamed {
    double height;
    double depth;
    double width;

    /**
     * Returns the name of the shape with its dimensions.
     * @return the name string
     */
    public String name(){
        return getClass().getName() + "_h" +height+"_d"+depth+"_w"+width;
    }

    /**
     * Prints the name of the shape.
     */
    public void printName(){
        System.out.println(name());
    }

    /**
     * Constructs a 3D shape with the given dimensions.
     * @param width the width
     * @param height the height
     * @param depth the depth
     */
    Shape3D(double width,double height, double depth){
        setAllDimension(width,height,depth);
    }

    /**
     * Sets the width of the shape.
     * @param width the width
     * @throws IllegalArgumentException if width is not positive
     */
    public void setWidth(double width) {
        if (width <= 0) throw new IllegalArgumentException("Width must be positive");
        this.width = width;
    }

    /**
     * Sets the depth of the shape.
     * @param depth the depth
     * @throws IllegalArgumentException if depth is not positive
     */
    public void setDepth(double depth) {
        if (depth <= 0) throw new IllegalArgumentException("Depth must be positive");
        this.depth = depth;
    }

    /**
     * Sets the height of the shape.
     * @param height the height
     * @throws IllegalArgumentException if height is not positive
     */
    public void setHeight(double height) {
        if (height <= 0) throw new IllegalArgumentException("Height must be positive");
        this.height = height;
    }

    /**
     * Sets all dimensions of the shape.
     * @param width the width
     * @param height the height
     * @param depth the depth
     */
    public void setAllDimension(double width, double height,double depth){
        setDepth(depth);
        setHeight(height);
        setWidth(width);
    }

    /**
     * Gets the height of the shape.
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Gets the depth of the shape.
     * @return the depth
     */
    public double getDepth() {
        return depth;
    }

    /**
     * Gets the width of the shape.
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Calculates the volume of the shape.
     * @return the volume
     */
    public abstract double getVolume();

    /**
     * Calculates the surface area of the shape.
     * @return the surface area
     */
    public abstract double getSurfaceArea();
}
