package Employees;

import Utils.Logger;
import java.util.ArrayList;

public class TestDepartment {
    private static Logger logger = new Logger("TestDepartment");

    private static void logInfo(String msg) { 
        logger.info(msg);
    }

    private static void logSuccess(String msg) { 
        System.out.println(Logger.GREEN + msg + Logger.RESET);
    }

    private static void logWarn(String msg) { 
        logger.warning(msg);
    }

    private static void logError(String msg) { 
        logger.error(msg);
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
        // Create employees
        Employees.OfficeWorker ow = new OfficeWorker(5001, "alice example", 3000);
        ow.setSalary(3000);
        Employees.ShiftWorker sw = new ShiftWorker(3001, "bob example", 20);
        sw.work(40);
        Employees.Manager m = new Employees.Manager(5002, "eve example", 4000);
        m.setSalary(4000);
        m.setBonus(0.1);

        // Create department
        ArrayList<Employees.Employee> emps = new ArrayList<>();
        emps.add(ow);
        emps.add(sw);
        Employees.Department dep = new Department("IT", emps, m);

        // Test methods
        logInfo("Department: " + dep.getName());
        logInfo("Head: " + dep.getHead().getName());
        logInfo("Total Salary: " + dep.getTotalSalary());
        logSuccess("Department toString: " + dep);
        logSuccess("Manager toString: " + m);
        logSuccess("OfficeWorker toString: " + ow);
        logSuccess("ShiftWorker toString: " + sw);

        // Add/remove employees
        Employees.Driver d = new Driver(2002, "carol example", 25, 'C');
        d.work(10);
        dep.addEmployee(d);
        logInfo("Employees after adding: " + dep.getEmployees().size());
        logSuccess("Driver toString: " + d);
        dep.removeEmployee(sw);
        logInfo("Employees after removing: " + dep.getEmployees().size());

        // Switch head
        Employees.Manager m2 = new Manager(5003, "frank example", 4200);
        m2.setSalary(4200);
        dep.switchHead(m2);
        logInfo("New head: " + dep.getHead().getName());
        logSuccess("New head toString: " + m2);

        // Polymorphism test
        for (Employee e : dep.getEmployees()) {
            logInfo("Type: " + e.getClass().getSimpleName() + ", Name: " + e.getName() + ", Salary: " + e.getSalary());
            if (e instanceof Driver) {
                logInfo("Driver license: " + ((Driver) e).getLicense());
            }
            logSuccess("toString: " + e);
        }
        logSuccess("All employees successfully tested.");
    }
}
