package Runner.ui;

import CompanyManagement.CompanyManagment;
import Utils.Logger;
import Vehicles.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Enhanced UI Manager for Vehicle operations with table view.
 * Provides interface for creating, managing, and testing vehicle objects.
 * 
 * @author Senior Java Developer
 * @version 2.0
 */
public class VehicleManagerUI {
    private static final Logger logger = new Logger("VehicleManagerUI");
    
    private final MainApplicationSwing parentApp;
    private CompanyManagment companyManagement;
    
    // UI Components
    private JPanel mainPanel;
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> tableSorter;
    private JTextField tankSizeField;
    private JTextField maxSpeedField;
    private JTextField longitudeField;
    private JTextField latitudeField;
    private JComboBox<String> vehicleTypeCombo;
    private JTextField loadingAreaField;
    private JTextField seatCountField;
    private JTextField brandField;
    private JTextField modelField;
    private JTextField yearField;
    private JTextField searchField;
    
    // Table column indices
    private static final int COL_TYPE = 0;
    private static final int COL_BRAND = 1;
    private static final int COL_MODEL = 2;
    private static final int COL_YEAR = 3;
    private static final int COL_CAPACITY = 4;
    private static final int COL_STATUS = 5;

    /**
     * Constructor for VehicleManagerUI.
     * @param parentApp Reference to the main application
     */
    public VehicleManagerUI(MainApplicationSwing parentApp) {
        this.parentApp = parentApp;
        initializeTableModel();
        logger.info("VehicleManagerUI initialized with table view");
    }
    
    /**
     * Sets the company management reference for data access.
     */
    public void setCompanyManagement(CompanyManagment companyManagement) {
        this.companyManagement = companyManagement;
        refreshVehicleTable();
    }
    
    /**
     * Initializes the table model with appropriate columns.
     */
    private void initializeTableModel() {
        String[] columnNames = {"Type", "Brand", "Model", "Year", "Capacity/Seats", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        tableSorter = new TableRowSorter<>(tableModel);
    }
    
    /**
     * Creates and returns the main content panel for vehicle management.
     * @return JPanel containing all vehicle management controls
     */
    public JPanel createContent() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search and filter panel
        JPanel searchPanel = createSearchPanel();
        
        // Create vehicle table panel
        JPanel tablePanel = createVehicleTablePanel();
        
        // Create form panel
        JPanel formPanel = createVehicleFormPanel();
        
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
        searchField.setToolTipText("Search vehicles by type, brand, model, or year");
        panel.add(searchField);
        
        JButton searchButton = new JButton("Filter");
        searchButton.addActionListener(e -> filterVehicleTable());
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
     * Creates the vehicle table panel with enhanced features.
     */
    private JPanel createVehicleTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Vehicle Fleet"));
        
        // Create table
        vehicleTable = new JTable(tableModel);
        vehicleTable.setRowSorter(tableSorter);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehicleTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Set column widths
        vehicleTable.getColumnModel().getColumn(COL_TYPE).setPreferredWidth(80);
        vehicleTable.getColumnModel().getColumn(COL_BRAND).setPreferredWidth(100);
        vehicleTable.getColumnModel().getColumn(COL_MODEL).setPreferredWidth(120);
        vehicleTable.getColumnModel().getColumn(COL_YEAR).setPreferredWidth(60);
        vehicleTable.getColumnModel().getColumn(COL_CAPACITY).setPreferredWidth(100);
        vehicleTable.getColumnModel().getColumn(COL_STATUS).setPreferredWidth(80);
        
        // Add selection listener
        vehicleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = vehicleTable.getSelectedRow();
                if (selectedRow >= 0) {
                    // Convert view row to model row due to sorting
                    int modelRow = vehicleTable.convertRowIndexToModel(selectedRow);
                    populateFormFromTableRow();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setPreferredSize(new Dimension(580, 400));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel totalLabel = new JLabel("Total Vehicles: 0");
        summaryPanel.add(totalLabel);
        
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Filters the vehicle table based on search text.
     */
    private void filterVehicleTable() {
        String searchText = searchField.getText().toLowerCase().trim();
        List<Vehicle> vehicles = companyManagement.getVehicles();
        
        if (searchText.isEmpty()) {
            // Show all vehicles
            tableModel.setRowCount(0);
            for (Vehicle vehicle : vehicles) {
                addVehicleToTable(vehicle);
            }
        } else {
            // Filter vehicles
            tableModel.setRowCount(0);
            for (Vehicle vehicle : vehicles) {
                String vehicleText = getVehicleSearchText(vehicle).toLowerCase();
                if (vehicleText.contains(searchText)) {
                    addVehicleToTable(vehicle);
                }
            }
        }
    }
    
    /**
     * Gets searchable text for a vehicle.
     */
    private String getVehicleSearchText(Vehicle vehicle) {
        StringBuilder sb = new StringBuilder();
        sb.append(vehicle.getClass().getSimpleName()).append(" ");
        
        if (vehicle instanceof Truck truck) {
            sb.append(truck.getBrand()).append(" ").append(truck.getModel()).append(" ");
        } else if (vehicle instanceof Bus bus) {
            sb.append(bus.getBrand()).append(" ").append(bus.getModel()).append(" ");
        } else if (vehicle instanceof PassengerVehicle pv) {
            sb.append(pv.getBrand()).append(" ").append(pv.getModel()).append(" ");
        }
        
        return sb.toString();
    }
    
    /**
     * Adds a vehicle to the table model.
     */
    private void addVehicleToTable(Vehicle vehicle) {
        String type = vehicle.getClass().getSimpleName();
        String brand = "";
        String model = "";
        String year = "";
        String capacitySeats = "";
        String status = "Active";
        
        if (vehicle instanceof Truck truck) {
            brand = truck.getBrand();
            model = truck.getModel();
            year = String.valueOf(truck.getYear());
            capacitySeats = truck.getLoadingArea() + " m²";
        } else if (vehicle instanceof Bus bus) {
            brand = bus.getBrand();
            model = bus.getModel();
            year = String.valueOf(bus.getYear());
            capacitySeats = bus.getSeats() + " seats";
        } else if (vehicle instanceof PassengerVehicle pv) {
            brand = pv.getBrand();
            model = pv.getModel();
            year = String.valueOf(pv.getYear());
            capacitySeats = pv.getSeats() + " seats";
        }
        
        tableModel.addRow(new Object[]{type, brand, model, year, capacitySeats, status});
    }
    
    /**
     * Populates form from selected table row.
     */
    private void populateFormFromTableRow() {
        int selectedRow = vehicleTable.getSelectedRow();
        List<Vehicle> vehicles = companyManagement.getVehicles();
        
        if (selectedRow >= 0 && selectedRow < vehicles.size()) {
            Vehicle vehicle = vehicles.get(selectedRow);
            populateFormFromVehicle(vehicle);
        }
    }
    
    /**
     * Refreshes the vehicle table display.
     */
    private void refreshVehicleTable() {
        tableModel.setRowCount(0);
        List<Vehicle> vehicles = companyManagement.getVehicles();
        for (Vehicle vehicle : vehicles) {
            addVehicleToTable(vehicle);
        }
    }

    /**
     * Creates the vehicle form panel.
     */
    private JPanel createVehicleFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Vehicle Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Vehicle Type
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Vehicle Type:"), gbc);
        gbc.gridx = 1;
        vehicleTypeCombo = new JComboBox<>(new String[]{
            "Truck", "Bus", "PassengerVehicle"
        });
        vehicleTypeCombo.addActionListener(e -> toggleFieldsBasedOnType());
        panel.add(vehicleTypeCombo, gbc);
        
        // Tank Size
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Tank Size (L):"), gbc);
        gbc.gridx = 1;
        tankSizeField = new JTextField(10);
        panel.add(tankSizeField, gbc);
        
        // Max Speed
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Max Speed (km/h):"), gbc);
        gbc.gridx = 1;
        maxSpeedField = new JTextField(10);
        panel.add(maxSpeedField, gbc);
        
        // GPS Position
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Longitude (0-359°):"), gbc);
        gbc.gridx = 1;
        longitudeField = new JTextField(10);
        panel.add(longitudeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Latitude (-90 to 90°):"), gbc);
        gbc.gridx = 1;
        latitudeField = new JTextField(10);
        panel.add(latitudeField, gbc);
        
        // Loading Area (for Truck)
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Loading Area (m²):"), gbc);
        gbc.gridx = 1;
        loadingAreaField = new JTextField(10);
        panel.add(loadingAreaField, gbc);
        
        // Seat Count (for Bus/PassengerVehicle)
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Seat Count:"), gbc);
        gbc.gridx = 1;
        seatCountField = new JTextField(10);
        panel.add(seatCountField, gbc);
        
        // Instructions
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        JTextArea instructions = new JTextArea("""
                Vehicle Requirements:
                • Truck: Requires Driver with license C
                • Bus: Requires Driver with license D
                • PassengerVehicle: Requires Driver with license B
                
                Default Driver (ID: 2000) will be assigned.
                GPS coordinates must be valid ranges.
                """);
        instructions.setEditable(false);
        instructions.setOpaque(false);
        instructions.setFont(instructions.getFont().deriveFont(Font.ITALIC, 11f));
        panel.add(instructions, gbc);
        
        toggleFieldsBasedOnType(); // Initial setup
        
        return panel;
    }
    
    /**
     * Creates the action panel with buttons.
     */
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton createButton = new JButton("Create Vehicle");
        createButton.addActionListener(e -> createVehicle());
        
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteVehicle());
        
        JButton clearButton = new JButton("Clear Form");
        clearButton.addActionListener(e -> clearForm());
        
        JButton testButton = new JButton("Test Vehicle Operations");
        testButton.addActionListener(e -> testVehicleOperations());
        
        JButton refuelButton = new JButton("Refuel Selected");
        refuelButton.addActionListener(e -> refuelVehicle());
        
        panel.add(createButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        panel.add(refuelButton);
        panel.add(testButton);
        
        return panel;
    }
    
    /**
     * Toggles field visibility based on selected vehicle type.
     */
    private void toggleFieldsBasedOnType() {
        String type = (String) vehicleTypeCombo.getSelectedItem();
        
        // Reset all fields
        loadingAreaField.setEnabled(false);
        seatCountField.setEnabled(false);
        
        switch (type) {
            case "Truck":
                loadingAreaField.setEnabled(true);
                break;
            case "Bus":
            case "PassengerVehicle":
                seatCountField.setEnabled(true);
                break;
        }
    }
    
    /**
     * Creates a new vehicle based on form data.
     */
    private void createVehicle() {
        try {
            String type = (String) vehicleTypeCombo.getSelectedItem();
            
            // Validate required fields
            if (tankSizeField.getText().trim().isEmpty() || 
                maxSpeedField.getText().trim().isEmpty() ||
                longitudeField.getText().trim().isEmpty() || 
                latitudeField.getText().trim().isEmpty()) {
                parentApp.showWarningDialog("Validation Error", "Tank size, max speed, and GPS coordinates are required.");
                return;
            }
            
            double tankSize = Double.parseDouble(tankSizeField.getText().trim());
            double maxSpeed = Double.parseDouble(maxSpeedField.getText().trim());
            double longitude = Double.parseDouble(longitudeField.getText().trim());
            double latitude = Double.parseDouble(latitudeField.getText().trim());
            
            GPSPosition position = new GPSPosition(longitude, latitude);
            Vehicle vehicle = null;
            
            switch (type) {
                case "Truck":
                    if (loadingAreaField.getText().trim().isEmpty()) {
                        parentApp.showWarningDialog("Validation Error", "Loading area is required for trucks.");
                        return;
                    }
                    double loadingArea = Double.parseDouble(loadingAreaField.getText().trim());
                    vehicle = new Truck(tankSize, maxSpeed, position, loadingArea);
                    break;
                    
                case "Bus":
                    if (seatCountField.getText().trim().isEmpty()) {
                        parentApp.showWarningDialog("Validation Error", "Seat count is required for buses.");
                        return;
                    }
                    int busSeats = Integer.parseInt(seatCountField.getText().trim());
                    vehicle = new Bus(tankSize, maxSpeed, position, busSeats);
                    break;
                    
                case "PassengerVehicle":
                    if (seatCountField.getText().trim().isEmpty()) {
                        parentApp.showWarningDialog("Validation Error", "Seat count is required for passenger vehicles.");
                        return;
                    }
                    int passengerSeats = Integer.parseInt(seatCountField.getText().trim());
                    vehicle = new PassengerVehicle(tankSize, maxSpeed, position, passengerSeats);
                    break;
            }
            
            if (vehicle != null) {
                // Get vehicles from CompanyManagement
                companyManagement.getVehicles().add(vehicle);
                addVehicleToTable(vehicle);
                clearForm();
                
                parentApp.appendToOutput("Created vehicle: " + vehicle + "\n");
                logger.info("Created vehicle: " + vehicle);
            }
            
        } catch (NumberFormatException e) {
            parentApp.showErrorDialog("Input Error", "Please enter valid numeric values", e);
        } catch (Exception e) {
            parentApp.showErrorDialog("Creation Error", "Failed to create vehicle", e);
        }
    }
    
    /**
     * Deletes the selected vehicle.
     */
    private void deleteVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            parentApp.showWarningDialog("Selection Error", "Please select a vehicle to delete.");
            return;
        }
        
        List<Vehicle> vehicles = companyManagement.getVehicles();
        if (selectedRow >= vehicles.size()) {
            parentApp.showWarningDialog("Selection Error", "Invalid vehicle selection.");
            return;
        }
        
        Vehicle selected = vehicles.get(selectedRow);
        
        int result = JOptionPane.showConfirmDialog(
            parentApp,
            "Are you sure you want to delete: " + selected + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            vehicles.remove(selected);
            refreshVehicleTable();
            clearForm();
            
            parentApp.appendToOutput("Deleted vehicle: " + selected + "\n");
            logger.info("Deleted vehicle: " + selected);
        }
    }
    
    /**
     * Refuels the selected vehicle.
     */
    private void refuelVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            parentApp.showWarningDialog("Selection Error", "Please select a vehicle to refuel.");
            return;
        }
        
        List<Vehicle> vehicles = companyManagement.getVehicles();
        if (selectedRow >= vehicles.size()) {
            parentApp.showWarningDialog("Selection Error", "Invalid vehicle selection.");
            return;
        }
        
        Vehicle selected = vehicles.get(selectedRow);
        
        String amountStr = JOptionPane.showInputDialog(
            parentApp,
            "Enter refuel amount (liters):",
            "Refuel Vehicle",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr.trim());
                selected.refuel(amount);
                
                parentApp.appendToOutput("Refueled " + selected.getClass().getSimpleName() + 
                                       " with " + amount + "L. Tank level: " + selected.getTankLevel() + "L\n");
                logger.info("Refueled vehicle: " + selected);
                
            } catch (NumberFormatException e) {
                parentApp.showErrorDialog("Input Error", "Please enter a valid number", e);
            } catch (Exception e) {
                parentApp.showErrorDialog("Refuel Error", "Failed to refuel vehicle", e);
            }
        }
    }
    
    /**
     * Clears the form fields.
     */
    private void clearForm() {
        tankSizeField.setText("");
        maxSpeedField.setText("");
        longitudeField.setText("");
        latitudeField.setText("");
        loadingAreaField.setText("");
        seatCountField.setText("");
        vehicleTypeCombo.setSelectedIndex(0);
        vehicleTable.clearSelection();
    }
    
    /**
     * Populates form fields from selected vehicle.
     */
    private void populateFormFromVehicle(Vehicle vehicle) {
        tankSizeField.setText(String.valueOf(vehicle.getTankSize()));
        // Note: maxSpeed and GPS position getters would need to be added to Vehicle class
        
        if (vehicle instanceof Truck) {
            vehicleTypeCombo.setSelectedItem("Truck");
            // loadingAreaField would need getter method
        } else if (vehicle instanceof Bus) {
            vehicleTypeCombo.setSelectedItem("Bus");
            // seatCountField would need getter method
        } else if (vehicle instanceof PassengerVehicle) {
            vehicleTypeCombo.setSelectedItem("PassengerVehicle");
            // seatCountField would need getter method
        }
        
        toggleFieldsBasedOnType();
    }
    
    /**
     * Tests various vehicle operations.
     */
    private void testVehicleOperations() {
        parentApp.appendToOutput("=== Testing Vehicle Operations ===\n");
        
        try {
            // Test GPS Position
            parentApp.appendToOutput("Testing GPS Position...\n");
            GPSPosition gps1 = new GPSPosition(10.0, 50.0);
            GPSPosition gps2 = new GPSPosition(20.0, 60.0);
            parentApp.appendToOutput("GPS Position 1: " + gps1 + "\n");
            parentApp.appendToOutput("GPS Position 2: " + gps2 + "\n");
            
            // Test Truck
            parentApp.appendToOutput("Testing Truck creation...\n");
            Truck truck = new Truck(100, 120, gps1, 2000);
            parentApp.appendToOutput("Created: " + truck + "\n");
            truck.refuel(50);
            parentApp.appendToOutput("After refueling 50L: Tank level = " + truck.getTankLevel() + "L\n");
            
            // Test Bus
            parentApp.appendToOutput("Testing Bus creation...\n");
            Bus bus = new Bus(150, 100, gps2, 50);
            parentApp.appendToOutput("Created: " + bus + "\n");
            
            // Test PassengerVehicle
            parentApp.appendToOutput("Testing PassengerVehicle creation...\n");
            PassengerVehicle passengerVehicle = new PassengerVehicle(80, 140, gps1, 5);
            parentApp.appendToOutput("Created: " + passengerVehicle + "\n");
            
            // Test vehicle operations
            parentApp.appendToOutput("\nTesting vehicle operations...\n");
            Vehicle[] testVehicles = {truck, bus, passengerVehicle};
            for (Vehicle v : testVehicles) {
                parentApp.appendToOutput("Vehicle: " + v.getClass().getSimpleName() + 
                                       " - Tank: " + v.getTankLevel() + "/" + v.getTankSize() + "L\n");
            }
            
            parentApp.appendToOutput("=== Vehicle Tests Completed ===\n\n");
            
        } catch (Exception e) {
            parentApp.showErrorDialog("Test Error", "Error during vehicle testing", e);
        }
    }
    
    /**
     * Returns the list of created vehicles.
     */
    public List<Vehicle> getVehicles() {
        return companyManagement.getVehicles();
    }
    
    /**
     * Resets the manager to initial state.
     */
    public void reset() {
        companyManagement.getVehicles().clear();
        refreshVehicleTable();
        clearForm();
        logger.info("VehicleManagerUI reset");
    }
}
