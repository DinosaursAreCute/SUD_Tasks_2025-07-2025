//department with a name, a list of employees, and a manager as head.
package Employees;

import java.util.ArrayList;

public class Department {
    private String name;
    private ArrayList<Employees.Employee> employees = new ArrayList<>();
    private Employees.Manager head;

    public Department(String name, ArrayList<Employees.Employee> employees, Employees.Manager head) {
        setEmployees(employees);
        setHead(head);
        setName(name);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Employees.Employee> getEmployees() {
        return employees;
    }

    public Employees.Manager getHead() {
        return head;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmployees(ArrayList<Employees.Employee> employees) {
        this.employees = employees;
    }

    public void setHead(Employees.Manager head) {
        this.head = head;
    }
    public Employees.Manager switchHead(Employees.Manager newHead){
        Manager oldHead = head;
        setHead(newHead);
        return oldHead;
    }

    public void addEmployee(Employees.Employee employee) {
       employees.add(employee);
    }
    public void removeEmployee(Employees.Employee employee) {
        employees.remove(employee);
    }
    public double getTotalSalary() {
        double totalSalary = 0;
        for (Employee employee : employees) {
            totalSalary += employee.getSalary();
        }
        totalSalary += head.getSalary();
        return totalSalary;
    }

    @Override
    public String toString() {
        return String.format("Department{name='%s', head=%s, employees=%s}", name, head != null ? head.getName() : "none", employees);
    }
}
