//shift worker, paid by hours worked and hourly rate. Only valid IDs start with 3.
package Employees;

public class ShiftWorker extends Employee {
    private int hoursWorked;
    private double hourlyRate;

    public ShiftWorker(int id, String name, double hourlyRate) {
        super(name);
        setId(id);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = 0;
    }
     public ShiftWorker(String name, double hourlyRate) {
        super(name);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = 0;
    }
    public void work() {
        hoursWorked += 8;
    }

    public void work(int hours) {
        if (hours >= 1) hoursWorked += hours;
        else logger.error("Invalid hours worked: " + hours + ". Must be greater than or equal to 1.");
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {

        if(hourlyRate > 0) this.hourlyRate = hourlyRate;
        else logger.error("Invalid hourly rate: " + hourlyRate + ". Must be positive.");
    }

    @Override
    public void setSalary(double salary) {
        logger.warning("Direct salary setting for ShiftWorker is not applicable. Use hourlyRate and hoursWorked.");
    }

    @Override
    public void setId(int id) {
        try {
            if (String.valueOf(id).charAt(0) != '3') {
                throw new IllegalArgumentException("ShiftWorker ID must start with 3");
            }
            if (id < 1000 || id > 9999) throw new IllegalArgumentException("ID must be four digits and positive");
            this.id = id;
        }catch (IllegalArgumentException e) {
            logger.error("Error setting ID: " + e.getMessage());
            throw new IllegalArgumentException("Invalid ID for ShiftWorker", e);
        }
    }

    @Override
    public double getSalary() {
        return hoursWorked * hourlyRate;
    }

    @Override
    public String toString() {
        return String.format("ShiftWorker{id=%d, name='%s', hoursWorked=%d, hourlyRate=%.2f, salary=%.2f}", getId(), getName(), getHoursWorked(), getHourlyRate(), getSalary());
    }
}