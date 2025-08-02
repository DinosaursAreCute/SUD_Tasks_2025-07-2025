package Shapes;

public class TestShapes {
    private static final double EPSILON = 0.01;

    // Farbige Logging-Methoden
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String RED = "\u001B[31m";

    private static void logInfo(String msg) {
        System.out.println(CYAN + msg + RESET);
    }
    private static void logSuccess(String msg) {
        System.out.println(GREEN + msg + RESET);
    }
    private static void logWarn(String msg) {
        System.out.println(YELLOW + msg + RESET);
    }
    private static void logError(String msg) {
        System.out.println(RED + msg + RESET);
    }

    public static void main(String[] args) {
        Rectangle baseRect = new Rectangle(7.7, 4.0);
        logInfo(baseRect.name());
        Prism prismRect = new Prism(baseRect, (double)6.0);
        logInfo(prismRect.name());
        logInfo("Basisrechteck: " + String.valueOf(baseRect));
        check("Prism Rechteck Oberfläche", prismRect.getSurfaceArea(), (double)202.0F);
        check("Prism Rechteck Volumen", prismRect.getVolume(), 184.8);
        RegularPolygon basePoly9 = new RegularPolygon(9, (double)3.0F);
        RegularPrism prismPoly9 = new RegularPrism(basePoly9, (double)6.0F);
        prismPoly9.printName();
        prismRect.printName();

        logInfo("Basis 9-Eck: " + String.valueOf(basePoly9));
        check("Prism 9-Eck Oberfläche", prismPoly9.getSurfaceArea(), 273.273);
        check("Prism 9-Eck Volumen", prismPoly9.getVolume(), 333.819);
        Cone cone = new Cone((double)2.5F, (double)6.0F);
        cone.printName();
        logInfo("Kegel: " + String.valueOf(cone));
        check("Kegel Oberfläche", cone.getSurfaceArea(), 70.686);
        check("Kegel Volumen", cone.getVolume(), 39.27);
        RegularPolygon basePoly3 = new RegularPolygon(3, (double)5.0F);
        RegularPyramid pyramid3 = new RegularPyramid(basePoly3, (double)6.0F);

        logInfo("Basis 3-Eck: " + String.valueOf(basePoly3));
        check("Pyramide 3 Oberfläche", pyramid3.getSurfaceArea(), 57.109);
        check("Pyramide 3 Volumen", pyramid3.getVolume(), 21.651);
        RegularPolygon basePoly4 = new RegularPolygon(4, (double)5.0F);
        RegularPyramid pyramid4 = new RegularPyramid(basePoly4, (double)6.0F);
        logInfo("Basis 4-Eck: " + String.valueOf(basePoly4));
        check("Pyramide 4 Oberfläche", pyramid4.getSurfaceArea(), (double)90.0F);
        check("Pyramide 4 Volumen", pyramid4.getVolume(), (double)50.0F);
        RegularPolygon basePoly7 = new RegularPolygon(7, (double)5.0F);
        RegularPyramid pyramid7 = new RegularPyramid(basePoly7, (double)6.0F);
        logInfo("Basis 7-Eck: " + String.valueOf(basePoly7));
        check("Pyramide 7 Oberfläche", pyramid7.getSurfaceArea(), 229.694);
        check("Pyramide 7 Volumen", pyramid7.getVolume(), 181.696);
        Sphere sphere = new Sphere((double)2.5F);
        logInfo("Kugel: " + String.valueOf(sphere));
        check("Kugel Oberfläche", sphere.getSurfaceArea(), 78.54);
        check("Kugel Volumen", sphere.getVolume(), 65.45);
    }
    private static int count_errors = 0;
    private static int count_success = 0;
    private static void check(String name, double actual, double expected) {
        if (Math.abs(actual - expected) < EPSILON) {
            count_success++;
            logSuccess(name + " OK (" + actual + ")");
        } else {
            count_errors++;
            logError(name + " ERROR: expected " + expected + ", got " + actual);
        }
        System.out.println("Successful tests: " + count_success + ", Failed tests: " + count_errors);
    }
}
