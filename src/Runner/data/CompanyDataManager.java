package Runner.data;

import Employees.*;
import CompanyManagement.*;
import Vehicles.*;
import Utils.Logger;

import java.io.*;
import java.util.*;

/**
 * Manages persistent storage and loading of company data.
 * Handles employees, vehicles, departments, and other company assets.
 * 
 * @author Senior Java Developer
 * @version 1.0
 */
public class CompanyDataManager {
    private static final Logger logger = new Logger("CompanyDataManager");
    private static final String DATA_DIR = "Runner/data/";
    private static final String EMPLOYEES_FILE = DATA_DIR + "employees.csv";
    private static final String DEPARTMENTS_FILE = DATA_DIR + "departments.csv";
    private static final String VEHICLES_FILE = DATA_DIR + "vehicles.csv";
    
    private CompanyManagment companyManagement;
    
    /**
     * Constructor for CompanyDataManager.
     */
    public CompanyDataManager(CompanyManagment companyManagement) {
        this.companyManagement = companyManagement;
        logger.info("CompanyDataManager initialized");
    }
    
    /**
     * Loads all company data from files on startup.
     */
    public void loadAllData() {
        logger.info("Loading all company data from files");
        try {
            loadEmployeesFromFile();
            loadDepartmentsFromFile();
            loadVehiclesFromFile();
            
            // Assign employees to departments after all data is loaded
            assignEmployeesToDepartments();
            
            logger.info("All company data loaded successfully");
        } catch (Exception e) {
            logger.error("Failed to load company data: " + e.getMessage());
            // If loading fails, generate sample data
            generateSampleData();
        }
    }
    
    /**
     * Saves all company data to files.
     */
    public void saveAllData() {
        logger.info("Saving all company data to files");
        try {
            saveEmployeesToFile();
            saveDepartmentsToFile();
            saveVehiclesToFile();
            logger.info("All company data saved successfully");
        } catch (Exception e) {
            logger.error("Failed to save company data: " + e.getMessage());
        }
    }
    
    /**
     * Loads employees from CSV file.
     */
    private void loadEmployeesFromFile() throws IOException {
        File file = new File(EMPLOYEES_FILE);
        if (!file.exists()) {
            logger.info("Employees file not found, will generate sample data");
            return;
        }
        
        logger.info("Loading employees from " + EMPLOYEES_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String type = parts[0].trim();
                    String name = parts[1].trim();
                    int id = Integer.parseInt(parts[2].trim());
                    double salary = Double.parseDouble(parts[3].trim());
                    
                    Employee employee = createEmployeeFromData(type, name, id, salary, parts);
                    if (employee != null) {
                        companyManagement.add(employee);
                    }
                }
            }
        }
        logger.info("Employees loaded successfully");
    }
    
    /**
     * Creates an employee object from CSV data.
     */
    private Employee createEmployeeFromData(String type, String name, int id, double salary, String[] allParts) {
        try {
            switch (type.toUpperCase()) {
                case "OFFICE":
                    return new OfficeWorker(id, name, salary);
                    
                case "SHIFT":
                    if (allParts.length >= 5) {
                        double hourlyRate = Double.parseDouble(allParts[4].trim());
                        return new ShiftWorker(id, name, hourlyRate);
                    }
                    return new ShiftWorker(id, name, 25.0);
                    
                case "DRIVER":
                    if (allParts.length >= 6) {
                        double hourlyRate = Double.parseDouble(allParts[4].trim());
                        String licenseStr = allParts[5].trim();
                        char license = licenseStr.length() > 0 ? licenseStr.charAt(0) : 'B';
                        return new Driver(id, name, hourlyRate, license);
                    }
                    return new Driver(id, name, 30.0, 'B');
                    
                case "MANAGER":
                    return new Manager(id, name, salary);
                    
                default:
                    logger.warning("Unknown employee type: " + type);
                    return null;
            }
        } catch (Exception e) {
            logger.error("Failed to create employee from data: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Loads departments from CSV file.
     */
    private void loadDepartmentsFromFile() throws IOException {
        File file = new File(DEPARTMENTS_FILE);
        if (!file.exists()) {
            logger.info("Departments file not found, will generate sample data");
            return;
        }
        
        logger.info("Loading departments from " + DEPARTMENTS_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String name = parts[0].trim();
                    String managerName = parts[1].trim();
                    
                    // Find manager by name
                    Manager manager = findManagerByName(managerName);
                    if (manager != null) {
                        Department dept = new Department(name, new ArrayList<>(), manager);
                        companyManagement.add(dept);
                    } else {
                        logger.warning("Manager not found for department: " + name);
                    }
                }
            }
        }
        logger.info("Departments loaded successfully");
    }
    
    /**
     * Loads vehicles from CSV file.
     */
    private void loadVehiclesFromFile() throws IOException {
        File file = new File(VEHICLES_FILE);
        if (!file.exists()) {
            logger.info("Vehicles file not found, will generate sample data");
            return;
        }
        
        logger.info("Loading vehicles from " + VEHICLES_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String type = parts[0].trim();
                    String brand = parts[1].trim();
                    String model = parts[2].trim();
                    int year = Integer.parseInt(parts[3].trim());
                    int capacityOrSeats = Integer.parseInt(parts[4].trim());
                    
                    switch (type.toUpperCase()) {
                        case "TRUCK":
                            Truck truck = new Truck(brand, model, year, capacityOrSeats);
                            companyManagement.add(truck);
                            break;
                        case "BUS":
                            Bus bus = new Bus(brand, model, year, capacityOrSeats);
                            companyManagement.add(bus);
                            break;
                        case "PASSENGER":
                            PassengerVehicle passenger = new PassengerVehicle(brand, model, year, capacityOrSeats);
                            companyManagement.add(passenger);
                            break;
                        default:
                            logger.warning("Unknown vehicle type: " + type);
                    }
                }
            }
        }
        logger.info("Vehicles loaded successfully");
    }
    
    /**
     * Finds a manager by name in the loaded employees.
     */
    private Manager findManagerByName(String name) {
        for (Employee emp : companyManagement.getEmployees()) {
            if (emp instanceof Manager && emp.getName().equals(name)) {
                return (Manager) emp;
            }
        }
        return null;
    }
    
    /**
     * Saves employees to CSV file.
     */
    private void saveEmployeesToFile() throws IOException {
        logger.info("Saving employees to " + EMPLOYEES_FILE);
        try (PrintWriter writer = new PrintWriter(new FileWriter(EMPLOYEES_FILE))) {
            writer.println("Type,Name,ID,Salary,HourlyRate,License");
            
            for (Employee emp : companyManagement.getEmployees()) {
                StringBuilder line = new StringBuilder();
                
                if (emp instanceof Manager) {
                    line.append("MANAGER,").append(emp.getName()).append(",")
                        .append(emp.getId()).append(",").append(emp.getSalary()).append(",,");
                } else if (emp instanceof Driver) {
                    Driver driver = (Driver) emp;
                    line.append("DRIVER,").append(emp.getName()).append(",")
                        .append(emp.getId()).append(",").append(emp.getSalary())
                        .append(",").append(driver.getHourlyRate()).append(",")
                        .append(driver.getLicense());
                } else if (emp instanceof ShiftWorker) {
                    ShiftWorker sw = (ShiftWorker) emp;
                    line.append("SHIFT,").append(emp.getName()).append(",")
                        .append(emp.getId()).append(",").append(emp.getSalary())
                        .append(",").append(sw.getHourlyRate()).append(",");
                } else if (emp instanceof OfficeWorker) {
                    line.append("OFFICE,").append(emp.getName()).append(",")
                        .append(emp.getId()).append(",").append(emp.getSalary()).append(",,");
                }
                
                writer.println(line.toString());
            }
        }
        logger.info("Employees saved successfully");
    }
    
    /**
     * Saves departments to CSV file.
     */
    private void saveDepartmentsToFile() throws IOException {
        logger.info("Saving departments to " + DEPARTMENTS_FILE);
        try (PrintWriter writer = new PrintWriter(new FileWriter(DEPARTMENTS_FILE))) {
            writer.println("Name,Manager");
            
            for (Department dept : companyManagement.getDepartments()) {
                writer.println(dept.getName() + "," + dept.getHead().getName());
            }
        }
        logger.info("Departments saved successfully");
    }
    
    /**
     * Saves vehicles to CSV file.
     */
    private void saveVehiclesToFile() throws IOException {
        logger.info("Saving vehicles to " + VEHICLES_FILE);
        try (PrintWriter writer = new PrintWriter(new FileWriter(VEHICLES_FILE))) {
            writer.println("Type,Brand,Model,Year,Capacity_or_Seats");
            
            for (Object vehicle : companyManagement.getVehicles()) {
                if (vehicle instanceof Truck) {
                    Truck truck = (Truck) vehicle;
                    writer.println("TRUCK," + truck.getBrand() + "," + truck.getModel() + "," + 
                                 truck.getYear() + "," + truck.getCargoCapacity());
                } else if (vehicle instanceof Bus) {
                    Bus bus = (Bus) vehicle;
                    writer.println("BUS," + bus.getBrand() + "," + bus.getModel() + "," + 
                                 bus.getYear() + "," + bus.getSeats());
                } else if (vehicle instanceof PassengerVehicle) {
                    PassengerVehicle pv = (PassengerVehicle) vehicle;
                    writer.println("PASSENGER," + pv.getBrand() + "," + pv.getModel() + "," + 
                                 pv.getYear() + "," + pv.getSeats());
                }
            }
        }
        logger.info("Vehicles saved successfully");
    }
    
    /**
     * Generates sample data for a mid-sized company (50-100 employees).
     */
    public void generateSampleData() {
        logger.info("Generating sample data for mid-sized company");
        
        generateSampleEmployees();
        generateSampleDepartments();
        assignEmployeesToDepartments();
        
        // Save the generated data
        saveAllData();
        
        logger.info("Sample data generation completed");
    }
    
    /**
     * Generates sample employees for the company.
     */
    private void generateSampleEmployees() {
        logger.info("Generating sample employees");
        
        // Employee names for variety
        String[] firstNames = {
            "James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda",
            "William", "Elizabeth", "David", "Barbara", "Richard", "Susan", "Joseph", "Jessica",
            "Thomas", "Sarah", "Christopher", "Karen", "Charles", "Nancy", "Daniel", "Lisa",
            "Matthew", "Betty", "Anthony", "Helen", "Mark", "Sandra", "Donald", "Donna",
            "Steven", "Carol", "Paul", "Ruth", "Andrew", "Sharon", "Kenneth", "Michelle"
        };
        
        String[] lastNames = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
            "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas",
            "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White",
            "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker", "Young"
        };
        
        Random random = new Random();
        Set<String> usedNames = new HashSet<>();
        
        // Generate 15 Managers first
        for (int i = 0; i < 15; i++) {
            String name = generateUniqueName(firstNames, lastNames, random, usedNames);
            int id = 5000 + i;
            double salary = 80000 + (random.nextDouble() * 40000); // $80k-$120k
            
            Manager manager = new Manager(id, name, salary);
            companyManagement.add(manager);
        }
        
        // Generate 25 Office Workers
        for (int i = 0; i < 25; i++) {
            String name = generateUniqueName(firstNames, lastNames, random, usedNames);
            int id = 5100 + i;
            double salary = 40000 + (random.nextDouble() * 30000); // $40k-$70k
            
            OfficeWorker worker = new OfficeWorker(id, name, salary);
            companyManagement.add(worker);
        }
        
        // Generate 30 Shift Workers
        for (int i = 0; i < 30; i++) {
            String name = generateUniqueName(firstNames, lastNames, random, usedNames);
            int id = 3000 + i;
            double hourlyRate = 18.0 + (random.nextDouble() * 12.0); // $18-$30/hr
            
            ShiftWorker worker = new ShiftWorker(id, name, hourlyRate);
            companyManagement.add(worker);
        }
        
        // Generate 20 Drivers
        char[] licenses = {'A', 'B', 'C', 'D'};
        for (int i = 0; i < 20; i++) {
            String name = generateUniqueName(firstNames, lastNames, random, usedNames);
            int id = 2000 + i;
            double hourlyRate = 22.0 + (random.nextDouble() * 15.0); // $22-$37/hr
            char license = licenses[random.nextInt(licenses.length)];
            
            Driver driver = new Driver(id, name, hourlyRate, license);
            companyManagement.add(driver);
        }
        
        logger.info("Generated " + companyManagement.getEmployees().size() + " employees");
    }
    
    /**
     * Generates a unique name combination.
     */
    private String generateUniqueName(String[] firstNames, String[] lastNames, Random random, Set<String> usedNames) {
        String name;
        do {
            name = firstNames[random.nextInt(firstNames.length)] + " " + 
                   lastNames[random.nextInt(lastNames.length)];
        } while (usedNames.contains(name));
        
        usedNames.add(name);
        return name;
    }
    
    /**
     * Generates sample departments for the company.
     */
    private void generateSampleDepartments() {
        logger.info("Generating sample departments");
        
        String[] deptNames = {
            "Human Resources", "Finance", "Marketing", "Sales", "IT Support",
            "Operations", "Customer Service", "Research & Development", "Quality Assurance",
            "Logistics", "Security", "Maintenance", "Training"
        };
        
        // Get available managers
        List<Manager> managers = new ArrayList<>();
        for (Employee emp : companyManagement.getEmployees()) {
            if (emp instanceof Manager) {
                managers.add((Manager) emp);
            }
        }
        
        // Create departments with managers
        int deptCount = Math.min(deptNames.length, managers.size());
        for (int i = 0; i < deptCount; i++) {
            Department dept = new Department(deptNames[i], new ArrayList<>(), managers.get(i));
            companyManagement.add(dept);
        }
        
        logger.info("Generated " + companyManagement.getDepartments().size() + " departments");
    }
    
    /**
     * Assigns employees to departments with realistic distribution.
     */
    private void assignEmployeesToDepartments() {
        logger.info("Assigning employees to departments");
        
        List<Department> departments = companyManagement.getDepartments();
        List<Employee> unassignedEmployees = new ArrayList<>();
        
        // Get all non-manager employees
        for (Employee emp : companyManagement.getEmployees()) {
            if (!(emp instanceof Manager)) {
                unassignedEmployees.add(emp);
            }
        }
        
        // Distribute employees across departments
        Random random = new Random();
        for (Employee emp : unassignedEmployees) {
            if (!departments.isEmpty()) {
                Department dept = departments.get(random.nextInt(departments.size()));
                dept.addEmployee(emp);
            }
        }
        
        logger.info("Employees assigned to departments");
    }
}
