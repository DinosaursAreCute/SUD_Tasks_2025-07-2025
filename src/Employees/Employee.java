package Employees;
import Utils.Logger;
public abstract class Employee {
    protected int id;
    protected String name;
    protected Logger logger;

    public Employee(String name) {
        this.logger = new Logger(getClass().getSimpleName());
        logger.debug("Initializing employee with name: " + name);
        try {
            setName(name);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating employee: " + e.getMessage());
            throw new IllegalArgumentException("Invalid name for employee creation", e);
        }
    }

    abstract public void setSalary(double salary);
    abstract public void setId(int id);

    public int getId() { return id; }
    public String getName() { return name; }

    public void setName(String name) {
        try {
            logger.debug("Attempting to set name: " + name);
            if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
            String[] parts = name.split(" ");
            if (parts.length == 0) throw new IllegalArgumentException("Name must contain at least one part");
            String capitalizedName = "";
            for (String part : parts) {
                if (!part.isEmpty()) {
                    String capitalizedPart = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
                    capitalizedName += capitalizedPart + " ";
                }
            }
            logger.debug("Capitalized name: " + capitalizedName);
            this.name = capitalizedName.trim();
        } catch (IllegalArgumentException e) {
            logger.error("Error setting name: " + e.getMessage());
            throw e;
        }
    }

    public abstract double getSalary();

    @Override
    public String toString() {
        return String.format("%s{id=%d, name='%s'}", this.getClass().getSimpleName(), id, name);
    }
}
