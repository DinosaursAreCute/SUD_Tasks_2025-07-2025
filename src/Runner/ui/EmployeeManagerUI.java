package Runner.ui;

import CompanyManagement.CompanyManagment;
import Employees.*;
import Utils.Logger;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Enhanced UI Manager for Employee operations with table view and CompanyManagement integration.
 * Provides interface for creating, managing, and testing employee objects.
 * 
 * @author Senior Java Developer
 * @version 2.0
 */
public class EmployeeManagerUI {
    private static final Logger logger = new Logger("EmployeeManagerUI");
    
    private final MainApplicationSwing parentApp;
    private final CompanyManagment companyManagement;
    
    // UI Components
    private JPanel mainPanel;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> tableSorter;
    private JTextField nameField;
    private JTextField idField;
    private JTextField salaryField;
    private JComboBox<String> employeeTypeCombo;
    private JTextField licenseField;
    private JTextField hourlyRateField;
    private JTextField bonusField;
    private JComboBox<Department> departmentCombo;
    private JTextField searchField;
    
    // Table column indices
    private static final int COL_ID = 0;
    private static final int COL_NAME = 1;
    private static final int COL_TYPE = 2;
    private static final int COL_SALARY = 3;
    private static final int COL_DEPARTMENT = 4;
    private static final int COL_DETAILS = 5;
    
    /**
     * Constructor for EmployeeManagerUI.
     * @param parentApp Reference to the main application
     */
    public EmployeeManagerUI(MainApplicationSwing parentApp) {
        this.parentApp = parentApp;
        this.companyManagement = new CompanyManagment();
        initializeTableModel();
        logger.info("Enhanced EmployeeManagerUI initialized with CompanyManagement integration");
    }
    
    /**
     * Initializes the table model with appropriate columns.
     */
    private void initializeTableModel() {
        String[] columnNames = {"ID", "Name", "Type", "Salary", "Department", "Details"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        tableSorter = new TableRowSorter<>(tableModel);
    }
    
    /**
     * Creates and returns the main content panel for employee management.
     * @return JPanel containing all employee management controls
     */
    public JPanel createContent() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search and filter panel
        JPanel searchPanel = createSearchPanel();
        
        // Create employee table panel
        JPanel tablePanel = createEmployeeTablePanel();
        
        // Create form panel
        JPanel formPanel = createEmployeeFormPanel();
        
        // Create action panel
        JPanel actionPanel = createActionPanel();
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(tablePanel);
        splitPane.setRightComponent(formPanel);
        splitPane.setDividerLocation(600);
        
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Creates the search and filter panel.
     */
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(new TitledBorder("Search & Filter"));
        
        panel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchField.addCaretListener(e -> filterTable());
        panel.add(searchField);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshEmployeeTable());
        panel.add(refreshButton);
        
        JButton showAllButton = new JButton("Show All");
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            refreshEmployeeTable();
        });
        panel.add(showAllButton);
        
        return panel;
    }
    
    /**
     * Creates the employee table panel with enhanced features.
     */
    private JPanel createEmployeeTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("All Employees"));
        
        employeeTable = new JTable(tableModel);
        employeeTable.setRowSorter(tableSorter);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Set column widths
        employeeTable.getColumnModel().getColumn(COL_ID).setPreferredWidth(60);
        employeeTable.getColumnModel().getColumn(COL_NAME).setPreferredWidth(120);
        employeeTable.getColumnModel().getColumn(COL_TYPE).setPreferredWidth(100);
        employeeTable.getColumnModel().getColumn(COL_SALARY).setPreferredWidth(80);
        employeeTable.getColumnModel().getColumn(COL_DEPARTMENT).setPreferredWidth(120);
        employeeTable.getColumnModel().getColumn(COL_DETAILS).setPreferredWidth(150);
        
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
                    Employee employee = getEmployeeFromTableRow(modelRow);
                    if (employee != null) {
                        populateFormFromEmployee(employee);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setPreferredSize(new Dimension(580, 400));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add summary panel
        JPanel summaryPanel = createSummaryPanel();
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates a summary panel showing employee statistics.
     */
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel totalLabel = new JLabel("Total: 0");
        JLabel managersLabel = new JLabel("Managers: 0");
        JLabel driversLabel = new JLabel("Drivers: 0");
        JLabel avgSalaryLabel = new JLabel("Avg Salary: $0");
        
        panel.add(totalLabel);
        panel.add(managersLabel);
        panel.add(driversLabel);
        panel.add(avgSalaryLabel);
        
        return panel;
    }
    
    /**
     * Creates the employee form panel for creating/editing employees.
     */
    private JPanel createEmployeeFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Employee Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Employee Type
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        employeeTypeCombo = new JComboBox<>(new String[]{"OfficeWorker", "ShiftWorker", "Driver", "Manager"});
        employeeTypeCombo.addActionListener(e -> toggleFieldsBasedOnType());
        panel.add(employeeTypeCombo, gbc);
        
        // ID
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(15);
        panel.add(idField, gbc);
        
        // Name
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        panel.add(nameField, gbc);
        
        // Salary
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1;
        salaryField = new JTextField(15);
        panel.add(salaryField, gbc);
        
        // License (for Drivers)
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("License:"), gbc);
        gbc.gridx = 1;
        licenseField = new JTextField(15);
        panel.add(licenseField, gbc);
        
        // Hourly Rate (for ShiftWorkers)
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Hourly Rate:"), gbc);
        gbc.gridx = 1;
        hourlyRateField = new JTextField(15);
        panel.add(hourlyRateField, gbc);
        
        // Bonus (for Managers)
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Bonus (%):"), gbc);
        gbc.gridx = 1;
        bonusField = new JTextField(15);
        panel.add(bonusField, gbc);
        
        // Department
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        departmentCombo = new JComboBox<>();
        departmentCombo.addItem(null); // Allow no department
        panel.add(departmentCombo, gbc);
        
        toggleFieldsBasedOnType();
        return panel;
    }
    
    /**
     * Creates the action panel with buttons.
     */
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton createButton = new JButton("Create Employee");
        createButton.addActionListener(e -> createEmployee());
        panel.add(createButton);
        
        JButton updateButton = new JButton("Update Employee");
        updateButton.addActionListener(e -> updateEmployee());
        panel.add(updateButton);
        
        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.addActionListener(e -> deleteEmployee());
        panel.add(deleteButton);
        
        JButton clearButton = new JButton("Clear Form");
        clearButton.addActionListener(e -> clearForm());
        panel.add(clearButton);
        
        JButton testButton = new JButton("Test Operations");
        testButton.addActionListener(e -> testEmployeeOperations());
        panel.add(testButton);
        
        JButton companyStatsButton = new JButton("Company Statistics");
        companyStatsButton.addActionListener(e -> showCompanyStatistics());
        panel.add(companyStatsButton);
        
        return panel;
    }
    
    /**
     * Toggles form fields visibility based on selected employee type.
     */
    private void toggleFieldsBasedOnType() {
        String selectedType = (String) employeeTypeCombo.getSelectedItem();
        
        licenseField.setEnabled("Driver".equals(selectedType));
        hourlyRateField.setEnabled("ShiftWorker".equals(selectedType) || "Driver".equals(selectedType));
        bonusField.setEnabled("Manager".equals(selectedType));
        
        // Update ID field placeholder based on type
        switch (selectedType) {
            case "OfficeWorker", "Manager" -> idField.setToolTipText("ID must start with 5 (e.g., 5001)");
            case "ShiftWorker" -> idField.setToolTipText("ID must start with 3 (e.g., 3001)");
            case "Driver" -> idField.setToolTipText("ID must start with 2 (e.g., 2001)");
        }
    }
    
    /**
     * Creates a new employee based on form input.
     */
    private void createEmployee() {
        try {
            String type = (String) employeeTypeCombo.getSelectedItem();
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            
            if (name.isEmpty()) {
                parentApp.showWarningDialog("Input Error", "Name cannot be empty");
                return;
            }
            
            Employee employee = switch (type) {
                case "OfficeWorker" -> {
                    double salary = Double.parseDouble(salaryField.getText().trim());
                    yield new OfficeWorker(id, name, salary);
                }
                case "ShiftWorker" -> {
                    double hourlyRate = Double.parseDouble(hourlyRateField.getText().trim());
                    yield new ShiftWorker(id, name, hourlyRate);
                }
                case "Driver" -> {
                    double hourlyRate = Double.parseDouble(hourlyRateField.getText().trim());
                    char license = licenseField.getText().trim().charAt(0);
                    yield new Driver(id, name, hourlyRate, license);
                }
                case "Manager" -> {
                    double salary = Double.parseDouble(salaryField.getText().trim());
                    Manager mgr = new Manager(id, name, salary);
                    if (!bonusField.getText().trim().isEmpty()) {
                        double bonus = Double.parseDouble(bonusField.getText().trim()) / 100.0;
                        mgr.setBonus(bonus);
                    }
                    yield mgr;
                }
                default -> throw new IllegalArgumentException("Unknown employee type: " + type);
            };
            
            // Add to company management
            companyManagement.add(employee);
            
            // Add to department if selected
            Department selectedDept = (Department) departmentCombo.getSelectedItem();
            if (selectedDept != null) {
                selectedDept.addEmployee(employee);
            }
            
            refreshEmployeeTable();
            clearForm();
            
            parentApp.appendToOutput("Created employee: " + employee + "\n");
            logger.info("Employee created: " + employee);
            
        } catch (NumberFormatException e) {
            parentApp.showErrorDialog("Input Error", "Please check numeric values", e);
        } catch (Exception e) {
            parentApp.showErrorDialog("Creation Error", "Failed to create employee", e);
        }
    }
    
    /**
     * Updates the selected employee.
     */
    private void updateEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow < 0) {
            parentApp.showWarningDialog("Selection Error", "Please select an employee to update");
            return;
        }
        
        try {
            int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
            Employee employee = getEmployeeFromTableRow(modelRow);
            
            if (employee != null) {
                // Update basic properties
                String newName = nameField.getText().trim();
                if (!newName.isEmpty()) {
                    employee.setName(newName);
                }
                
                if (!salaryField.getText().trim().isEmpty()) {
                    double newSalary = Double.parseDouble(salaryField.getText().trim());
                    employee.setSalary(newSalary);
                }
                
                // Update type-specific properties
                if (employee instanceof Manager manager && !bonusField.getText().trim().isEmpty()) {
                    double bonus = Double.parseDouble(bonusField.getText().trim()) / 100.0;
                    manager.setBonus(bonus);
                }
                
                refreshEmployeeTable();
                parentApp.appendToOutput("Updated employee: " + employee + "\n");
                logger.info("Employee updated: " + employee);
            }
            
        } catch (Exception e) {
            parentApp.showErrorDialog("Update Error", "Failed to update employee", e);
        }
    }
    
    /**
     * Deletes the selected employee.
     */
    private void deleteEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow < 0) {
            parentApp.showWarningDialog("Selection Error", "Please select an employee to delete");
            return;
        }
        
        int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
        Employee employee = getEmployeeFromTableRow(modelRow);
        
        if (employee != null) {
            int confirm = JOptionPane.showConfirmDialog(
                mainPanel,
                "Are you sure you want to delete employee: " + employee.getName() + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                companyManagement.remove(employee);
                
                // Remove from departments
                for (Department dept : companyManagement.getDepartments()) {
                    dept.removeEmployee(employee);
                }
                
                refreshEmployeeTable();
                clearForm();
                
                parentApp.appendToOutput("Deleted employee: " + employee.getName() + "\n");
                logger.info("Employee deleted: " + employee);
            }
        }
    }
    
    /**
     * Clears the form fields.
     */
    private void clearForm() {
        nameField.setText("");
        idField.setText("");
        salaryField.setText("");
        licenseField.setText("");
        hourlyRateField.setText("");
        bonusField.setText("");
        departmentCombo.setSelectedIndex(0);
        employeeTypeCombo.setSelectedIndex(0);
    }
    
    /**
     * Populates form fields from selected employee.
     */
    private void populateFormFromEmployee(Employee employee) {
        nameField.setText(employee.getName());
        idField.setText(String.valueOf(employee.getId()));
        salaryField.setText(String.valueOf(employee.getSalary()));
        
        if (employee instanceof Manager manager) {
            employeeTypeCombo.setSelectedItem("Manager");
            bonusField.setText(String.valueOf(manager.getBonus() * 100));
        } else if (employee instanceof Driver driver) {
            employeeTypeCombo.setSelectedItem("Driver");
            licenseField.setText(String.valueOf(driver.getLicense()));
            hourlyRateField.setText(String.valueOf(driver.getHourlyRate()));
        } else if (employee instanceof ShiftWorker shiftWorker) {
            employeeTypeCombo.setSelectedItem("ShiftWorker");
            hourlyRateField.setText(String.valueOf(shiftWorker.getHourlyRate()));
        } else if (employee instanceof OfficeWorker) {
            employeeTypeCombo.setSelectedItem("OfficeWorker");
        }
        
        // Find department
        Department employeeDept = findEmployeeDepartment(employee);
        departmentCombo.setSelectedItem(employeeDept);
        
        toggleFieldsBasedOnType();
    }
    
    /**
     * Finds the department that contains the given employee.
     */
    private Department findEmployeeDepartment(Employee employee) {
        for (Department dept : companyManagement.getDepartments()) {
            if (dept.getEmployees().contains(employee)) {
                return dept;
            }
        }
        return null;
    }
    
    /**
     * Refreshes the employee table with current data.
     */
    private void refreshEmployeeTable() {
        tableModel.setRowCount(0);
        
        // Add all employees from company management
        for (Employee employee : companyManagement.getEmployees()) {
            addEmployeeToTable(employee);
        }
        
        // Also add employees from departments (if not already in main list)
        for (Department dept : companyManagement.getDepartments()) {
            for (Employee employee : dept.getEmployees()) {
                if (!companyManagement.getEmployees().contains(employee)) {
                    addEmployeeToTable(employee);
                }
            }
        }
        
        updateDepartmentCombo();
        updateSummaryInfo();
    }
    
    /**
     * Adds an employee to the table.
     */
    private void addEmployeeToTable(Employee employee) {
        Department dept = findEmployeeDepartment(employee);
        String deptName = dept != null ? dept.getName() : "None";
        
        String details = getEmployeeDetails(employee);
        
        Object[] rowData = {
            employee.getId(),
            employee.getName(),
            employee.getClass().getSimpleName(),
            String.format("$%.2f", employee.getSalary()),
            deptName,
            details
        };
        
        tableModel.addRow(rowData);
    }
    
    /**
     * Gets specific details for each employee type.
     */
    private String getEmployeeDetails(Employee employee) {
        if (employee instanceof Manager manager) {
            return String.format("Bonus: %.1f%%, Amount: $%.2f", 
                manager.getBonus() * 100, manager.getBonusAmount());
        } else if (employee instanceof Driver driver) {
            return String.format("License: %c, Hours: %d, Rate: $%.2f", 
                driver.getLicense(), driver.getHoursWorked(), driver.getHourlyRate());
        } else if (employee instanceof ShiftWorker shiftWorker) {
            return String.format("Hours: %d, Rate: $%.2f", 
                shiftWorker.getHoursWorked(), shiftWorker.getHourlyRate());
        } else {
            return "Office Worker";
        }
    }
    
    /**
     * Gets the employee object from a table row.
     */
    private Employee getEmployeeFromTableRow(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) {
            return null;
        }
        
        int id = (Integer) tableModel.getValueAt(row, COL_ID);
        
        // Search in company management
        for (Employee employee : companyManagement.getEmployees()) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        
        // Search in departments
        for (Department dept : companyManagement.getDepartments()) {
            for (Employee employee : dept.getEmployees()) {
                if (employee.getId() == id) {
                    return employee;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Updates the department combo box with current departments.
     */
    private void updateDepartmentCombo() {
        departmentCombo.removeAllItems();
        departmentCombo.addItem(null); // No department option
        
        for (Department dept : companyManagement.getDepartments()) {
            departmentCombo.addItem(dept);
        }
    }
    
    /**
     * Updates summary information panel.
     */
    private void updateSummaryInfo() {
        // This would update the summary panel with current statistics
        // Implementation depends on having access to the summary labels
    }
    
    /**
     * Filters the table based on search text.
     */
    private void filterTable() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            tableSorter.setRowFilter(null);
        } else {
            tableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }
    
    /**
     * Shows company statistics dialog.
     */
    private void showCompanyStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== Company Statistics ===\n\n");
        
        List<Employee> allEmployees = companyManagement.getEmployees();
        stats.append("Total Employees: ").append(allEmployees.size()).append("\n");
        
        long managers = allEmployees.stream().filter(e -> e instanceof Manager).count();
        long drivers = allEmployees.stream().filter(e -> e instanceof Driver).count();
        long shiftWorkers = allEmployees.stream().filter(e -> e instanceof ShiftWorker && !(e instanceof Driver)).count();
        long officeWorkers = allEmployees.stream().filter(e -> e instanceof OfficeWorker && !(e instanceof Manager)).count();
        
        stats.append("Managers: ").append(managers).append("\n");
        stats.append("Drivers: ").append(drivers).append("\n");
        stats.append("Shift Workers: ").append(shiftWorkers).append("\n");
        stats.append("Office Workers: ").append(officeWorkers).append("\n\n");
        
        double totalSalary = allEmployees.stream().mapToDouble(Employee::getSalary).sum();
        double avgSalary = allEmployees.isEmpty() ? 0 : totalSalary / allEmployees.size();
        
        stats.append("Total Salary Cost: $").append(String.format("%.2f", totalSalary)).append("\n");
        stats.append("Average Salary: $").append(String.format("%.2f", avgSalary)).append("\n\n");
        
        stats.append("Departments: ").append(companyManagement.getDepartments().size()).append("\n");
        for (Department dept : companyManagement.getDepartments()) {
            stats.append("  - ").append(dept.getName()).append(": ").append(dept.getEmployees().size()).append(" employees\n");
        }
        
        parentApp.showInfoDialog("Company Statistics", stats.toString());
    }
    
    /**
     * Tests various employee operations.
     */
    private void testEmployeeOperations() {
        parentApp.appendToOutput("=== Testing Employee Operations ===\n");
        
        try {
            // Test OfficeWorker
            parentApp.appendToOutput("Testing OfficeWorker creation...\n");
            OfficeWorker ow = new OfficeWorker(5001, "John Doe", 3000);
            companyManagement.add(ow);
            parentApp.appendToOutput("Created: " + ow + "\n");
            
            // Test ShiftWorker
            parentApp.appendToOutput("Testing ShiftWorker creation...\n");
            ShiftWorker sw = new ShiftWorker(3001, "Jane Smith", 20);
            sw.work(40);
            companyManagement.add(sw);
            parentApp.appendToOutput("Created: " + sw + ", Salary: " + sw.getSalary() + "\n");
            
            // Test Driver
            parentApp.appendToOutput("Testing Driver creation...\n");
            Driver driver = new Driver(2001, "Bob Johnson", 25, 'B');
            companyManagement.add(driver);
            parentApp.appendToOutput("Created: " + driver + "\n");
            
            // Test Manager
            parentApp.appendToOutput("Testing Manager creation...\n");
            Manager manager = new Manager(5002, "Alice Manager", 5000);
            manager.setBonus(0.15);
            companyManagement.add(manager);
            parentApp.appendToOutput("Created: " + manager + ", Bonus Amount: " + manager.getBonusAmount() + "\n");
            
            // Test inheritance and polymorphism
            parentApp.appendToOutput("\nTesting polymorphism...\n");
            Employee[] testEmployees = {ow, sw, driver, manager};
            for (Employee emp : testEmployees) {
                parentApp.appendToOutput("Employee: " + emp.getName() + " - Salary: " + emp.getSalary() + "\n");
            }
            
            refreshEmployeeTable();
            parentApp.appendToOutput("=== Employee Tests Completed ===\n\n");
            
        } catch (Exception e) {
            parentApp.showErrorDialog("Test Error", "Error during employee testing", e);
        }
    }
    
    /**
     * Returns the company management instance.
     */
    public CompanyManagment getCompanyManagement() {
        return companyManagement;
    }

    /**
     * Refreshes the UI components with current data.
     * This method can be called from external components to update the display.
     */
    public void refreshUI() {
        SwingUtilities.invokeLater(() -> {
            refreshEmployeeTable();
            logger.debug("EmployeeManagerUI refreshed from external call");
        });
    }

    /**
     * Resets the manager to initial state.
     */
    public void reset() {
        clearForm();
        tableModel.setRowCount(0);
        // Clear company management data
        companyManagement.getEmployees().clear();
        companyManagement.getDepartments().clear();
        logger.info("EmployeeManagerUI reset to initial state");
    }
}
