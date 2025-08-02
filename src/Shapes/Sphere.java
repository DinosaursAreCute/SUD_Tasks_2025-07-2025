//a sphere.
package Shapes;

public class Sphere extends Shape3D {
    private double radius;
    public Sphere(double radius) {
        super(radius,radius,radius);
        setRadius(radius);

    }

    public void setRadius(double radius) {
        if (radius <= 0) throw new IllegalArgumentException("Radius must be positive");
        this.radius = radius;
    }

    @Override
    public double getVolume() { return (4.0/3.0) * Math.PI * Math.pow(radius, 3); }
    @Override
    public double getSurfaceArea() { return 4 * Math.PI * radius * radius; }
}
