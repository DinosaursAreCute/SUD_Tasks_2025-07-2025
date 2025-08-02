# SUD_Tasks: Detailed Function Documentation

 
⚠️Note this was created by AI and may not be 100% accurate.
I could not be bothered to check it. ⚠️
---

## Table of Contents
- [Employees Module](#employees-module)
- [Shapes Module](#shapes-module)
- [Vehicles Module](#vehicles-module)
- [Test Classes](#test-classes)
- [General Notes](#general-notes)

---

## Employees Module

### Department.java
- **Department()**: <⚠️TODO ⚠️>

### Driver.java
- **Driver(int id, String name, double hourlyRate, char license)**: Constructor. Creates a driver with a license.
- **void setLicense(char license)**: Sets the driver's license type.
- **DriverLicense getLicense()**: Returns the driver's license type.

### Employee.java
- **Employee(int id, String name)**: Abstract base class for employees.
- **void setId(int id)**: Sets the employee's ID.
- **void setName(String name)**: Sets the employee's name.

### EmployeeManagement.java
    <⚠️TODO #2 ⚠️>

### Manager.java
- **Manager(int id, String name, double fixedSalary, double bonusPercent)**: Constructor. Creates a manager with a fixed salary and bonus percentage.
- **double getBonus(double base)**: Calculates the bonus based on a base salary.

### OfficeWorker.java
- **OfficeWorker(int id, String name, double fixedSalary)**: Constructor. Creates an office worker with a fixed salary.
- **double getSalary()**: Returns the fixed salary.

### ShiftWorker.java
- **ShiftWorker(int id, String name, double hourlyRate)**: Constructor. Creates a shift worker with an hourly rate.
- **void work()**: Increases hours worked by 8.
- **void work(int hours)**: Increases hours worked by a specified amount.

---

## Shapes Module

### Circle.java
- **Circle(double radius)**: Constructor. Creates a circle with a radius.
- **double getArea()**: Returns the area of the circle.
- **double getPerimeter()**: Returns the perimeter of the circle.

### Cone.java
- **Cone(double radius, double height)**: Constructor. Creates a cone with a circular base and height.
- **double getVolume()**: Returns the volume of the cone.

### Cuboid.java
- **Cuboid(double width, double height, double depth)**: Constructor. Creates a cuboid with width, height, and depth.
- **double getVolume()**: Returns the volume of the cuboid.

### Cylinder.java
- **Cylinder(double radius, double height)**: Constructor. Creates a cylinder with a circular base and height.
- **double getVolume()**: Returns the volume of the cylinder.

### Rectangle.java
- **Rectangle(double width, double height)**: Constructor. Creates a rectangle with width and height.
- **double getArea()**: Returns the area of the rectangle.

### RegularPrism.java
- **RegularPrism(int nSides, double sideLength, double height)**: Constructor. Creates a regular prism with a polygon base.
- **double getVolume()**: Returns the volume of the prism.

### RegularPyramid.java
- **RegularPyramid(int nSides, double sideLength, double height)**: Constructor. Creates a regular pyramid with a polygon base.
- **double getVolume()**: Returns the volume of the pyramid.

### Sphere.java
- **Sphere(double radius)**: Constructor. Creates a sphere with a radius.
- **double getVolume()**: Returns the volume of the sphere.

---

## Vehicles Module

### Bus.java
- **Bus(double tankSize, double maxSpeed, GPSPosition position, int seatCount, String driverName)**: Constructor. Creates a bus with a driver name.
- **void setDriver(Driver driver)**: Sets the driver for the bus.

### FleetManagement.java
- **void addVehicle(Vehicle v)**: Adds a vehicle to the fleet.
- **boolean removeVehicle(Vehicle v)**: Removes a vehicle from the fleet.

### GPSPosition.java
- **GPSPosition(double longitude, double latitude)**: Constructor. Creates a GPS position with longitude and latitude.
- **void setPosition(double longitude, double latitude)**: Sets the GPS position.

### PassengerVehicle.java
- **PassengerVehicle(double tankSize, double maxSpeed, GPSPosition position, int seatCount)**: Constructor. Creates a passenger vehicle with seat count.

### Truck.java
- **Truck(double tankSize, double maxSpeed, GPSPosition position, double loadingArea)**: Constructor. Creates a truck with a loading area.
- **void load(double area)**: Loads cargo into the truck.

### Vehicle.java
- **Vehicle(double tankSize, double maxSpeed, GPSPosition position)**: Abstract base class for vehicles.
- **void refuel(double amount)**: Refuels the vehicle.

---

## Test Classes

### TestDepartment.java
- **void main(String[] args)**: Tests the Department class.

### TestEmployee.java
- **void main(String[] args)**: Tests the Employee inheritance hierarchy.

### TestShapes.java
- **void main(String[] args)**: Tests the Shapes module.

### TestVehicle.java
- **void main(String[] args)**: Tests the Vehicles module.

---

## General Notes
- All classes follow standard Java conventions and are organized by topic.
- Abstract classes and interfaces are used for extensibility and code reuse.
- Test classes provide simple demonstrations and can be run directly.

---

If you need more details about a specific class or function, please refer to the source code or request a focused breakdown.
