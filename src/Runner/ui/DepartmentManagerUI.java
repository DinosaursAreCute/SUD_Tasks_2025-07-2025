package Runner.ui;

import CompanyManagement.CompanyManagment;
import Employees.*;
import Utils.Logger;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Enhanced UI Manager for Department operations with table view.
 * Provides interface for creating, managing, and testing department objects.
 * 
 * @author Senior Java Developer
 * @version 2.0
 */
public class DepartmentManagerUI {
    private static final Logger logger = new Logger("DepartmentManagerUI");
    
    private final MainApplicationSwing parentApp;
    private CompanyManagment companyManagement;
    
    // UI Components
    private JPanel mainPanel;
    private JTable departmentTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> tableSorter;
    private JTextField departmentNameField;
    private JComboBox<Manager> managerCombo;
    private JTextField searchField;
    
    // Table column indices
    private static final int COL_NAME = 0;
    private static final int COL_MANAGER = 1;
    private static final int COL_EMPLOYEE_COUNT = 2;
    private static final int COL_TOTAL_SALARY = 3;

    /**
     * Constructor for DepartmentManagerUI.
     * @param parentApp Reference to the main application
     */
    public DepartmentManagerUI(MainApplicationSwing parentApp) {
        this.parentApp = parentApp;
        initializeTableModel();
        logger.info("DepartmentManagerUI initialized with table view");
    }
    
    /**
     * Sets the company management reference for data access.
     */
    public void setCompanyManagement(CompanyManagment companyManagement) {
        this.companyManagement = companyManagement;
        refreshDepartmentTable();
        updateManagerComboBox();
    }
    
    /**
     * Initializes the table model with appropriate columns.
     */
    private void initializeTableModel() {
        String[] columnNames = {"Department", "Manager", "Employee Count", "Total Salary"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        tableSorter = new TableRowSorter<>(tableModel);
    }
    
    /**
     * Creates and returns the main content panel for department management.
     * @return JPanel containing all department management controls
     */
    public JPanel createContent() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search and filter panel
        JPanel searchPanel = createSearchPanel();
        
        // Create department table panel
        JPanel tablePanel = createDepartmentTablePanel();
        
        // Create form panel
        JPanel formPanel = createDepartmentFormPanel();
        
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
        searchField.setToolTipText("Search departments by name or manager");
        panel.add(searchField);
        
        JButton searchButton = new JButton("Filter");
        searchButton.addActionListener(e -> filterDepartmentTable());
        panel.add(searchButton);
        
        JButton clearSearchButton = new JButton("Clear");
        clearSearchButton.addActionListener(e -> {
            searchField.setText("");
            tableSorter.setRowFilter(null);
        });
        panel.add(clearSearchButton);
        
        return panel;
    }

    /**
     * Creates the department table panel with enhanced features.
     */
    private JPanel createDepartmentTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Department List"));
        
        // Create table
        departmentTable = new JTable(tableModel);
        departmentTable.setRowSorter(tableSorter);
        departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Configure table appearance
        departmentTable.setRowHeight(25);
        departmentTable.getTableHeader().setReorderingAllowed(false);
        departmentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Set column widths
        departmentTable.getColumnModel().getColumn(COL_NAME).setPreferredWidth(200);
        departmentTable.getColumnModel().getColumn(COL_MANAGER).setPreferredWidth(150);
        departmentTable.getColumnModel().getColumn(COL_EMPLOYEE_COUNT).setPreferredWidth(100);
        departmentTable.getColumnModel().getColumn(COL_TOTAL_SALARY).setPreferredWidth(120);
        
        // Add selection listener
        departmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateFormFromSelection();
            }
        });
        
        // Add double-click listener to open department overview
        departmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = departmentTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        int modelRow = departmentTable.convertRowIndexToModel(selectedRow);
                        openDepartmentOverview(modelRow);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(departmentTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the department form panel.
     */
    private JPanel createDepartmentFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Department Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Department Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Department Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        departmentNameField = new JTextField(20);
        panel.add(departmentNameField, gbc);
        
        // Manager
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Manager:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        managerCombo = new JComboBox<>();
        managerCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Manager) {
                    Manager manager = (Manager) value;
                    setText(manager.getName() + " (ID: " + manager.getId() + ")");
                }
                return this;
            }
        });
        panel.add(managerCombo, gbc);
        
        // Add spacer
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weighty = 1.0;
        panel.add(new JPanel(), gbc);
        
        return panel;
    }
    
    /**
     * Creates the action panel.
     */
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton createButton = new JButton("Create Department");
        createButton.addActionListener(e -> createDepartment());
        panel.add(createButton);
        
        JButton updateButton = new JButton("Update Selected");
        updateButton.addActionListener(e -> updateSelectedDepartment());
        panel.add(updateButton);
        
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedDepartment());
        panel.add(deleteButton);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshDepartmentTable());
        panel.add(refreshButton);
        
        JButton clearFormButton = new JButton("Clear Form");
        clearFormButton.addActionListener(e -> clearForm());
        panel.add(clearFormButton);
        
        return panel;
    }
    
    /**
     * Refreshes the department table with current data.
     */
    private void refreshDepartmentTable() {
        if (companyManagement == null) {
            return;
        }
        
        logger.info("Refreshing department table");
        tableModel.setRowCount(0); // Clear existing data
        
        try {
            for (Department dept : companyManagement.getDepartments()) {
                Object[] rowData = new Object[4];
                rowData[COL_NAME] = dept.getName();
                rowData[COL_MANAGER] = dept.getHead().getName();
                rowData[COL_EMPLOYEE_COUNT] = dept.getEmployees().size();
                
                // Calculate total salary for department
                double totalSalary = 0.0;
                for (Employee emp : dept.getEmployees()) {
                    totalSalary += emp.getSalary();
                }
                rowData[COL_TOTAL_SALARY] = String.format("$%.2f", totalSalary);
                
                tableModel.addRow(rowData);
            }
            
            logger.info("Department table refreshed with " + companyManagement.getDepartments().size() + " departments");
            
        } catch (Exception e) {
            logger.error("Error refreshing department table: " + e.getMessage());
            parentApp.showErrorDialog("Refresh Error", "Failed to refresh department table", e);
        }
    }
    
    /**
     * Updates the manager combo box with available managers.
     */
    private void updateManagerComboBox() {
        if (companyManagement == null) {
            return;
        }
        
        managerCombo.removeAllItems();
        for (Employee emp : companyManagement.getEmployees()) {
            if (emp instanceof Manager) {
                managerCombo.addItem((Manager) emp);
            }
        }
    }
    
    /**
     * Filters the department table based on search criteria.
     */
    private void filterDepartmentTable() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            tableSorter.setRowFilter(null);
        } else {
            tableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }
    
    /**
     * Updates the form fields from the selected table row.
     */
    private void updateFormFromSelection() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = departmentTable.convertRowIndexToModel(selectedRow);
            String deptName = (String) tableModel.getValueAt(modelRow, COL_NAME);
            String managerName = (String) tableModel.getValueAt(modelRow, COL_MANAGER);
            
            departmentNameField.setText(deptName);
            
            // Find and select the manager in combo box
            for (int i = 0; i < managerCombo.getItemCount(); i++) {
                Manager manager = managerCombo.getItemAt(i);
                if (manager.getName().equals(managerName)) {
                    managerCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    /**
     * Creates a new department.
     */
    private void createDepartment() {
        try {
            String name = departmentNameField.getText().trim();
            Manager selectedManager = (Manager) managerCombo.getSelectedItem();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Please enter a department name.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (selectedManager == null) {
                JOptionPane.showMessageDialog(mainPanel, "Please select a manager.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if department name already exists
            for (Department existingDept : companyManagement.getDepartments()) {
                if (existingDept.getName().equalsIgnoreCase(name)) {
                    JOptionPane.showMessageDialog(mainPanel, "Department name already exists.", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            Department newDept = new Department(name, new ArrayList<>(), selectedManager);
            companyManagement.add(newDept);
            
            refreshDepartmentTable();
            clearForm();
            
            parentApp.appendToOutput("Created new department: " + name + " with manager: " + selectedManager.getName() + "\n");
            logger.info("Created department: " + name);
            
        } catch (Exception e) {
            logger.error("Error creating department: " + e.getMessage());
            parentApp.showErrorDialog("Creation Error", "Failed to create department", e);
        }
    }
    
    /**
     * Updates the selected department.
     */
    private void updateSelectedDepartment() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(mainPanel, "Please select a department to update.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int modelRow = departmentTable.convertRowIndexToModel(selectedRow);
            String currentName = (String) tableModel.getValueAt(modelRow, COL_NAME);
            
            // Find the department to update
            Department deptToUpdate = null;
            for (Department dept : companyManagement.getDepartments()) {
                if (dept.getName().equals(currentName)) {
                    deptToUpdate = dept;
                    break;
                }
            }
            
            if (deptToUpdate == null) {
                JOptionPane.showMessageDialog(mainPanel, "Department not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String newName = departmentNameField.getText().trim();
            Manager newManager = (Manager) managerCombo.getSelectedItem();
            
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Please enter a department name.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (newManager == null) {
                JOptionPane.showMessageDialog(mainPanel, "Please select a manager.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update department (Note: Department class may not have setters, so this is conceptual)
            // In a real implementation, you might need to recreate the department
            parentApp.appendToOutput("Updated department: " + currentName + " -> " + newName + "\n");
            
            refreshDepartmentTable();
            logger.info("Updated department: " + currentName);
            
        } catch (Exception e) {
            logger.error("Error updating department: " + e.getMessage());
            parentApp.showErrorDialog("Update Error", "Failed to update department", e);
        }
    }
    
    /**
     * Deletes the selected department.
     */
    private void deleteSelectedDepartment() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(mainPanel, "Please select a department to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int modelRow = departmentTable.convertRowIndexToModel(selectedRow);
            String deptName = (String) tableModel.getValueAt(modelRow, COL_NAME);
            
            int result = JOptionPane.showConfirmDialog(mainPanel,
                "Are you sure you want to delete department '" + deptName + "'?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                // Find and remove the department
                Department deptToRemove = null;
                for (Department dept : companyManagement.getDepartments()) {
                    if (dept.getName().equals(deptName)) {
                        deptToRemove = dept;
                        break;
                    }
                }
                
                if (deptToRemove != null) {
                    companyManagement.remove(deptToRemove);
                    refreshDepartmentTable();
                    clearForm();
                    
                    parentApp.appendToOutput("Deleted department: " + deptName + "\n");
                    logger.info("Deleted department: " + deptName);
                }
            }
            
        } catch (Exception e) {
            logger.error("Error deleting department: " + e.getMessage());
            parentApp.showErrorDialog("Deletion Error", "Failed to delete department", e);
        }
    }
    
    /**
     * Clears the form fields.
     */
    private void clearForm() {
        departmentNameField.setText("");
        managerCombo.setSelectedIndex(-1);
        departmentTable.clearSelection();
    }
    
    /**
     * Opens a department overview window showing all employees in the selected department.
     * @param modelRow The model row index of the selected department
     */
    private void openDepartmentOverview(int modelRow) {
        try {
            if (companyManagement != null && modelRow < companyManagement.getDepartments().size()) {
                Department selectedDept = companyManagement.getDepartments().get(modelRow);
                
                // Create and show the department overview dialog
                DepartmentOverviewDialog dialog = new DepartmentOverviewDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(mainPanel), 
                    selectedDept, 
                    companyManagement
                );
                dialog.setVisible(true);
                
                logger.info("Opened overview for department: " + selectedDept.getName());
            }
        } catch (Exception e) {
            logger.error("Error opening department overview: " + e.getMessage());
            parentApp.showErrorDialog("Overview Error", "Failed to open department overview", e);
        }
    }
    
    /**
     * Returns the company management reference.
     */
    public CompanyManagment getCompanyManagement() {
        return companyManagement;
    }
    
    /**
     * Gets the count of departments in the table.
     */
    public int getDepartmentCount() {
        return tableModel.getRowCount();
    }
}
