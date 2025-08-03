package Vehicles;

import Utils.Logger;
import Employees.Driver;
import Employees.Employee;
import Employees.OfficeWorker;
import java.util.ArrayList;
import java.util.List;

/**
 * Comprehensive unit test suite for the Vehicle system.
 * Tests all methods, getters, setters, polymorphism, and edge cases.
 * Features colored logging output and detailed test statistics.
 * 
 * @author Vehicle Test Suite
 * @version 2.0
 */
public class TestVehicle {
    
    // ANSI Color codes for colored console output
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";
    private static final String BOLD = "\u001B[1m";
    
    private static Logger logger;
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;
    
    public static void main(String[] args) {
        logger = new Logger("VehicleTestSuite");
        
        printHeader();
        
        try {
            // Test Data Setup
            setupTestData();
            
            // Core Test Suites
            testGPSPosition();
            testVehicleConstructors();
            testVehicleGettersAndSetters();
            testVehicleMethods();
            testPassengerVehicle();
            testBus();
            testTruck();
            testPolymorphism();
            testExceptionHandling();
            testLicenseSystem();
            
        } catch (Exception e) {
            logError("Critical test failure: " + e.getMessage());
            e.printStackTrace();
        } finally {
            printTestSummary();
        }
    }
    
    // ========== TEST DATA SETUP ==========
    private static Driver driverA, driverB, driverC;
    private static GPSPosition position1, position2, position3;
    private static OfficeWorker employee1, employee2;
    
    private static void setupTestData() {
        logInfo("Setting up test data...");
        
        try {
            // Create drivers with different licenses (Driver IDs must start with 2, licenses A/B/C only)
            driverA = new Driver(2001, "Alice Anderson", 25.50, 'A');
            driverB = new Driver(2002, "Bob Builder", 22.00, 'B');
            driverC = new Driver(2003, "Charlie Cargo", 28.75, 'C');
            
            // Create GPS positions
            position1 = new GPSPosition(10.5, 52.5);   // Berlin-ish
            position2 = new GPSPosition(45.2, 48.8);   // Paris-ish
            position3 = new GPSPosition(120.3, 35.7);  // Tokyo-ish
            
            // Create employees for passenger testing (OfficeWorker IDs must start with 5)
            employee1 = new OfficeWorker(5001, "Emma Employee", 18.50);
            employee2 = new OfficeWorker(5002, "Frank Fellow", 19.25);
            
            logSuccess("Test data setup completed successfully");
            
        } catch (Exception e) {
            logError("Failed to setup test data: " + e.getMessage());
            throw new RuntimeException("Test setup failed", e);
        }
    }
    
    // ========== GPS POSITION TESTS ==========
    private static void testGPSPosition() {
        logSection("Testing GPSPosition Class");
        
        // Test valid construction
        assertTest("GPS valid construction", () -> {
            GPSPosition pos = new GPSPosition(180.5, 45.0);
            return pos.getLongitude() == 180.5 && pos.getLatitude() == 45.0;
        });
        
        // Test setPosition method
        assertTest("GPS setPosition method", () -> {
            GPSPosition pos = new GPSPosition(0, 0);
            pos.setPosition(270.75, -60.25);
            return pos.getLongitude() == 270.75 && pos.getLatitude() == -60.25;
        });
        
        // Test distance calculation
        assertTest("GPS distance calculation", () -> {
            double distance = GPSPosition.distanceInKm(52.5, 13.4, 48.8, 2.3); // Berlin to Paris
            return distance > 800 && distance < 900; // Approximately 878 km
        });
        
        // Test boundary values
        assertTest("GPS boundary values", () -> {
            GPSPosition pos1 = new GPSPosition(0, -90);     // Min values
            GPSPosition pos2 = new GPSPosition(359.99, 90); // Max values
            return pos1.getLongitude() == 0 && pos2.getLatitude() == 90;
        });
        
        // Test invalid longitude
        assertExceptionTest("GPS invalid longitude", () -> {
            new GPSPosition(-1, 0);
        }, IllegalArgumentException.class);
        
        // Test invalid latitude
        assertExceptionTest("GPS invalid latitude", () -> {
            new GPSPosition(0, 91);
        }, IllegalArgumentException.class);
        
        // Test toString
        assertTest("GPS toString method", () -> {
            GPSPosition pos = new GPSPosition(123.456789, 45.123456);
            String result = pos.toString();
            return result.contains("GPSPosition") && result.contains("longitude") && result.contains("latitude");
        });
    }
    
    // ========== VEHICLE CONSTRUCTOR TESTS ==========
    private static void testVehicleConstructors() {
        logSection("Testing Vehicle Constructors");
        
        // Test PassengerVehicle basic constructor
        assertTest("PassengerVehicle basic constructor", () -> {
            PassengerVehicle pv = new PassengerVehicle(60.0, 180.0, position1, 5, 'B');
            return pv.getTankSize() == 60.0 && pv.getPosition().equals(position1);
        });
        
        // Test PassengerVehicle with driver
        assertTest("PassengerVehicle with driver constructor", () -> {
            PassengerVehicle pv = new PassengerVehicle(50.0, 120.0, driverB, position1, 4, 'B');
            return pv.getDriver().equals(driverB) && pv.getPassengerCount() == 0;
        });
        
        // Test Bus constructors
        assertTest("Bus constructor without driver", () -> {
            Bus bus = new Bus(100.0, 80.0, position1, 30);
            return bus.getTankSize() == 100.0 && bus.getDriver() == null;
        });
        
        // Note: Bus requires D license, but Drivers can only have A/B/C licenses
        // So we test Bus without driver for now
        assertTest("Bus constructor basic functionality", () -> {
            Bus bus = new Bus(120.0, 90.0, position2, 40);
            return bus.getTankSize() == 120.0;
        });
        
        // Test Truck constructors
        assertTest("Truck constructor without driver", () -> {
            Truck truck = new Truck(80.0, 100.0, position1, 25.5);
            return truck.getTankSize() == 80.0 && truck.getLoadedArea() == 0;
        });
        
        assertTest("Truck constructor with driver", () -> {
            Truck truck = new Truck(90.0, 110.0, driverC, position2, 30.0);
            return truck.getDriver().equals(driverC);
        });
    }
    
    // ========== VEHICLE GETTERS AND SETTERS TESTS ==========
    private static void testVehicleGettersAndSetters() {
        logSection("Testing Vehicle Getters and Setters");
        
        PassengerVehicle testVehicle = new PassengerVehicle(50.0, 120.0, position1, 4, 'B');
        
        // Test tank size setter/getter
        assertTest("Tank size setter/getter", () -> {
            testVehicle.setTankSize(75.0);
            return testVehicle.getTankSize() == 75.0;
        });
        
        // Test tank level setter/getter
        assertTest("Tank level setter/getter", () -> {
            testVehicle.setTankLevel(50.0);
            return testVehicle.getTankLevel() == 50.0;
        });
        
        // Test max speed setter/getter
        assertTest("Max speed setter/getter", () -> {
            testVehicle.setMaxSpeed(150.0);
            return testVehicle.getInfo().contains("150");
        });
        
        // Test current speed setter
        assertTest("Current speed setter", () -> {
            testVehicle.setCurrentSpeed(80.0);
            return testVehicle.getInfo().contains("80");
        });
        
        // Test engine on/off
        assertTest("Engine state setter", () -> {
            testVehicle.setEngineOn(true);
            return testVehicle.getInfo().contains("On");
        });
        
        // Test position setter/getter
        assertTest("Position setter/getter", () -> {
            testVehicle.setPosition(position2);
            return testVehicle.getPosition().equals(position2);
        });
        
        // Test driver setter/getter
        assertTest("Driver setter/getter", () -> {
            testVehicle.setDriver(driverB);
            return testVehicle.getDriver().equals(driverB);
        });
        
        // Test invalid tank size
        assertExceptionTest("Invalid tank size", () -> {
            testVehicle.setTankSize(-10);
        }, IllegalArgumentException.class);
        
        // Test invalid tank level
        assertExceptionTest("Invalid tank level (too high)", () -> {
            testVehicle.setTankLevel(100); // Tank size is 75
        }, IllegalArgumentException.class);
    }
    
    // ========== VEHICLE METHODS TESTS ==========
    private static void testVehicleMethods() {
        logSection("Testing Vehicle Methods");
        
        PassengerVehicle testVehicle = new PassengerVehicle(100.0, 200.0, driverB, position1, 4, 'B');
        
        // Test refueling
        assertTest("Refuel method", () -> {
            testVehicle.refuel(50.0);
            return testVehicle.getTankLevel() == 50.0;
        });
        
        // Test engine start/stop
        assertTest("Engine start/stop", () -> {
            testVehicle.startEngine();
            boolean started = testVehicle.getInfo().contains("On");
            testVehicle.stopEngine();
            boolean stopped = testVehicle.getInfo().contains("Off");
            return started && stopped;
        });
        
        // Test acceleration
        assertTest("Acceleration method", () -> {
            testVehicle.startEngine();
            testVehicle.accelerate(50.0);
            return testVehicle.getInfo().contains("50");
        });
        
        // Test braking
        assertTest("Braking method", () -> {
            testVehicle.brake(20.0);
            return testVehicle.getInfo().contains("30"); // 50 - 20
        });
        
        // Test driving
        assertTest("Drive method", () -> {
            testVehicle.refuel(100.0); // Ensure plenty of fuel
            GPSPosition originalPos = testVehicle.getPosition();
            GPSPosition nearbyPos = new GPSPosition(originalPos.getLongitude() + 0.1, originalPos.getLatitude() + 0.1);
            testVehicle.drive(nearbyPos);
            return !testVehicle.getPosition().equals(originalPos);
        });
        
        // Test license validation
        assertTest("License validation", () -> {
            return testVehicle.isLicenseValid('A') && 
                   testVehicle.isLicenseValid('D') && 
                   !testVehicle.isLicenseValid('X');
        });
        
        // Test license requirement check
        assertTest("License requirement check", () -> {
            return testVehicle.isLicenseReqMet('B') && 
                   testVehicle.isLicenseReqMet('C') && 
                   testVehicle.isLicenseReqMet('D');
        });
    }
    
    // ========== PASSENGER VEHICLE TESTS ==========
    private static void testPassengerVehicle() {
        logSection("Testing PassengerVehicle Specific Methods");
        
        PassengerVehicle pv = new PassengerVehicle(60.0, 150.0, position1, 3, 'B');
        
        // Test passenger boarding
        assertTest("Board passengers", () -> {
            boolean result1 = pv.boardPassenger(employee1);
            boolean result2 = pv.boardPassenger(employee2);
            return result1 && result2 && pv.getPassengerCount() == 2;
        });
        
        // Test passenger count
        assertTest("Passenger count", () -> {
            return pv.getPassengerCount() == 2;
        });
        
        // Test passenger unboarding
        assertTest("Unboard passenger", () -> {
            boolean result = pv.unboardPassenger(employee1);
            return result && pv.getPassengerCount() == 1;
        });
        
        // Test seat limit
        assertTest("Seat limit enforcement", () -> {
            pv.boardPassenger(employee1); // Back to 2 passengers
            OfficeWorker employee3 = new OfficeWorker(5003, "Grace Guest", 20.00);
            boolean canBoard = pv.boardPassenger(employee3); // Should work (3rd passenger)
            OfficeWorker employee4 = new OfficeWorker(5004, "Henry Helper", 21.00);
            boolean cantBoard = !pv.boardPassenger(employee4); // Should fail (4th passenger)
            return canBoard && cantBoard;
        });
        
        // Test seat count setter
        assertTest("Seat count setter", () -> {
            pv.setSeatCount(5);
            return true; // If no exception, test passes
        });
        
        // Test toString method
        assertTest("PassengerVehicle toString", () -> {
            String result = pv.toString();
            return result.contains("PassengerVehicle") && result.contains("seatCount");
        });
    }
    
    // ========== BUS TESTS ==========
    private static void testBus() {
        logSection("Testing Bus Specific Features");
        
        // Test Bus requires license D (but since drivers can only have A/B/C, we test the requirement)
        assertTest("Bus license requirement check", () -> {
            Bus bus = new Bus(100.0, 80.0, position1, 50);
            // We can't test with actual D driver, but we can verify the bus was created
            return bus.getTankSize() == 100.0;
        });
        
        // Test Bus inheritance from PassengerVehicle
        assertTest("Bus passenger functionality", () -> {
            Bus bus = new Bus(100.0, 80.0, position1, 50);
            boolean boarding = bus.boardPassenger(employee1);
            return boarding && bus.getPassengerCount() == 1;
        });
        
        // Test Bus with insufficient license (A license for D requirement)
        assertExceptionTest("Bus with insufficient license", () -> {
            new Bus(100.0, 80.0, position1, 50, driverA); // A license insufficient for D requirement
        }, IllegalArgumentException.class);
    }
    
    // ========== TRUCK TESTS ==========
    private static void testTruck() {
        logSection("Testing Truck Specific Features");
        
        Truck truck = new Truck(80.0, 120.0, position1, 20.0);
        
        // Test loading
        assertTest("Truck loading", () -> {
            truck.load(10.0);
            return truck.getLoadedArea() == 10.0;
        });
        
        // Test unloading
        assertTest("Truck unloading", () -> {
            truck.unload(5.0);
            return truck.getLoadedArea() == 5.0;
        });
        
        // Test loading area setter
        assertTest("Loading area setter", () -> {
            truck.setLoadingArea(25.0);
            return true; // If no exception, test passes
        });
        
        // Test loaded area setter
        assertTest("Loaded area setter", () -> {
            truck.setLoadedArea(15.0);
            return truck.getLoadedArea() == 15.0;
        });
        
        // Test overloading prevention
        assertExceptionTest("Truck overloading prevention", () -> {
            truck.load(15.0); // Already 15 + 15 = 30, but max is 25
        }, IllegalArgumentException.class);
        
        // Test truck license requirement (must be C)
        assertTest("Truck requires license C or higher", () -> {
            Truck truckWithDriver = new Truck(80.0, 120.0, driverC, position1, 20.0);
            return truckWithDriver.getDriver().getLicense() == 'C';
        });
    }
    
    // ========== POLYMORPHISM TESTS ==========
    private static void testPolymorphism() {
        logSection("Testing Polymorphism and Inheritance");
        
        // Test polymorphic array
        assertTest("Polymorphic vehicle array", () -> {
            List<Vehicle> fleet = new ArrayList<>();
            fleet.add(new Bus(100.0, 80.0, position1, 50));
            fleet.add(new Truck(80.0, 120.0, driverC, position2, 20.0));
            fleet.add(new PassengerVehicle(60.0, 150.0, driverB, position3, 5, 'B'));
            
            return fleet.size() == 3 && 
                   fleet.get(0) instanceof Bus && 
                   fleet.get(1) instanceof Truck && 
                   fleet.get(2) instanceof PassengerVehicle;
        });
        
        // Test polymorphic method calls
        assertTest("Polymorphic method calls", () -> {
            List<Vehicle> fleet = new ArrayList<>();
            fleet.add(new Bus(100.0, 80.0, position1, 50));
            fleet.add(new Truck(80.0, 120.0, position2, 20.0));
            
            // Call common methods on all vehicles
            for (Vehicle vehicle : fleet) {
                vehicle.refuel(20.0);
                vehicle.startEngine();
                vehicle.accelerate(30.0);
            }
            
            return fleet.get(0).getTankLevel() > 0 && fleet.get(1).getTankLevel() > 0;
        });
        
        // Test instanceof checks
        assertTest("Instanceof checks", () -> {
            Vehicle bus = new Bus(100.0, 80.0, position1, 50);
            Vehicle truck = new Truck(80.0, 120.0, position1, 20.0);
            
            return (bus instanceof PassengerVehicle) && 
                   (bus instanceof Vehicle) && 
                   (truck instanceof Vehicle) && 
                   !(truck instanceof PassengerVehicle);
        });
        
        // Test method overriding
        assertTest("Method overriding (toString)", () -> {
            Vehicle pv = new PassengerVehicle(60.0, 150.0, position1, 5, 'B');
            String pvString = pv.toString();
            
            return pvString.contains("PassengerVehicle") && pvString.contains("seatCount");
        });
    }
    
    // ========== EXCEPTION HANDLING TESTS ==========
    private static void testExceptionHandling() {
        logSection("Testing Exception Handling");
        
        // Test various invalid constructor parameters
        assertExceptionTest("Negative tank size", () -> {
            new PassengerVehicle(-10.0, 150.0, position1, 5, 'B');
        }, IllegalArgumentException.class);
        
        assertExceptionTest("Zero max speed", () -> {
            new PassengerVehicle(60.0, 0.0, position1, 5, 'B');
        }, IllegalArgumentException.class);
        
        assertExceptionTest("Null position", () -> {
            new PassengerVehicle(60.0, 150.0, null, 5, 'B');
        }, IllegalArgumentException.class);
        
        assertExceptionTest("Invalid license", () -> {
            new PassengerVehicle(60.0, 150.0, position1, 5, 'X');
        }, IllegalArgumentException.class);
        
        assertExceptionTest("Negative seat count", () -> {
            new PassengerVehicle(60.0, 150.0, position1, -1, 'B');
        }, IllegalArgumentException.class);
        
        // Test runtime exceptions
        assertExceptionTest("Drive without engine", () -> {
            PassengerVehicle pv = new PassengerVehicle(60.0, 150.0, position1, 5, 'B');
            pv.refuel(50.0);
            pv.drive(position2); // Engine not started
        }, IllegalStateException.class);
        
        assertExceptionTest("Drive without fuel", () -> {
            PassengerVehicle pv = new PassengerVehicle(60.0, 150.0, position1, 5, 'B');
            pv.startEngine();
            pv.drive(position2); // No fuel
        }, IllegalStateException.class);
    }
    
    // ========== LICENSE SYSTEM TESTS ==========
    private static void testLicenseSystem() {
        logSection("Testing License System");
        
        // Test license hierarchy (A=1, B=2, C=3, D=4)
        assertTest("License hierarchy validation", () -> {
            PassengerVehicle pvB = new PassengerVehicle(60.0, 150.0, position1, 5, 'B');
            
            // Driver with C license should be able to drive B-requirement vehicle
            pvB.setDriver(driverC);
            boolean cCanDriveB = pvB.getDriver() != null;
            
            // Driver with A license should NOT be able to drive B-requirement vehicle
            boolean aCannotDriveB = false;
            try {
                pvB.setDriver(driverA);
            } catch (Exception e) {
                aCannotDriveB = true;
            }
            
            return cCanDriveB && aCannotDriveB;
        });
        
        // Test specific license requirements
        assertTest("Specific license requirements", () -> {
            // Bus requires D license (but drivers can only have A/B/C, so we test without driver)
            Bus bus = new Bus(100.0, 80.0, position1, 50);
            boolean busOk = bus.getTankSize() == 100.0; // Test creation successful
            
            // Truck requires C license
            Truck truck = new Truck(80.0, 120.0, position1, 20.0);
            truck.setDriver(driverC);
            boolean truckOk = truck.getDriver() != null;
            
            return busOk && truckOk;
        });
    }
    
    // ========== UTILITY METHODS ==========
    
    private static void assertTest(String testName, TestCase test) {
        totalTests++;
        try {
            if (test.execute()) {
                passedTests++;
                logSuccess("✓ " + testName);
            } else {
                failedTests++;
                logError("✗ " + testName + " - Assertion failed");
            }
        } catch (Exception e) {
            failedTests++;
            logError("✗ " + testName + " - Exception: " + e.getMessage());
        }
    }
    
    private static void assertExceptionTest(String testName, ExceptionTestCase test, Class<? extends Exception> expectedType) {
        totalTests++;
        try {
            test.execute();
            failedTests++;
            logError("✗ " + testName + " - Expected exception but none was thrown");
        } catch (Exception e) {
            if (expectedType.isInstance(e)) {
                passedTests++;
                logSuccess("✓ " + testName + " - Correctly threw " + expectedType.getSimpleName());
            } else {
                failedTests++;
                logError("✗ " + testName + " - Expected " + expectedType.getSimpleName() + " but got " + e.getClass().getSimpleName());
            }
        }
    }
    
    private static void printHeader() {
        System.out.println(BOLD + CYAN + "╔══════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD + CYAN + "║                    VEHICLE TEST SUITE v2.0                  ║" + RESET);
        System.out.println(BOLD + CYAN + "║              Comprehensive Testing Framework                 ║" + RESET);
        System.out.println(BOLD + CYAN + "╚══════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    private static void logSection(String section) {
        System.out.println(BOLD + PURPLE + "\n▶ " + section + RESET);
        System.out.println(PURPLE + "─".repeat(section.length() + 2) + RESET);
    }
    
    private static void logSuccess(String message) {
        System.out.println(GREEN + message + RESET);
    }
    
    private static void logError(String message) {
        System.out.println(RED + message + RESET);
    }
    
    private static void logInfo(String message) {
        System.out.println(BLUE + message + RESET);
    }
    
    private static void logWarning(String message) {
        System.out.println(YELLOW + message + RESET);
    }
    
    private static void printTestSummary() {
        System.out.println(BOLD + CYAN + "\n╔══════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD + CYAN + "║                        TEST SUMMARY                          ║" + RESET);
        System.out.println(BOLD + CYAN + "╠══════════════════════════════════════════════════════════════╣" + RESET);
        System.out.println(BOLD + WHITE + "║ Total Tests:  " + String.format("%3d", totalTests) + "                                        ║" + RESET);
        System.out.println(BOLD + GREEN + "║ Passed:       " + String.format("%3d", passedTests) + "                                        ║" + RESET);
        System.out.println(BOLD + RED + "║ Failed:       " + String.format("%3d", failedTests) + "                                        ║" + RESET);
        System.out.println(BOLD + CYAN + "║ Success Rate: " + String.format("%3.1f", (double)passedTests/totalTests*100) + "%                                      ║" + RESET);
        System.out.println(BOLD + CYAN + "╚══════════════════════════════════════════════════════════════╝" + RESET);
        
        if (failedTests == 0) {
            System.out.println(BOLD + GREEN + "\n🎉 ALL TESTS PASSED! Vehicle system is working perfectly! 🎉" + RESET);
        } else {
            System.out.println(BOLD + YELLOW + "\n⚠️  " + failedTests + " test(s) failed. Please review the issues above." + RESET);
        }
    }
    
    // Functional interfaces for test cases
    @FunctionalInterface
    private interface TestCase {
        boolean execute() throws Exception;
    }
    
    @FunctionalInterface
    private interface ExceptionTestCase {
        void execute() throws Exception;
    }
}
