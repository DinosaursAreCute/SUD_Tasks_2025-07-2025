package Shapes;

abstract public class Pyramids extends Shape3D{
    double height;
    Shape2D base;

    Pyramids(double width, double height, double depth,int sides) {
        super(width, height, depth);
        base = new Rectangle(width,depth);
    }

    @Override
    public double getVolume() {
        return getArea() * height / 3;
    }

    private double getArea() {
        return 0;
    }

    public Shape2D getBase() {
        return base;
    }

    public void setBase(Shape2D base) {
        this.base = base;
    }

    public void setHeight(double height) {
        if (height <= 0) throw new IllegalArgumentException("Height must be positive");
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public double getSurfaceArea() {
        double slantHeightA = Math.sqrt((getWidth() / 2) * (getWidth() / 2) + height * height);
        double slantHeightB = Math.sqrt((getHeight() / 2) * (getHeight() / 2) + height * height);
        double sideAreaA = getWidth() * slantHeightB;
        double sideAreaB = getHeight() * slantHeightA;
        return base.getArea() + sideAreaA + sideAreaB;
    }
}
