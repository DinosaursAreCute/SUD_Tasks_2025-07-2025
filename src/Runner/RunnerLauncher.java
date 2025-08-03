package Runner;

import Runner.ui.MainApplicationSwing;
import Utils.Logger;

/**
 * Main launcher for the Company Management System Runner.
 * Provides entry point for both UI and command-line versions.
 * 
 * @author Senior Java Developer
 * @version 1.0
 */
public class RunnerLauncher {
    private static final Logger logger = new Logger("RunnerLauncher");
    
    /**
     * Main entry point for the Runner application.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        logger.info("Starting Company Management System Runner");
        
        try {
            // Check if command line mode is requested
            if (args.length > 0 && "cmd".equalsIgnoreCase(args[0])) {
                logger.info("Starting in command-line mode");
                launchCmdVersion();
            } else {
                logger.info("Starting in GUI mode");
                launchGuiVersion();
            }
        } catch (Exception e) {
            logger.error("Failed to start Runner application: " + e.getMessage());
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Launches the GUI version of the application.
     */
    private static void launchGuiVersion() {
        try {
            // Set system look and feel
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            logger.warning("Could not set system look and feel: " + e.getMessage());
        }
        
        // Launch GUI on Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                new MainApplicationSwing();
                logger.info("GUI application launched successfully");
            } catch (Exception e) {
                logger.error("Failed to launch GUI application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Launches the command-line version of the application.
     */
    private static void launchCmdVersion() {
        System.out.println("Company Management System - Command Line Runner");
        System.out.println("=".repeat(50));
        System.out.println();
        
        // Command line interface implementation
        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            boolean running = true;
            
            while (running) {
                showMainMenu();
                System.out.print("Enter your choice: ");
                
                String choice = scanner.nextLine().trim();
                
                switch (choice) {
                    case "1":
                        runEmployeeTests();
                        break;
                    case "2":
                        runVehicleTests();
                        break;
                    case "3":
                        runShapeTests();
                        break;
                    case "4":
                        runDepartmentTests();
                        break;
                    case "5":
                        runAllTests();
                        break;
                    case "6":
                        createSampleObjects();
                        break;
                    case "0":
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
                
                if (running) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                }
            }
        } catch (Exception e) {
            logger.error("Error in command line interface: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Shows the main menu for command line interface.
     */
    private static void showMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("COMPANY MANAGEMENT SYSTEM - MAIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Run Employee Tests");
        System.out.println("2. Run Vehicle Tests");
        System.out.println("3. Run Shape Tests");
        System.out.println("4. Run Department Tests");
        System.out.println("5. Run All Tests");
        System.out.println("6. Create Sample Objects");
        System.out.println("0. Exit");
        System.out.println("=".repeat(50));
    }
    
    /**
     * Runs employee-related tests.
     */
    private static void runEmployeeTests() {
        System.out.println("\n=== Running Employee Tests ===");
        
        try {
            // Run TestEmployee main method
            System.out.println("Executing Employees.TestEmployee...");
            Employees.TestEmployee.main(new String[]{});
            
            System.out.println("\nEmployee tests completed successfully.");
            
        } catch (Exception e) {
            System.err.println("Error running employee tests: " + e.getMessage());
            logger.error("Error running employee tests: " + e.getMessage());
        }
    }
    
    /**
     * Runs vehicle-related tests.
     */
    private static void runVehicleTests() {
        System.out.println("\n=== Running Vehicle Tests ===");
        
        try {
            // Create sample vehicles for testing
            System.out.println("Creating test vehicles...");
            
            Vehicles.GPSPosition gps1 = new Vehicles.GPSPosition(10.0, 50.0);
            Vehicles.GPSPosition gps2 = new Vehicles.GPSPosition(20.0, 60.0);
            
            Vehicles.Truck truck = new Vehicles.Truck(100, 120, gps1, 2000);
            System.out.println("Created truck: " + truck);
            
            Vehicles.Bus bus = new Vehicles.Bus(150, 100, gps2, 50);
            System.out.println("Created bus: " + bus);
            
            // Test refueling
            truck.refuel(50);
            System.out.println("Refueled truck, tank level: " + truck.getTankLevel());
            
            System.out.println("\nVehicle tests completed successfully.");
            
        } catch (Exception e) {
            System.err.println("Error running vehicle tests: " + e.getMessage());
            logger.error("Error running vehicle tests: " + e.getMessage());
        }
    }
    
    /**
     * Runs shape-related tests.
     */
    private static void runShapeTests() {
        System.out.println("\n=== Running Shape Tests ===");
        
        try {
            // Test 2D shapes
            System.out.println("Testing 2D shapes...");
            Shapes.Circle circle = new Shapes.Circle(5.0);
            System.out.printf("Circle (r=5): Area=%.2f, Perimeter=%.2f%n", 
                            circle.getArea(), circle.getPerimeter());
            
            Shapes.Rectangle rectangle = new Shapes.Rectangle(4.0, 6.0);
            System.out.printf("Rectangle (4x6): Area=%.2f, Perimeter=%.2f%n", 
                            rectangle.getArea(), rectangle.getPerimeter());
            
            // Test 3D shapes
            System.out.println("\nTesting 3D shapes...");
            Shapes.Sphere sphere = new Shapes.Sphere(3.0);
            System.out.printf("Sphere (r=3): Volume=%.2f, Surface Area=%.2f%n", 
                            sphere.getVolume(), sphere.getSurfaceArea());
            
            Shapes.Cylinder cylinder = new Shapes.Cylinder(2.0, 5.0);
            System.out.printf("Cylinder (r=2, h=5): Volume=%.2f, Surface Area=%.2f%n", 
                            cylinder.getVolume(), cylinder.getSurfaceArea());
            
            System.out.println("\nShape tests completed successfully.");
            
        } catch (Exception e) {
            System.err.println("Error running shape tests: " + e.getMessage());
            logger.error("Error running shape tests: " + e.getMessage());
        }
    }
    
    /**
     * Runs department-related tests.
     */
    private static void runDepartmentTests() {
        System.out.println("\n=== Running Department Tests ===");
        
        try {
            System.out.println("Executing Employees.TestDepartment...");
            Employees.TestDepartment.main(new String[]{});
            
            System.out.println("\nDepartment tests completed successfully.");
            
        } catch (Exception e) {
            System.err.println("Error running department tests: " + e.getMessage());
            logger.error("Error running department tests: " + e.getMessage());
        }
    }
    
    /**
     * Runs all available tests.
     */
    private static void runAllTests() {
        System.out.println("\n=== Running All Tests ===");
        
        runEmployeeTests();
        runVehicleTests();
        runShapeTests();
        runDepartmentTests();
        
        System.out.println("\n=== All Tests Completed ===");
    }
    
    /**
     * Creates sample objects for demonstration.
     */
    private static void createSampleObjects() {
        System.out.println("\n=== Creating Sample Objects ===");
        
        try {
            // Create employees
            System.out.println("Creating employees...");
            Employees.OfficeWorker ow = new Employees.OfficeWorker(5001, "John Doe", 3000);
            Employees.ShiftWorker sw = new Employees.ShiftWorker(3001, "Jane Smith", 20);
            sw.work(40);
            Employees.Driver driver = new Employees.Driver(2001, "Bob Driver", 25, 'B');
            Employees.Manager manager = new Employees.Manager(5002, "Alice Manager", 5000);
            manager.setBonus(0.1);
            
            System.out.println("Created: " + ow);
            System.out.println("Created: " + sw + " (Salary: $" + sw.getSalary() + ")");
            System.out.println("Created: " + driver);
            System.out.println("Created: " + manager + " (Bonus: $" + manager.getBonusAmount() + ")");
            
            // Create shapes
            System.out.println("\nCreating shapes...");
            Shapes.Circle circle = new Shapes.Circle(3.0);
            Shapes.Cuboid cuboid = new Shapes.Cuboid(2, 3, 4);
            
            System.out.printf("Circle: Area=%.2f, Perimeter=%.2f%n", 
                            circle.getArea(), circle.getPerimeter());
            System.out.printf("Cuboid: Volume=%.2f, Surface Area=%.2f%n", 
                            cuboid.getVolume(), cuboid.getSurfaceArea());
            
            // Create vehicles
            System.out.println("\nCreating vehicles...");
            Vehicles.GPSPosition gps = new Vehicles.GPSPosition(15.0, 55.0);
            Vehicles.Truck truck = new Vehicles.Truck(80, 100, gps, 1500);
            
            System.out.println("Created: " + truck);
            
            System.out.println("\nSample objects created successfully.");
            
        } catch (Exception e) {
            System.err.println("Error creating sample objects: " + e.getMessage());
            logger.error("Error creating sample objects: " + e.getMessage());
        }
    }
}
