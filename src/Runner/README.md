# Company Management System Runner

## Overview

The Company Management System Runner is a comprehensive testing and management interface for the Java class hierarchy system. It provides both GUI and command-line interfaces for interacting with all system components.

## Features

### GUI Version (Swing-based)
- **Employee Management**: Create, update, and manage different types of employees
  - Office Workers (ID: 5xxx)
  - Shift Workers (ID: 3xxx) 
  - Drivers (ID: 2xxx)
  - Managers (extends Office Workers)

- **Vehicle Management**: Create and manage fleet vehicles
  - Trucks (require license C)
  - Buses (require license D)
  - Passenger Vehicles (require license B)

- **Shape Management**: Create and test geometric shapes
  - 2D Shapes: Circle, Rectangle, Triangle, Square
  - 3D Shapes: Sphere, Cylinder, Cone, Cuboid

- **Department Management**: Organize employees into departments
  - Assign managers as department heads
  - Add/remove employees from departments
  - Calculate total department salaries

- **Test Runner**: Execute automated tests
  - Individual test classes
  - Complete test suite
  - Performance monitoring
  - Export test results

### Command-Line Version
- Interactive menu-driven interface
- Run individual test modules
- Create sample objects
- Execute complete test suite

## Installation & Usage

### GUI Mode (Default)
```bash
# Navigate to the src directory
cd src

# Compile the Runner classes
javac -cp . Runner/*.java Runner/ui/*.java

# Run the GUI application
java -cp . Runner.RunnerLauncher
```

### Command-Line Mode
```bash
# Run in command-line mode
java -cp . Runner.RunnerLauncher cmd
```

## Architecture

### Main Components

1. **MainApplicationSwing**: Main GUI application window with tabbed interface
2. **EmployeeManagerUI**: Employee creation and management interface
3. **VehicleManagerUI**: Vehicle fleet management interface  
4. **ShapeManagerUI**: Geometric shape testing interface
5. **DepartmentManagerUI**: Department organization interface
6. **TestRunnerUI**: Automated testing interface
7. **RunnerLauncher**: Entry point for both GUI and CLI modes

### Design Patterns Used

- **MVC Pattern**: Separation of UI and business logic
- **Factory Pattern**: Object creation for different types
- **Observer Pattern**: UI updates based on data changes
- **Command Pattern**: Action handling and event processing

## System Requirements

- Java 8 or higher
- Swing library (included in standard JRE)
- All prerequisite classes from the company management system

## Class Dependencies

### Required Packages:
- `Employees.*` - Employee hierarchy classes
- `Vehicles.*` - Vehicle management classes
- `Shapes.*` - Geometric shape classes
- `Utils.Logger` - Logging utility

### Test Classes:
- `Employees.TestEmployee`
- `Employees.TestDepartment`
- `Shapes.TestShapes`
- `Vehicles.TestVehicle`
- `CompanyManagement.CompanyManagementTest`

## Features in Detail

### Employee Management
- **ID Validation**: Automatic validation based on employee type
- **Inheritance Testing**: Polymorphism demonstrations
- **Salary Calculations**: Different calculation methods per type
- **Error Handling**: Comprehensive validation with rollback

### Vehicle Management
- **GPS Integration**: Location-based functionality
- **Driver Assignment**: License validation and assignment
- **Fleet Operations**: Refueling, maintenance tracking
- **Real-time Updates**: Live status monitoring

### Shape Calculations
- **Geometric Validation**: Triangle inequality, positive dimensions
- **Precision Calculations**: High-precision area and volume calculations
- **Interactive Testing**: Real-time calculation updates
- **Mathematical Accuracy**: Proper handling of π and geometric formulas

### Department Operations
- **Hierarchical Management**: Manager assignment and switching
- **Employee Assignment**: Drag-and-drop employee management
- **Financial Reporting**: Salary aggregation and reporting
- **Organizational Charts**: Visual department structure

### Test Automation
- **Background Execution**: Non-blocking test execution
- **Progress Monitoring**: Real-time progress bars and status
- **Result Capture**: Complete output capture and logging
- **Performance Metrics**: Execution time and memory usage
- **Export Functionality**: Save results to files

## Troubleshooting

### Common Issues

1. **ClassNotFoundException**: Ensure all prerequisite classes are compiled
2. **GUI Not Starting**: Check Java version and Swing availability
3. **Test Failures**: Verify all test classes have main methods
4. **Memory Issues**: Monitor heap usage during performance tests

### Error Handling

- All operations include comprehensive try-catch blocks
- User-friendly error dialogs with technical details
- Logging system captures all errors for debugging
- Automatic recovery from non-fatal errors

## Future Enhancements

### Planned Features
- Database integration for persistent storage
- Network-based multi-user access
- Advanced reporting and analytics
- Configuration management system
- Plugin architecture for extensions

### Performance Optimizations
- Lazy loading for large datasets
- Background processing for heavy operations
- Caching for frequently accessed data
- Memory optimization for large object collections

## Development Standards

### Code Quality
- Comprehensive JavaDoc documentation
- Consistent naming conventions
- Error handling best practices
- Design pattern implementation
- SOLID principles adherence

### Testing Standards
- Unit test coverage for all components
- Integration testing for system interactions
- Performance benchmarking
- User interface testing
- Error condition testing

## Contributing

This system follows enterprise-grade Java development standards:

1. **Code Style**: Standard Java conventions
2. **Documentation**: Complete JavaDoc for all public methods
3. **Error Handling**: Comprehensive exception management
4. **Logging**: Structured logging with Utils.Logger
5. **Testing**: Automated test coverage

## License

This software is part of the Company Management System educational project.
Developed following senior Java developer best practices and standards.

---

*For technical support or questions, refer to the comprehensive logging system output or the inline documentation.*
