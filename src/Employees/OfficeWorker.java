//office worker with a fixed salary. Only valid IDs start with 5.
package Employees;

public class OfficeWorker extends Employee {
    private double salary;

    public OfficeWorker(int id, String name, double fixedSalary) {
        super(name);
        setId(id);
        setSalary(fixedSalary);
    }

    @Override
    public double getSalary() {
        return salary;
    }

    @Override
    public void setSalary(double salary) {
        try {
            if (salary < 0) {
                throw new IllegalArgumentException("Salary cannot be negative");
            }
            this.salary = salary;
        } catch (IllegalArgumentException e) {
            logger.error("Error setting salary: " + e.getMessage());
        }
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void setId(int id) {
        try {
            if (String.valueOf(id).charAt(0) != '5') {
                throw new IllegalArgumentException("OfficeWorker ID must start with 5");
            }
            if (id < 1000 || id > 9999)
                throw new IllegalArgumentException("ID must be four digits and positive");
            this.id = id;
        } catch (IllegalArgumentException e) {
            logger.error("Error setting ID: " + e.getMessage());
            throw new IllegalArgumentException("Invalid ID for OfficeWorker", e);
        }
    }

    @Override
    public String toString() {
        return String.format("OfficeWorker{id=%d, name='%s', salary=%.2f}", getId(), getName(), getSalary());
    }

}
