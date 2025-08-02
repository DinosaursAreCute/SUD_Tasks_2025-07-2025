// a regular prism with polygon base.
package Shapes;

public class RegularPrism extends Shape3D {
    private RegularPolygon base;
    private double height;

    public RegularPrism(RegularPolygon base, double height) {
        super(base.getPerimeter(), height, base.getPerimeter()); //TODO:implement actual calc of width for polygons
        this.base = base;
        setHeight(height);
    }
    public RegularPrism(int nSides, double sideLength, double height) {
        super(sideLength, height, sideLength); //TODO:implement actual calc of width for polygons
        this.base = new RegularPolygon(nSides, sideLength);
        setHeight(height);
    }
    public void setHeight(double height) {
        if (height <= 0) throw new IllegalArgumentException("Height must be positive");
        this.height = height;
    }

    @Override
    public double getVolume() {
        return base.getArea() * height;
    }

    @Override
    public double getSurfaceArea() {
        return 2 * base.getArea() + base.getPerimeter() * height;
    }
}
