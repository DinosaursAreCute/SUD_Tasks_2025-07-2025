package Runner.ui;

import Utils.Logger;
import Runner.data.CompanyDataManager;
import CompanyManagement.CompanyManagment;
import Employees.Department;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Main Swing Application for the Company Management System Runner.
 * Provides a comprehensive UI for testing and managing all system components.
 * 
 * @author Senior Java Developer
 * @version 1.0
 */
public class MainApplicationSwing extends JFrame {
    private static final Logger logger = new Logger("MainApplicationSwing");
    
    // Main application components
    private JTabbedPane mainTabPane;
    private JTextArea outputArea;
    private JScrollPane outputScrollPane;
    
    // UI Controllers for different modules
    private EmployeeManagerUI employeeManager;
    private VehicleManagerUI vehicleManager;
    private ShapeManagerUI shapeManager;
    private DepartmentManagerUI departmentManager;
    private TestRunnerUI testRunner;
    
    // Data management
    private CompanyDataManager dataManager;
    
    // Company metrics labels (for real-time updates)
    private JLabel totalEmployeesLabel;
    private JLabel totalDepartmentsLabel;
    private JLabel totalVehiclesLabel;
    private JLabel totalSalaryLabel;
    
    /**
     * Constructor for the main application UI.
     */
    public MainApplicationSwing() {
        super("Company Management System - Runner v1.0");
        logger.info("Initializing Company Management System Runner UI");
        
        try {
            initializeComponents();
            initializeDataManager();
            setupMainLayout();
            setupMenuBar();
            
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1200, 800);
            setLocationRelativeTo(null);
            setVisible(true);
            
            logger.info("UI application started successfully");
            appendToOutput("=== Company Management System Runner Started ===\n");
            appendToOutput("=== Loading Company Data ===\n");
            
        } catch (Exception e) {
            logger.error("Failed to start UI application: " + e.getMessage());
            showErrorDialog("Application Startup Error", "Failed to initialize the application", e);
        }
    }
    
    /**
     * Initializes all UI components and managers.
     */
    private void initializeComponents() {
        logger.debug("Initializing UI components");
        
        // Initialize output area
        outputArea = new JTextArea(8, 80);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.GREEN);
        
        outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        outputScrollPane.setBorder(new TitledBorder("System Output & Logs"));
        
        // Initialize managers with shared output area
        employeeManager = new EmployeeManagerUI(this);
        vehicleManager = new VehicleManagerUI(this);
        shapeManager = new ShapeManagerUI(this);
        departmentManager = new DepartmentManagerUI(this);
        testRunner = new TestRunnerUI(this);
        
        // Initialize main tab pane
        mainTabPane = new JTabbedPane();
        
        logger.debug("UI components initialized successfully");
    }
    
    /**
     * Initializes the data manager and loads company data.
     */
    private void initializeDataManager() {
        logger.info("Initializing data manager and loading company data");
        
        try {
            // Initialize data manager with the company management from employee manager
            dataManager = new CompanyDataManager(employeeManager.getCompanyManagement());
            
            // Load all company data from files
            dataManager.loadAllData();
            
            appendToOutput("Company data loaded successfully from files\n");
            appendToOutput("Total employees loaded: " + employeeManager.getCompanyManagement().getEmployees().size() + "\n");
            appendToOutput("Total departments loaded: " + employeeManager.getCompanyManagement().getDepartments().size() + "\n");
            appendToOutput("Total vehicles loaded: " + employeeManager.getCompanyManagement().getVehicles().size() + "\n");
            
            // Refresh all UI managers with loaded data
            SwingUtilities.invokeLater(() -> {
                refreshCompanyMetrics();
                refreshAllManagers();
            });
            
        } catch (Exception e) {
            logger.error("Failed to initialize data manager: " + e.getMessage());
            appendToOutput("Error loading company data: " + e.getMessage() + "\n");
            appendToOutput("Sample data will be generated instead\n");
        }
    }
    
    /**
     * Sets up the main application layout.
     */
    private void setupMainLayout() {
        setLayout(new BorderLayout());
        
        // Create main tabs
        mainTabPane.addTab("👥 Employees", employeeManager.createContent());
        mainTabPane.addTab("🚗 Vehicles", vehicleManager.createContent());
        mainTabPane.addTab("📐 Shapes", shapeManager.createContent());
        mainTabPane.addTab("🏢 Departments", departmentManager.createContent());
        mainTabPane.addTab("🏛️ Company", createCompanyOverviewTab());
        mainTabPane.addTab("🧪 Tests", testRunner.createContent());
        
        // Create output controls panel
        JPanel outputControlsPanel = createOutputControlsPanel();
        
        // Main split pane
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setTopComponent(mainTabPane);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(outputControlsPanel, BorderLayout.NORTH);
        bottomPanel.add(outputScrollPane, BorderLayout.CENTER);
        
        mainSplitPane.setBottomComponent(bottomPanel);
        mainSplitPane.setDividerLocation(500);
        mainSplitPane.setResizeWeight(0.7);
        
        add(mainSplitPane, BorderLayout.CENTER);
    }
    
    /**
     * Creates the output controls panel.
     */
    private JPanel createOutputControlsPanel() {
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton clearButton = new JButton("Clear Output");
        clearButton.addActionListener(e -> outputArea.setText(""));
        
        JButton exportLogsButton = new JButton("Export Logs");
        exportLogsButton.addActionListener(e -> exportLogs());
        
        JCheckBox autoScrollCheckBox = new JCheckBox("Auto-scroll", true);
        autoScrollCheckBox.addActionListener(e -> {
            // Auto-scroll functionality will be handled in appendToOutput
        });
        
        controlsPanel.add(clearButton);
        controlsPanel.add(exportLogsButton);
        controlsPanel.add(autoScrollCheckBox);
        
        return controlsPanel;
    }
    
    /**
     * Sets up the application menu bar.
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newSessionItem = new JMenuItem("New Session");
        JMenuItem saveDataItem = new JMenuItem("Save Company Data");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        newSessionItem.addActionListener(e -> resetApplication());
        saveDataItem.addActionListener(e -> saveCompanyData());
        exitItem.addActionListener(e -> {
            logger.info("Application exit requested");
            
            // Save data before exiting
            if (dataManager != null) {
                dataManager.saveAllData();
                appendToOutput("Data saved before exit\n");
            }
            
            System.exit(0);
        });
        
        fileMenu.add(newSessionItem);
        fileMenu.add(saveDataItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Tools Menu
        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem runAllTestsItem = new JMenuItem("Run All Tests");
        JMenuItem systemInfoItem = new JMenuItem("System Information");
        
        runAllTestsItem.addActionListener(e -> testRunner.runAllTests());
        systemInfoItem.addActionListener(e -> showSystemInfo());
        
        toolsMenu.add(runAllTestsItem);
        toolsMenu.add(systemInfoItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        JMenuItem userGuideItem = new JMenuItem("User Guide");
        
        aboutItem.addActionListener(e -> showAboutDialog());
        userGuideItem.addActionListener(e -> showUserGuide());
        
        helpMenu.add(userGuideItem);
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Refreshes all UI managers with the current company data.
     */
    private void refreshAllManagers() {
        try {
            // Get company management from the employee manager
            if (employeeManager != null && employeeManager.getCompanyManagement() != null) {
                CompanyManagment company = employeeManager.getCompanyManagement();
                
                // Refresh employee manager UI
                try {
                    employeeManager.refreshUI();
                    appendToOutput("Employee manager refreshed\n");
                } catch (Exception e) {
                    appendToOutput("Warning: Could not refresh employee manager: " + e.getMessage() + "\n");
                    logger.debug("Employee manager refresh failed: " + e.getMessage());
                }
                
                // Update vehicle manager
                if (vehicleManager != null) {
                    try {
                        vehicleManager.setCompanyManagement(company);
                        appendToOutput("Vehicle manager refreshed\n");
                    } catch (Exception e) {
                        appendToOutput("Warning: Could not refresh vehicle manager: " + e.getMessage() + "\n");
                        logger.debug("Vehicle manager refresh failed: " + e.getMessage());
                    }
                }
                
                // Update department manager
                if (departmentManager != null) {
                    try {
                        departmentManager.setCompanyManagement(company);
                        appendToOutput("Department manager refreshed\n");
                    } catch (Exception e) {
                        appendToOutput("Warning: Could not refresh department manager: " + e.getMessage() + "\n");
                        logger.debug("Department manager refresh failed: " + e.getMessage());
                    }
                }
                
                appendToOutput("UI managers refresh completed\n");
            }
        } catch (Exception e) {
            appendToOutput("Error in refreshAllManagers: " + e.getMessage() + "\n");
            logger.error("Failed to refresh UI managers: " + e.getMessage());
        }
    }
    
    /**
     * Resets the application to initial state.
     */
    private void resetApplication() {
        logger.info("Resetting application session");
        
        // Save current data before resetting
        if (dataManager != null) {
            dataManager.saveAllData();
            appendToOutput("Data saved before reset\n");
        }
        
        outputArea.setText("");
        
        // Reset all managers
        employeeManager.reset();
        vehicleManager.reset();
        shapeManager.reset();
        // departmentManager.reset(); // DepartmentManagerUI doesn't have reset method
        testRunner.reset();
        
        // Reload data
        if (dataManager != null) {
            dataManager.loadAllData();
            appendToOutput("Data reloaded after reset\n");
        }
        
        appendToOutput("=== New Session Started ===\n");
    }
    
    /**
     * Manually saves all company data to files.
     */
    private void saveCompanyData() {
        logger.info("Manual save requested");
        
        try {
            if (dataManager != null) {
                dataManager.saveAllData();
                appendToOutput("Company data saved successfully to files\n");
                appendToOutput("Employees saved: " + employeeManager.getCompanyManagement().getEmployees().size() + "\n");
                appendToOutput("Departments saved: " + employeeManager.getCompanyManagement().getDepartments().size() + "\n");
                showInfoDialog("Save Successful", "All company data has been saved to files in src/Runner/data/");
            } else {
                appendToOutput("Error: Data manager not initialized\n");
                showWarningDialog("Save Warning", "Data manager not available. Cannot save data.");
            }
        } catch (Exception e) {
            logger.error("Failed to save company data: " + e.getMessage());
            showErrorDialog("Save Failed", "Could not save company data", e);
        }
    }
    
    /**
     * Exports current logs to file.
     */
    private void exportLogs() {
        logger.info("Export logs functionality requested");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File("system_logs.txt"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
                    writer.write(outputArea.getText());
                }
                showInfoDialog("Export Success", "Logs exported to: " + file.getAbsolutePath());
            } catch (java.io.IOException e) {
                showErrorDialog("Export Failed", "Could not export logs", e);
            }
        }
    }
    
    /**
     * Creates the company overview tab with comprehensive company statistics.
     */
    private JPanel createCompanyOverviewTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create overview metrics panel
        JPanel metricsPanel = createCompanyMetricsPanel();
        
        // Create company operations panel
        JPanel operationsPanel = createCompanyOperationsPanel();
        
        // Split pane for layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(metricsPanel);
        splitPane.setBottomComponent(operationsPanel);
        splitPane.setDividerLocation(200);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the company metrics panel.
     */
    private JPanel createCompanyMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.setBorder(new TitledBorder("Company Dashboard"));
        
        // Initialize metric labels that will be updated
        totalEmployeesLabel = new JLabel("0", JLabel.CENTER);
        totalDepartmentsLabel = new JLabel("0", JLabel.CENTER);
        totalVehiclesLabel = new JLabel("0", JLabel.CENTER);
        totalSalaryLabel = new JLabel("$0.00", JLabel.CENTER);
        
        panel.add(createMetricPanel("Total Employees", totalEmployeesLabel, Color.BLUE));
        panel.add(createMetricPanel("Departments", totalDepartmentsLabel, Color.GREEN));
        panel.add(createMetricPanel("Fleet Vehicles", totalVehiclesLabel, Color.ORANGE));
        panel.add(createMetricPanel("Total Salary Cost", totalSalaryLabel, Color.RED));
        
        // Add control buttons
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e_ -> refreshCompanyMetrics());
        panel.add(refreshButton);
        
        JButton workDayButton = new JButton("Process Work Day");
        workDayButton.addActionListener(e_ -> processCompanyWorkDay());
        panel.add(workDayButton);
        
        JButton statsButton = new JButton("Detailed Stats");
        statsButton.addActionListener(e_ -> showDetailedCompanyStats());
        panel.add(statsButton);
        
        JButton exportButton = new JButton("Export Report");
        exportButton.addActionListener(e_ -> exportCompanyReport());
        panel.add(exportButton);
        
        return panel;
    }
    
    /**
     * Creates a metric panel with label and value.
     */
    private JPanel createMetricPanel(String title, JLabel valueLabel, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(color, 2));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        titleLabel.setForeground(color);
        
        valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        valueLabel.setForeground(color);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the company operations panel.
     */
    private JPanel createCompanyOperationsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Company Operations"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Company-wide operations
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel operationsLabel = new JLabel("Company-wide Operations");
        operationsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        panel.add(operationsLabel, gbc);
        
        gbc.gridwidth = 1; gbc.gridy++; gbc.gridx = 0;
        JButton integrateDataButton = new JButton("Integrate All Data");
        integrateDataButton.addActionListener(e_ -> integrateAllManagerData());
        panel.add(integrateDataButton, gbc);
        
        gbc.gridx = 1;
        JButton generateReportButton = new JButton("Generate Full Report");
        generateReportButton.addActionListener(e_ -> generateComprehensiveReport());
        panel.add(generateReportButton, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        JButton companyTestButton = new JButton("Run Company Tests");
        companyTestButton.addActionListener(e_ -> runCompanyTests());
        panel.add(companyTestButton, gbc);
        
        gbc.gridx = 1;
        JButton resetAllButton = new JButton("Reset All Systems");
        resetAllButton.addActionListener(e_ -> resetAllSystems());
        panel.add(resetAllButton, gbc);
        
        return panel;
    }
    
    /**
     * Refreshes company metrics by collecting data from all managers.
     */
    private void refreshCompanyMetrics() {
        try {
            var companyMgmt = employeeManager.getCompanyManagement();
            
            // Update employee count
            totalEmployeesLabel.setText(String.valueOf(companyMgmt.getEmployees().size()));
            
            // Update department count
            totalDepartmentsLabel.setText(String.valueOf(companyMgmt.getDepartments().size()));
            
            // Update vehicle count
            totalVehiclesLabel.setText(String.valueOf(companyMgmt.getVehicles().size()));
            
            // Calculate and update total salary
            double totalSalary = 0.0;
            for (var emp : companyMgmt.getEmployees()) {
                totalSalary += emp.getSalary();
            }
            totalSalaryLabel.setText(String.format("$%.2f", totalSalary));
            
            // Update metrics display
            appendToOutput("Company metrics refreshed - Employees: " + companyMgmt.getEmployees().size() +
                          ", Departments: " + companyMgmt.getDepartments().size() +
                          ", Vehicles: " + companyMgmt.getVehicles().size() +
                          ", Total Salary: $" + String.format("%.2f", totalSalary) + "\n");
            logger.info("Company metrics refreshed");
            
        } catch (Exception e) {
            showErrorDialog("Refresh Error", "Failed to refresh company metrics", e);
        }
    }
    
    /**
     * Processes a work day for the entire company.
     */
    private void processCompanyWorkDay() {
        try {
            var companyMgmt = employeeManager.getCompanyManagement();
            companyMgmt.workADay();
            
            appendToOutput("Company work day processed - all shift workers worked 8 hours\n");
            logger.info("Company work day processed");
            
        } catch (Exception e) {
            showErrorDialog("Work Day Error", "Failed to process company work day", e);
        }
    }
    
    /**
     * Shows detailed company statistics.
     */
    private void showDetailedCompanyStats() {
        try {
            var companyMgmt = employeeManager.getCompanyManagement();
            
            StringBuilder stats = new StringBuilder();
            stats.append("=== DETAILED COMPANY STATISTICS ===\n\n");
            
            // Employee statistics
            stats.append("EMPLOYEE BREAKDOWN:\n");
            stats.append("Total Employees: ").append(companyMgmt.getEmployees().size()).append("\n");
            stats.append("Departments: ").append(companyMgmt.getDepartments().size()).append("\n");
            stats.append("Fleet Vehicles: ").append(companyMgmt.getVehicles().size()).append("\n\n");
            
            // Calculate total salaries
            double totalSalary = companyMgmt.getEmployees().stream()
                .mapToDouble(emp -> emp.getSalary())
                .sum();
            stats.append("Total Salary Cost: $").append(String.format("%.2f", totalSalary)).append("\n\n");
            
            // Department breakdown
            stats.append("DEPARTMENT DETAILS:\n");
            for (var dept : companyMgmt.getDepartments()) {
                stats.append("- ").append(dept.getName())
                     .append(": ").append(dept.getEmployees().size()).append(" employees")
                     .append(", Total Salary: $").append(String.format("%.2f", dept.getTotalSalary()))
                     .append("\n");
            }
            
            showInfoDialog("Company Statistics", stats.toString());
            
        } catch (Exception e) {
            showErrorDialog("Statistics Error", "Failed to generate company statistics", e);
        }
    }
    
    /**
     * Exports a comprehensive company report.
     */
    private void exportCompanyReport() {
        try {
            appendToOutput("Exporting comprehensive company report...\n");
            
            // Save current data first
            if (dataManager != null) {
                dataManager.saveAllData();
                appendToOutput("Company data saved to files\n");
            }
            
            // Generate comprehensive report
            StringBuilder report = new StringBuilder();
            report.append("=== COMPREHENSIVE COMPANY REPORT ===\n\n");
            
            if (employeeManager != null && employeeManager.getCompanyManagement() != null) {
                var companyMgmt = employeeManager.getCompanyManagement();
                
                report.append("COMPANY OVERVIEW:\n");
                report.append("Total Employees: ").append(companyMgmt.getEmployees().size()).append("\n");
                report.append("Total Departments: ").append(companyMgmt.getDepartments().size()).append("\n");
                report.append("Data Files: Employees, Departments saved to src/Runner/data/\n\n");
                
                // Calculate salary information
                double totalSalary = 0;
                int managers = 0, office = 0, shift = 0, drivers = 0;
                
                for (var emp : companyMgmt.getEmployees()) {
                    totalSalary += emp.getSalary();
                    if (emp.getClass().getSimpleName().equals("Manager")) managers++;
                    else if (emp.getClass().getSimpleName().equals("OfficeWorker")) office++;
                    else if (emp.getClass().getSimpleName().equals("ShiftWorker")) shift++;
                    else if (emp.getClass().getSimpleName().equals("Driver")) drivers++;
                }
                
                report.append("EMPLOYEE BREAKDOWN:\n");
                report.append("Managers: ").append(managers).append("\n");
                report.append("Office Workers: ").append(office).append("\n");
                report.append("Shift Workers: ").append(shift).append("\n");
                report.append("Drivers: ").append(drivers).append("\n");
                report.append("Total Salary Cost: $").append(String.format("%.2f", totalSalary)).append("\n\n");
                
                report.append("DEPARTMENTS:\n");
                for (var dept : companyMgmt.getDepartments()) {
                    report.append("- ").append(dept.getName())
                           .append(" (Manager: ").append(dept.getHead().getName()).append(")\n");
                }
            }
            
            // Save report to file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("company_report.txt"));
            
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    java.io.File file = fileChooser.getSelectedFile();
                    try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
                        writer.write(report.toString());
                    }
                    showInfoDialog("Export Success", "Company report exported to: " + file.getAbsolutePath());
                    appendToOutput("Company report exported successfully\n");
                } catch (java.io.IOException e) {
                    showErrorDialog("Export Failed", "Could not export company report", e);
                }
            }
            
        } catch (Exception e) {
            showErrorDialog("Export Error", "Failed to generate company report", e);
        }
    }
    
    /**
     * Integrates data from all managers into the company management system.
     */
    private void integrateAllManagerData() {
        try {
            // Get all created objects from managers and integrate them
            var companyMgmt = employeeManager.getCompanyManagement();
            
            // Integrate departments from department manager
            if (departmentManager != null && departmentManager.getCompanyManagement() != null) {
                for (Department dept : departmentManager.getCompanyManagement().getDepartments()) {
                    if (!companyMgmt.getDepartments().contains(dept)) {
                        companyMgmt.add(dept);
                    }
                }
            }
            
            // Integrate vehicles from vehicle manager
            for (var vehicle : vehicleManager.getVehicles()) {
                if (!companyMgmt.getVehicles().contains(vehicle)) {
                    companyMgmt.add(vehicle);
                }
            }
            
            appendToOutput("All manager data integrated into company management system\n");
            logger.info("All manager data integrated");
            
        } catch (Exception e) {
            showErrorDialog("Integration Error", "Failed to integrate manager data", e);
        }
    }
    
    /**
     * Generates a comprehensive report from all systems.
     */
    private void generateComprehensiveReport() {
        appendToOutput("=== GENERATING COMPREHENSIVE COMPANY REPORT ===\n");
        
        // Generate reports from available data
        try {
            appendToOutput("Employee Manager: " + employeeManager.getClass().getSimpleName() + " initialized\n");
            appendToOutput("Vehicle Manager: " + vehicleManager.getClass().getSimpleName() + " initialized\n");
            appendToOutput("Shape Manager: " + shapeManager.getClass().getSimpleName() + " initialized\n");
            appendToOutput("Department Manager: " + departmentManager.getClass().getSimpleName() + " initialized\n");
        } catch (Exception e) {
            appendToOutput("Error generating report: " + e.getMessage() + "\n");
        }
        
        appendToOutput("=== COMPREHENSIVE REPORT COMPLETED ===\n");
    }
    
    /**
     * Runs tests for all company systems.
     */
    private void runCompanyTests() {
        appendToOutput("=== RUNNING ALL COMPANY TESTS ===\n");
        testRunner.runAllTests();
    }
    
    /**
     * Resets all systems in the application.
     */
    private void resetAllSystems() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to reset ALL systems? This will clear all data and cannot be undone.",
            "Confirm Reset All Systems",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            resetApplication();
            appendToOutput("All systems have been reset\n");
        }
    }
    
    /**
     * Shows system information dialog.
     */
    private void showSystemInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        info.append("JVM: ").append(System.getProperty("java.vm.name")).append("\n");
        info.append("OS: ").append(System.getProperty("os.name")).append(" ").append(System.getProperty("os.version")).append("\n");
        info.append("Available Memory: ").append(Runtime.getRuntime().maxMemory() / (1024 * 1024)).append(" MB\n");
        info.append("Free Memory: ").append(Runtime.getRuntime().freeMemory() / (1024 * 1024)).append(" MB\n");
        info.append("Total Memory: ").append(Runtime.getRuntime().totalMemory() / (1024 * 1024)).append(" MB\n");
        
        showInfoDialog("System Information", info.toString());
    }
    
    /**
     * Shows the about dialog.
     */
    private void showAboutDialog() {
        String aboutText = """
                Company Management System Runner v1.0
                
                A comprehensive testing and management interface for:
                • Employee Management (Workers, Managers, Drivers)
                • Vehicle Management (Cars, Trucks, Buses)
                • Shape Geometry System
                • Department Management
                • Automated Testing Suite
                
                Built with Java Swing following enterprise-grade standards.
                Developed by Senior Java Developer Team.
                """;
        
        showInfoDialog("About", aboutText);
    }
    
    /**
     * Shows the user guide.
     */
    private void showUserGuide() {
        String guideText = """
                User Guide - Quick Start:
                
                1. Employee Management: Create and manage different types of employees
                   - Office Workers (ID starts with 5)
                   - Shift Workers (ID starts with 3)
                   - Drivers (ID starts with 2)
                   - Managers (extends Office Workers)
                
                2. Vehicle Management: Create vehicles and assign drivers
                   - Trucks (require license C)
                   - Buses (require license D)
                   - Passenger Vehicles (require license B)
                
                3. Shape Management: Create and test geometric shapes
                   - 2D Shapes: Circle, Rectangle, Triangle
                   - 3D Shapes: Sphere, Cylinder, Cone, Cuboid
                
                4. Department Management: Organize employees into departments
                   - Assign managers to departments
                   - Add/remove employees
                   - Calculate total salaries
                
                5. Test Runner: Execute automated tests for all components
                   - Individual test classes
                   - Complete test suite
                   - Performance monitoring
                
                Use the output area below to monitor all operations and results.
                """;
        
        showInfoDialog("User Guide", guideText);
    }
    
    /**
     * Appends text to the main output area with auto-scroll.
     */
    public void appendToOutput(String text) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(text);
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }
    
    /**
     * Shows an information dialog.
     */
    public void showInfoDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows a warning dialog.
     */
    public void showWarningDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Shows an error dialog with exception details.
     */
    public void showErrorDialog(String title, String message, Exception e) {
        String fullMessage = message + "\n\nError: " + e.getMessage();
        
        // Create expandable error dialog
        JTextArea textArea = new JTextArea(10, 50);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        textArea.setText(sw.toString());
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(fullMessage), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JOptionPane.showMessageDialog(this, panel, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Main method to launch the Swing application.
     */
    public static void main(String[] args) {
        logger.info("Launching Company Management System Runner UI");
        
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warning("Could not set system look and feel: " + e.getMessage());
        }
        
        // Launch application on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new MainApplicationSwing());
    }
}
