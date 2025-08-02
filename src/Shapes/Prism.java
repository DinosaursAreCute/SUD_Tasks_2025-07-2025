package Shapes;
/**
 * Represents a 3D prism with a rectangular base.
 */
public class Prism extends Shape3D{
    private double height;
    private Rectangle base;

    /**
     * Constructs a Prism with the given base and height.
     * @param base the rectangular base
     * @param height the height of the prism
     */
    public Prism(Rectangle base, double height) {
        super(base.getWidth(),height,base.getHeight());
        setHeight(height);
        setBase(base);
    }

    /**
     * Gets the height of the prism.
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the height of the prism.
     * @param height the new height
     * @throws IllegalArgumentException if height is not positive
     */
    public void setHeight(double height) {
        if(height <= 0) throw new IllegalArgumentException("Height must be positive");
        else this.height = height;
    }

    /**
     * Gets the area of the base.
     * @return the area of the base
     */
    public double getBase() {
        return base.getArea();
    }

    /**
     * Sets the base of the prism.
     * @param base the new rectangular base
     */
    public void setBase(Rectangle base) {
        this.base = base;
    }

    /**
     * Calculates the volume of the prism.
     * @return the volume
     */
    public double getVolume(){
        return base.getWidth() * base.getHeight() * height;
    }

    /**
     * Calculates the surface area of the prism.
     * @return the surface area
     */
    public double getSurfaceArea(){
        return 2 * getBase() + base.getPerimeter() * height;
    }
}
