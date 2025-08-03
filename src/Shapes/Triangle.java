package Shapes;
import Utils.Logger;
public class Triangle extends Shape2D {
    private Double[] sides = new Double[3];
    private static Logger logger = new Logger ("Shapes.Triangle");
    public Triangle(double sideA, double sideB, double sideC) {
        super(0, 0);
        sides[0] = sideA;
        sides[1] = sideB;
        sides[2] = sideC;
        if (!validateTriangle(sides)) {
            throw new IllegalArgumentException("Invalid triangle sides");
        }
        setHeight(getHeight());
    }

    public double getSideA() {
        return sides[0];
    }

    public void setSideA(double sideA) {
        Double oldA = sides[0];
        sides[0] = sideA;
        try{ validateTriangle(sides);}catch (IllegalArgumentException e){
            sides[0]=oldA;
            logger.error("Shapes.Triangle.java/setSideA: " + e.getMessage());
        }
    }

    public double getSideB() {
        return sides[1];
    }

    public void setSideB(double sideB) {
        Double oldB = sides[1];
        sides[1] = sideB;
        try{ validateTriangle(sides);}catch (IllegalArgumentException e){
            sides[1]=oldB;
            logger.error("Shapes.Triangle.java/setSideB: " + e.getMessage());
        }
    }

    public double getSideC() {
        return sides[2];
    }

    public void setSideC(double sideC) {
        Double oldC = sides[2];
        sides[2] = sideC;
        try{ validateTriangle(sides);}catch (IllegalArgumentException e){
            sides[2]=oldC;
            logger.error("Shapes.Triangle.java/setSideC: " + e.getMessage());
        }
    }

    private static boolean validateTriangle(Double[] sides) {
        double a = sides[0], b = sides[1], c = sides[2];
        if( a > 0 && b > 0 && c > 0 && a + b > c && a + c > b && b + c > a) return true;
        else{
            logger.error("Shapes.Triangle.java/validateTriangle: Invalid triangle sides: " + a + ", " + b + ", " + c);
            throw new IllegalArgumentException("Invalid triangle sides for sides");
        }
    }

    @Override
    public double getArea() {
        double a = sides[0], b = sides[1], c = sides[2];
        double s = (a + b + c) / 2.0;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    @Override
    public double getPerimeter() {
        return sides[0] + sides[1] + sides[2];
    }

    @Override
    public double getHeight() {
        return (2 * getArea()) / getWidth();
    }

    @Override
    public double getWidth() {
        return sides[0];
    }
}