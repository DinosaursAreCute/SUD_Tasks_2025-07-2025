//driver, a shift worker with a license. Only valid IDs start with 2.
package Employees;
public class Driver extends ShiftWorker {
    private char license;
    public Driver(int id, String name, double hourlyRate, char license) {
        super(name, hourlyRate);
        logger.debug("Creating driver with ID: " + id + ", Name: " + name + ", Hourly Rate: " + hourlyRate + ", License: " + license);
        setId(id);
        setLicense(license);
    }
    @Override
    public void setId(int id) {
        try {
            if (String.valueOf(id).charAt(0) != '2') {
                throw new IllegalArgumentException("Driver ID must start with 2");
            }
            if (id < 1000 || id > 9999) throw new IllegalArgumentException("ID must be four digits and positive");
            this.id = id;
        } catch (IllegalArgumentException e) {
            logger.error("Error setting ID: " + e.getMessage());
            throw new IllegalArgumentException("Invalid ID for Driver", e);
        }
    }
    public void setLicense(char license) {
        try {
            if (license != 'A' && license != 'B' && license != 'C' && license != 'D') {
                throw new IllegalArgumentException("Invalid license type. Must be A, B, C, or D.");
            }
            else  this.license = license;
        } catch (IllegalArgumentException e) {
            logger.error("Error setting license: " + e.getMessage());
            throw new IllegalArgumentException("Invalid license for Driver", e);
        }
    }

    public char getLicense() { return license; }
}
