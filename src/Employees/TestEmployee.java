// File: Employees/TestEmployee.java
package Employees;

public class TestEmployee {
    // Colored logging methods
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

    private static int count_errors = 0;
    private static int count_success = 0;
    private static void check(String name, Object actual, Object expected) {
        if ((actual == null && expected == null) || (actual != null && actual.equals(expected))) {
            count_success++;
            logSuccess(name + " OK (" + actual + ")");
        } else {
            count_errors++;
            logError(name + " ERROR: expected " + expected + ", got " + actual);
        }
        System.out.println("Successful tests: " + count_success + ", Failed tests: " + count_errors);
    }

    public static void main(String[] args) {
        // Polymorphism: Employee reference to subclasses
        Employees.Employee e1 = new Employees.OfficeWorker(5001, "alice example", 3000);
        e1.setSalary(3200);
        Employees.Employee e2 = new Employees.ShiftWorker(3001, "bob example", 20);
        ((Employees.ShiftWorker) e2).work(40);
        Driver e3 = new Employees.Driver(3002, "carol example", 25, 'B');
        e3.work(16);

        // Type check and downcasting
        if (e3 instanceof Employees.Driver) {
            logInfo("Driver license: " + e3.getLicense());
        }

        // Error cases: invalid IDs
        try {
            new OfficeWorker(4001, "fail", 1000);
            check("OfficeWorker invalid ID", false, true); // Should not reach here
        } catch (IllegalArgumentException ex) {
            logWarn("Correctly caught (OfficeWorker): " + ex.getMessage());
            check("OfficeWorker invalid ID exception", ex.getClass().getSimpleName(), "IllegalArgumentException");
        }
        try {
            new ShiftWorker(2001, "fail", 10);
            check("ShiftWorker invalid ID", false, true); // Should not reach here
        } catch (IllegalArgumentException ex) {
            logWarn("Correctly caught (ShiftWorker): " + ex.getMessage());
            check("ShiftWorker invalid ID exception", ex.getClass().getSimpleName(), "IllegalArgumentException");
        }
        // Test methods
        check("OfficeWorker Name", e1.getName(), "Alice Example");
        check("OfficeWorker Salary", e1.getSalary(), 3200.0);
        check("ShiftWorker Name", e2.getName(), "Bob Example");
        check("ShiftWorker Salary", e2.getSalary(), ((Employees.ShiftWorker) e2).getHourlyRate() * 40);
        check("Driver Name", e3.getName(), "Carol Example");
        check("Driver Salary", e3.getSalary(), 25.0 * 16);
        check("Driver License", e3.getLicense(), 'B');
        // toString checks
        logSuccess("OfficeWorker toString: " + e1);
        logSuccess("ShiftWorker toString: " + e2);
        logSuccess("Driver toString: " + e3);
        System.out.println("Successful tests: " + count_success + ", Failed tests: " + count_errors);
    }
}
