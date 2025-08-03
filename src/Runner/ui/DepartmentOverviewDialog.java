package Runner.ui;

import CompanyManagement.CompanyManagment;
import Employees.*;
import Utils.Logger;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Dialog window that displays a detailed overview of a department.
 * Shows all employees in the department in a table format with detailed information.
 * 
 * @author Senior Java Developer
 * @version 1.0
 */
public class DepartmentOverviewDialog extends JDialog {
    private static final Logger logger = new Logger("DepartmentOverviewDialog");
    
    private final Department department;
    private final CompanyManagment companyManagement;
    
    // UI Components
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> tableSorter;
    private JLabel departmentInfoLabel;
    private JLabel employeeCountLabel;
    private JLabel totalSalaryLabel;
    private JLabel avgSalaryLabel;
    
    // Table column indices
    private static final int COL_ID = 0;
    private static final int COL_NAME = 1;
    private static final int COL_TYPE = 2;
    private static final int COL_SALARY = 3;
    private static final int COL_DETAILS = 4;

    /**
     * Constructor for DepartmentOverviewDialog.
     * @param parent Parent frame
     * @param department The department to display
     * @param companyManagement Reference to company management
     */
    public DepartmentOverviewDialog(JFrame parent, Department department, CompanyManagment companyManagement) {
        super(parent, "Department Overview: " + department.getName(), true);
        this.department = department;
        this.companyManagement = companyManagement;
        
        initializeComponents();
        setupLayout();
        loadEmployeeData();
        updateStatistics();
        
        // Configure dialog
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        logger.info("Department overview dialog created for: " + department.getName());
    }
    
    /**
     * Initializes the UI components.
     */
    private void initializeComponents() {
        // Initialize table model
        String[] columnNames = {"ID", "Name", "Type", "Salary", "Details"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        // Create table with sorter
        employeeTable = new JTable(tableModel);
        tableSorter = new TableRowSorter<>(tableModel);
        employeeTable.setRowSorter(tableSorter);
        
        // Configure table appearance
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowHeight(25);
        employeeTable.getTableHeader().setReorderingAllowed(false);
        employeeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Set column widths
        employeeTable.getColumnModel().getColumn(COL_ID).setPreferredWidth(80);
        employeeTable.getColumnModel().getColumn(COL_NAME).setPreferredWidth(150);
        employeeTable.getColumnModel().getColumn(COL_TYPE).setPreferredWidth(120);
        employeeTable.getColumnModel().getColumn(COL_SALARY).setPreferredWidth(100);
        employeeTable.getColumnModel().getColumn(COL_DETAILS).setPreferredWidth(250);
        
        // Initialize info labels
        departmentInfoLabel = new JLabel();
        employeeCountLabel = new JLabel();
        totalSalaryLabel = new JLabel();
        avgSalaryLabel = new JLabel();
        
        // Style the labels
        Font boldFont = new Font(Font.SANS_SERIF, Font.BOLD, 12);
        departmentInfoLabel.setFont(boldFont);
        employeeCountLabel.setFont(boldFont);
        totalSalaryLabel.setFont(boldFont);
        avgSalaryLabel.setFont(boldFont);
    }
    
    /**
     * Sets up the dialog layout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create table panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // Create footer panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the header information panel.
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Department Information"));
        panel.setBackground(new Color(240, 248, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Department name and manager info
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        departmentInfoLabel.setText("Department: " + department.getName() + 
                                   " | Manager: " + (department.getHead() != null ? 
                                   department.getHead().getName() : "No Manager"));
        panel.add(departmentInfoLabel, gbc);
        
        // Statistics row
        gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(employeeCountLabel, gbc);
        gbc.gridx = 1;
        panel.add(totalSalaryLabel, gbc);
        
        return panel;
    }
    
    /**
     * Creates the employee table panel.
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Department Employees"));
        
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the footer panel with action buttons.
     */
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(new TitledBorder("Actions"));
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            loadEmployeeData();
            updateStatistics();
        });
        
        JButton exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(e -> exportDepartmentData());
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        panel.add(avgSalaryLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(refreshButton);
        panel.add(exportButton);
        panel.add(closeButton);
        
        return panel;
    }
    
    /**
     * Loads employee data into the table.
     */
    private void loadEmployeeData() {
        tableModel.setRowCount(0); // Clear existing data
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        
        for (Employee employee : department.getEmployees()) {
            Object[] rowData = new Object[5];
            rowData[COL_ID] = employee.getId();
            rowData[COL_NAME] = employee.getName();
            rowData[COL_TYPE] = getEmployeeType(employee);
            rowData[COL_SALARY] = currencyFormat.format(employee.getSalary());
            rowData[COL_DETAILS] = getEmployeeDetails(employee);
            
            tableModel.addRow(rowData);
        }
        
        logger.info("Loaded " + department.getEmployees().size() + " employees for department: " + department.getName());
    }
    
    /**
     * Updates the statistics labels.
     */
    private void updateStatistics() {
        int employeeCount = department.getEmployees().size();
        double totalSalary = department.getEmployees().stream()
                .mapToDouble(Employee::getSalary)
                .sum();
        double avgSalary = employeeCount > 0 ? totalSalary / employeeCount : 0;
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        
        employeeCountLabel.setText("Total Employees: " + employeeCount);
        totalSalaryLabel.setText("Total Salary: " + currencyFormat.format(totalSalary));
        avgSalaryLabel.setText("Average Salary: " + currencyFormat.format(avgSalary));
    }
    
    /**
     * Gets the employee type as a string.
     */
    private String getEmployeeType(Employee employee) {
        if (employee instanceof Manager) {
            return "Manager";
        } else if (employee instanceof Driver) {
            return "Driver";
        } else if (employee instanceof ShiftWorker) {
            return "Shift Worker";
        } else if (employee instanceof OfficeWorker) {
            return "Office Worker";
        }
        return "Employee";
    }
    
    /**
     * Gets detailed information about the employee.
     */
    private String getEmployeeDetails(Employee employee) {
        if (employee instanceof Manager manager) {
            return String.format("Bonus: $%.2f", 
                    manager.getBonus());
        } else if (employee instanceof Driver driver) {
            return String.format("Hourly Rate: $%.2f/h, License: %s", 
                    driver.getHourlyRate(), driver.getLicense());
        } else if (employee instanceof ShiftWorker shiftWorker) {
            return String.format("Hourly Rate: $%.2f/h, Hours Worked: %d", 
                    shiftWorker.getHourlyRate(), shiftWorker.getHoursWorked());
        } else if (employee instanceof OfficeWorker) {
            return String.format("Monthly Salary: $%.2f/m, Office Work", employee.getSalary());
        }
        return "Standard Employee";
    }
    
    /**
     * Exports department data to CSV (placeholder implementation).
     */
    private void exportDepartmentData() {
        JOptionPane.showMessageDialog(this, 
                "Export functionality will save department data to:\n" +
                "department_" + department.getName().toLowerCase().replace(" ", "_") + "_employees.csv",
                "Export Information", 
                JOptionPane.INFORMATION_MESSAGE);
        logger.info("Export requested for department: " + department.getName());
    }
}
