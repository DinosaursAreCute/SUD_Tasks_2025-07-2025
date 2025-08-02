package Shapes;

public class RegularPolygon{
    int nSides;
    double sideLength;

    public RegularPolygon(int nSides, double sideLength) {

        setnSides(nSides);
        setSideLength(sideLength);
    }

    public void setnSides(int nSides) {
        if(nSides < 3) throw new IllegalArgumentException("A polygon must have at least 3 sides");
        else this.nSides = nSides;
    }

    public void setSideLength(double sideLength) {
        if(sideLength <= 0) throw new IllegalArgumentException("Side length must be positive");
        else this.sideLength = sideLength;
    }
    public int getNumSides() {
        return nSides;
    }
    public double getArea() {
        return (nSides * sideLength * sideLength) / (4 * Math.tan(Math.PI / nSides));
    }
    public double getPerimeter() {
        return nSides * sideLength;
    }
    public double getSideLength() {
        return sideLength;
    }
    public int getnSides() {
        return nSides;
    }
}
