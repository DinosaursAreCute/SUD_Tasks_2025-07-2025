//regular pyramid with n sides.
package Shapes;

public class RegularPyramid extends Shape3D {
    private final Shapes.RegularPolygon base;
    private double height;

    public RegularPyramid(int nSides, double sideLength, double height) {
        super(sideLength, height, sideLength); //TODO:implement actual calc of width for polygons
        this.base = new Shapes.RegularPolygon(nSides, sideLength);
        setHeight(height);
    }
    public RegularPyramid(RegularPolygon base, double height){
        super(base.getPerimeter(), height, base.getPerimeter()); //TODO:implement actual calc of width for polygons
        this.base = base;
        setHeight(height);
    }

    public void setHeight(double height) {
        if (height <= 0) throw new IllegalArgumentException("Height must be positive");
        this.height = height;
    }

    @Override
    public double getVolume() {
        return (base.getArea() * height) / 3.0;
    }

    @Override
    public double getSurfaceArea() {
        double n = base.getNumSides();
        double s = base.getSideLength();
        double apothem = s / (2 * Math.tan(Math.PI / n));
        double slantHeight = Math.sqrt(apothem * apothem + height * height);
        double lateralArea = base.getPerimeter() * slantHeight / 2.0;
        return base.getArea() + lateralArea;
    }
}
