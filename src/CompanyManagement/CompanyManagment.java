package CompanyManagement;

import Employees.Department;
import Employees.Employee;
import Employees.ShiftWorker;
import Vehicles.Vehicle;

import java.util.ArrayList;

public class CompanyManagment {
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private ArrayList<Department> departments = new ArrayList<>();
    private ArrayList<Employee> employees = new ArrayList<>();
    private ArrayList<ShiftWorker> shiftWorkers = new ArrayList<>();


    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }
    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }
    public void addDepartment(Department department) {
        departments.add(department);
    }

    public void add(Department department) {
        departments.add(department);
    }
    public void add(Vehicle vehicle) {
        vehicles.add(vehicle);
    }
    public void add(Employee employee){
        employees.add(employee);
    }
    public void add(ShiftWorker shiftWorker) {
        shiftWorkers.add(shiftWorker);
    }

    public void remove(Department department) {
        departments.remove(department);
    }

    public void remove(Employee employee) {
        employees.remove(employee);
    }

    public void remove(ShiftWorker shiftWorker) {
        shiftWorkers.remove(shiftWorker);
    }

    public void remove(Vehicle vehicle){
        vehicles.remove(vehicle);
    }


    public ArrayList<ShiftWorker> getShiftWorkers() {
        return shiftWorkers;
    }
    public void setShiftWorkers(ArrayList<ShiftWorker> shiftWorkers) {
        this.shiftWorkers = shiftWorkers;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }
    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public ArrayList<Department> getDepartments() {
        return departments;
    }
    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }

    public void workADay(){
        for (int i = 0; i < shiftWorkers.size(); i++) {
            shiftWorkers.get(i).work(8);
        }
    }
}
