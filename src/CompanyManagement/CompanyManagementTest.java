package CompanyManagement;

import Employees.*;
import Utils.Logger;
import Vehicles.*;
import java.util.ArrayList;
public class CompanyManagementTest {
    private static Logger testLogger = new Logger("CompanyManagementTest");

    private static void logInfo(String msg) { testLogger.info(msg); }
    private static void logSuccess(String msg) { 
        System.out.println(Logger.GREEN + msg + Logger.RESET); 
    }
    private static void logWarn(String msg) { testLogger.warning(msg); }
    private static void logError(String msg) { testLogger.error(msg); }

    public static void main(String[] args) {
        Logger logger = new Logger("CompanyManagementTest");
        logger.info("Starting CompanyManagementTest...");
        CompanyManagment cm = new CompanyManagment();

        GPSPosition gps = new GPSPosition(10, 10);
        logger.debug("GPSPosition created: " + gps);
        GPSPosition gps2 = new GPSPosition(20, 20);
        logger.debug("GPSPosition created: " + gps2);
        // Create and test vehicles
        Vehicle v1 = new Vehicles.Truck(100, 120, gps, 2000);
        logger.debug("Truck created: " + v1);
        Vehicle v2 = new Vehicles.Bus(150, 100, gps2, 50);
        logger.debug("Bus created: " + v2);
        cm.addVehicle(v1);
        cm.addVehicle(v2);
        logSuccess("Vehicles added: " + cm.getVehicles());
        cm.removeVehicle(v1);
        logInfo("Vehicle removed, remaining: " + cm.getVehicles());

        // Create and test departments
        logger.debug("Creating department with manager and office worker...");
        Manager m = new Manager(5001, "Alice Manager", 4000);
        logger.debug("Manager created: " + m);
        m.setSalary(4000);
        logger.debug("Manager salary set: " + m.getSalary());
        ArrayList<Employee> emps = new ArrayList<>();

        OfficeWorker ow = new OfficeWorker(5002, "Bob Office", 3000);
        logger.debug("OfficeWorker created: " + ow);
        emps.add(ow);
        Department dep = new Department("IT", emps, m);
        cm.addDepartment(dep);
        logSuccess("Department added: " + cm.getDepartments());
        cm.remove(dep);
        logInfo("Department removed, remaining: " + cm.getDepartments());

        // Create and test employees
        ShiftWorker sw = new ShiftWorker(3001, "Charlie Shift", 20);
        logger.debug("ShiftWorker created: " + sw);
        cm.add(ow);
        cm.add(sw);
        logSuccess("Employees added: " + cm.getEmployees());
        cm.remove(ow);
        logInfo("Employee removed, remaining: " + cm.getEmployees());

        // Create and test shift workers
        cm.add(sw);
        logSuccess("Shift workers added: " + cm.getShiftWorkers());
        cm.remove(sw);
        logInfo("Shift worker removed, remaining: " + cm.getShiftWorkers());

        // Test setter methods
        ArrayList<Vehicle> newVehicles = new ArrayList<>();
        newVehicles.add(v2);
        cm.setVehicles(newVehicles);
        logInfo("Vehicles set: " + cm.getVehicles());

        ArrayList<Employee> newEmployees = new ArrayList<>();
        newEmployees.add(ow);
        cm.setEmployees(newEmployees);
        logInfo("Employees set: " + cm.getEmployees());

        ArrayList<Department> newDepartments = new ArrayList<>();
        newDepartments.add(dep);
        cm.setDepartments(newDepartments);
        logInfo("Departments set: " + cm.getDepartments());

        ArrayList<ShiftWorker> newShiftWorkers = new ArrayList<>();
        newShiftWorkers.add(sw);
        cm.setShiftWorkers(newShiftWorkers);
        logInfo("Shift workers set: " + cm.getShiftWorkers());

        // Test workADay
        sw.setHoursWorked(0);
        cm.workADay();
        logSuccess("After workADay: Shift worker hours = " + sw.getHoursWorked());

        logSuccess("Company management test finished.");
    }
}

