package Runner.ui;

import Utils.Logger;
import Shapes.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UI Manager for Shape operations.
 * Provides interface for creating, managing, and testing shape objects.
 * 
 * @author Senior Java Developer
 * @version 1.0
 */
public class ShapeManagerUI {
    private static final Logger logger = new Logger("ShapeManagerUI");
    
    private final MainApplicationSwing parentApp;
    private final List<Shape2D> shapes2D;
    private final List<Shape3D> shapes3D;
    
    // UI Components
    private JPanel mainPanel;
    private JTabbedPane shapeTabPane;
    private JList<Shape2D> shape2DList;
    private JList<Shape3D> shape3DList;
    private DefaultListModel<Shape2D> list2DModel;
    private DefaultListModel<Shape3D> list3DModel;
    
    // 2D Shape fields
    private JComboBox<String> shape2DTypeCombo;
    private JTextField width2DField;
    private JTextField height2DField;
    private JTextField radius2DField;
    private JTextField sideAField;
    private JTextField sideBField;
    private JTextField sideCField;
    
    // 3D Shape fields
    private JComboBox<String> shape3DTypeCombo;
    private JTextField width3DField;
    private JTextField height3DField;
    private JTextField depth3DField;
    private JTextField radius3DField;
    
    /**
     * Constructor for ShapeManagerUI.
     * @param parentApp Reference to the main application
     */
    public ShapeManagerUI(MainApplicationSwing parentApp) {
        this.parentApp = parentApp;
        this.shapes2D = new ArrayList<>();
        this.shapes3D = new ArrayList<>();
        this.list2DModel = new DefaultListModel<>();
        this.list3DModel = new DefaultListModel<>();
        logger.info("ShapeManagerUI initialized");
    }
    
    /**
     * Creates and returns the main content panel for shape management.
     * @return JPanel containing all shape management controls
     */
    public JPanel createContent() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create tabbed pane for 2D and 3D shapes
        shapeTabPane = new JTabbedPane();
        shapeTabPane.addTab("2D Shapes", create2DShapePanel());
        shapeTabPane.addTab("3D Shapes", create3DShapePanel());
        
        mainPanel.add(shapeTabPane, BorderLayout.CENTER);
        
        // Create action panel
        JPanel actionPanel = createShapeActionPanel();
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Creates the 2D shapes management panel.
     */
    private JPanel create2DShapePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // List panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(new TitledBorder("2D Shapes"));
        
        shape2DList = new JList<>(list2DModel);
        shape2DList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shape2DList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Shape2D selected = shape2DList.getSelectedValue();
                if (selected != null) {
                    populateForm2DFromShape(selected);
                }
            }
        });
        
        JScrollPane scrollPane2D = new JScrollPane(shape2DList);
        scrollPane2D.setPreferredSize(new Dimension(250, 300));
        listPanel.add(scrollPane2D, BorderLayout.CENTER);
        
        // Form panel
        JPanel formPanel = create2DShapeFormPanel();
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(listPanel);
        splitPane.setRightComponent(formPanel);
        splitPane.setDividerLocation(250);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the 2D shape form panel.
     */
    private JPanel create2DShapeFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("2D Shape Properties"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Shape Type
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Shape Type:"), gbc);
        gbc.gridx = 1;
        shape2DTypeCombo = new JComboBox<>(new String[]{
            "Rectangle", "Circle", "Triangle", "Square"
        });
        shape2DTypeCombo.addActionListener(e -> toggle2DFieldsBasedOnType());
        panel.add(shape2DTypeCombo, gbc);
        
        // Width
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Width:"), gbc);
        gbc.gridx = 1;
        width2DField = new JTextField(10);
        panel.add(width2DField, gbc);
        
        // Height
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Height:"), gbc);
        gbc.gridx = 1;
        height2DField = new JTextField(10);
        panel.add(height2DField, gbc);
        
        // Radius (for Circle)
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Radius:"), gbc);
        gbc.gridx = 1;
        radius2DField = new JTextField(10);
        panel.add(radius2DField, gbc);
        
        // Triangle sides
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Side A:"), gbc);
        gbc.gridx = 1;
        sideAField = new JTextField(10);
        panel.add(sideAField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Side B:"), gbc);
        gbc.gridx = 1;
        sideBField = new JTextField(10);
        panel.add(sideBField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Side C:"), gbc);
        gbc.gridx = 1;
        sideCField = new JTextField(10);
        panel.add(sideCField, gbc);
        
        // Results display
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        JTextArea results2D = new JTextArea(4, 20);
        results2D.setEditable(false);
        results2D.setBorder(BorderFactory.createTitledBorder("Calculations"));
        panel.add(new JScrollPane(results2D), gbc);
        
        toggle2DFieldsBasedOnType(); // Initial setup
        
        return panel;
    }
    
    /**
     * Creates the 3D shapes management panel.
     */
    private JPanel create3DShapePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // List panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(new TitledBorder("3D Shapes"));
        
        shape3DList = new JList<>(list3DModel);
        shape3DList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shape3DList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Shape3D selected = shape3DList.getSelectedValue();
                if (selected != null) {
                    populateForm3DFromShape(selected);
                }
            }
        });
        
        JScrollPane scrollPane3D = new JScrollPane(shape3DList);
        scrollPane3D.setPreferredSize(new Dimension(250, 300));
        listPanel.add(scrollPane3D, BorderLayout.CENTER);
        
        // Form panel
        JPanel formPanel = create3DShapeFormPanel();
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(listPanel);
        splitPane.setRightComponent(formPanel);
        splitPane.setDividerLocation(250);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the 3D shape form panel.
     */
    private JPanel create3DShapeFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("3D Shape Properties"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Shape Type
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Shape Type:"), gbc);
        gbc.gridx = 1;
        shape3DTypeCombo = new JComboBox<>(new String[]{
            "Cuboid", "Sphere", "Cylinder", "Cone"
        });
        shape3DTypeCombo.addActionListener(e -> toggle3DFieldsBasedOnType());
        panel.add(shape3DTypeCombo, gbc);
        
        // Width
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Width:"), gbc);
        gbc.gridx = 1;
        width3DField = new JTextField(10);
        panel.add(width3DField, gbc);
        
        // Height
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Height:"), gbc);
        gbc.gridx = 1;
        height3DField = new JTextField(10);
        panel.add(height3DField, gbc);
        
        // Depth
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Depth:"), gbc);
        gbc.gridx = 1;
        depth3DField = new JTextField(10);
        panel.add(depth3DField, gbc);
        
        // Radius (for Sphere, Cylinder, Cone)
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Radius:"), gbc);
        gbc.gridx = 1;
        radius3DField = new JTextField(10);
        panel.add(radius3DField, gbc);
        
        // Results display
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        JTextArea results3D = new JTextArea(4, 20);
        results3D.setEditable(false);
        results3D.setBorder(BorderFactory.createTitledBorder("Calculations"));
        panel.add(new JScrollPane(results3D), gbc);
        
        toggle3DFieldsBasedOnType(); // Initial setup
        
        return panel;
    }
    
    /**
     * Creates the action panel with buttons.
     */
    private JPanel createShapeActionPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton create2DButton = new JButton("Create 2D Shape");
        create2DButton.addActionListener(e -> create2DShape());
        
        JButton create3DButton = new JButton("Create 3D Shape");
        create3DButton.addActionListener(e -> create3DShape());
        
        JButton calculateButton = new JButton("Calculate Properties");
        calculateButton.addActionListener(e -> calculateShapeProperties());
        
        JButton clearButton = new JButton("Clear Forms");
        clearButton.addActionListener(e -> clearForms());
        
        JButton testButton = new JButton("Test Shape Operations");
        testButton.addActionListener(e -> testShapeOperations());
        
        panel.add(create2DButton);
        panel.add(create3DButton);
        panel.add(calculateButton);
        panel.add(clearButton);
        panel.add(testButton);
        
        return panel;
    }
    
    /**
     * Toggles 2D field visibility based on selected shape type.
     */
    private void toggle2DFieldsBasedOnType() {
        String type = (String) shape2DTypeCombo.getSelectedItem();
        
        // Reset all fields
        width2DField.setEnabled(false);
        height2DField.setEnabled(false);
        radius2DField.setEnabled(false);
        sideAField.setEnabled(false);
        sideBField.setEnabled(false);
        sideCField.setEnabled(false);
        
        switch (type) {
            case "Rectangle":
                width2DField.setEnabled(true);
                height2DField.setEnabled(true);
                break;
            case "Square":
                width2DField.setEnabled(true);
                break;
            case "Circle":
                radius2DField.setEnabled(true);
                break;
            case "Triangle":
                sideAField.setEnabled(true);
                sideBField.setEnabled(true);
                sideCField.setEnabled(true);
                break;
        }
    }
    
    /**
     * Toggles 3D field visibility based on selected shape type.
     */
    private void toggle3DFieldsBasedOnType() {
        String type = (String) shape3DTypeCombo.getSelectedItem();
        
        // Reset all fields
        width3DField.setEnabled(false);
        height3DField.setEnabled(false);
        depth3DField.setEnabled(false);
        radius3DField.setEnabled(false);
        
        switch (type) {
            case "Cuboid":
                width3DField.setEnabled(true);
                height3DField.setEnabled(true);
                depth3DField.setEnabled(true);
                break;
            case "Sphere":
                radius3DField.setEnabled(true);
                break;
            case "Cylinder":
            case "Cone":
                radius3DField.setEnabled(true);
                height3DField.setEnabled(true);
                break;
        }
    }
    
    /**
     * Creates a new 2D shape based on form data.
     */
    private void create2DShape() {
        try {
            String type = (String) shape2DTypeCombo.getSelectedItem();
            Shape2D shape = null;
            
            switch (type) {
                case "Rectangle":
                    double width = Double.parseDouble(width2DField.getText().trim());
                    double height = Double.parseDouble(height2DField.getText().trim());
                    shape = new Shapes.Rectangle(width, height);
                    break;
                case "Square":
                    double side = Double.parseDouble(width2DField.getText().trim());
                    shape = new Square(side);
                    break;
                case "Circle":
                    double radius = Double.parseDouble(radius2DField.getText().trim());
                    shape = new Circle(radius);
                    break;
                case "Triangle":
                    double sideA = Double.parseDouble(sideAField.getText().trim());
                    double sideB = Double.parseDouble(sideBField.getText().trim());
                    double sideC = Double.parseDouble(sideCField.getText().trim());
                    shape = new Triangle(sideA, sideB, sideC);
                    break;
            }
            
            if (shape != null) {
                shapes2D.add(shape);
                list2DModel.addElement(shape);
                
                parentApp.appendToOutput("Created 2D shape: " + shape.getClass().getSimpleName() + 
                                       " - Area: " + String.format("%.2f", shape.getArea()) + 
                                       ", Perimeter: " + String.format("%.2f", shape.getPerimeter()) + "\n");
                logger.info("Created 2D shape: " + shape);
            }
            
        } catch (NumberFormatException e) {
            parentApp.showErrorDialog("Input Error", "Please enter valid numeric values", e);
        } catch (Exception e) {
            parentApp.showErrorDialog("Creation Error", "Failed to create 2D shape", e);
        }
    }
    
    /**
     * Creates a new 3D shape based on form data.
     */
    private void create3DShape() {
        try {
            String type = (String) shape3DTypeCombo.getSelectedItem();
            Shape3D shape = null;
            
            switch (type) {
                case "Cuboid":
                    double width = Double.parseDouble(width3DField.getText().trim());
                    double height = Double.parseDouble(height3DField.getText().trim());
                    double depth = Double.parseDouble(depth3DField.getText().trim());
                    shape = new Cuboid(width, height, depth);
                    break;
                case "Sphere":
                    double radius = Double.parseDouble(radius3DField.getText().trim());
                    shape = new Sphere(radius);
                    break;
                case "Cylinder":
                    double cylRadius = Double.parseDouble(radius3DField.getText().trim());
                    double cylHeight = Double.parseDouble(height3DField.getText().trim());
                    shape = new Cylinder(cylRadius, cylHeight);
                    break;
                case "Cone":
                    double coneRadius = Double.parseDouble(radius3DField.getText().trim());
                    double coneHeight = Double.parseDouble(height3DField.getText().trim());
                    shape = new Cone(coneRadius, coneHeight);
                    break;
            }
            
            if (shape != null) {
                shapes3D.add(shape);
                list3DModel.addElement(shape);
                
                parentApp.appendToOutput("Created 3D shape: " + shape.getClass().getSimpleName() + 
                                       " - Volume: " + String.format("%.2f", shape.getVolume()) + 
                                       ", Surface Area: " + String.format("%.2f", shape.getSurfaceArea()) + "\n");
                logger.info("Created 3D shape: " + shape);
            }
            
        } catch (NumberFormatException e) {
            parentApp.showErrorDialog("Input Error", "Please enter valid numeric values", e);
        } catch (Exception e) {
            parentApp.showErrorDialog("Creation Error", "Failed to create 3D shape", e);
        }
    }
    
    /**
     * Calculates and displays properties of selected shapes.
     */
    private void calculateShapeProperties() {
        StringBuilder results = new StringBuilder();
        results.append("=== Shape Property Calculations ===\n");
        
        // Calculate 2D shapes
        if (!shapes2D.isEmpty()) {
            results.append("\n2D Shapes:\n");
            for (Shape2D shape : shapes2D) {
                results.append(String.format("%s - Area: %.2f, Perimeter: %.2f\n",
                    shape.getClass().getSimpleName(), shape.getArea(), shape.getPerimeter()));
            }
        }
        
        // Calculate 3D shapes
        if (!shapes3D.isEmpty()) {
            results.append("\n3D Shapes:\n");
            for (Shape3D shape : shapes3D) {
                results.append(String.format("%s - Volume: %.2f, Surface Area: %.2f\n",
                    shape.getClass().getSimpleName(), shape.getVolume(), shape.getSurfaceArea()));
            }
        }
        
        results.append("=== Calculations Complete ===\n\n");
        parentApp.appendToOutput(results.toString());
    }
    
    /**
     * Populates 2D form from selected shape.
     */
    private void populateForm2DFromShape(Shape2D shape) {
        // Implementation would depend on available getters in shape classes
        if (shape instanceof Shapes.Rectangle) {
            shape2DTypeCombo.setSelectedItem("Rectangle");
            // Would need getWidth() and getHeight() methods
        } else if (shape instanceof Circle) {
            shape2DTypeCombo.setSelectedItem("Circle");
            // Would need getRadius() method
        }
        toggle2DFieldsBasedOnType();
    }
    
    /**
     * Populates 3D form from selected shape.
     */
    private void populateForm3DFromShape(Shape3D shape) {
        // Implementation would depend on available getters in shape classes
        if (shape instanceof Sphere) {
            shape3DTypeCombo.setSelectedItem("Sphere");
            // Would need getRadius() method
        } else if (shape instanceof Cuboid) {
            shape3DTypeCombo.setSelectedItem("Cuboid");
            // Would need getWidth(), getHeight(), getDepth() methods
        }
        toggle3DFieldsBasedOnType();
    }
    
    /**
     * Clears all form fields.
     */
    private void clearForms() {
        // Clear 2D fields
        width2DField.setText("");
        height2DField.setText("");
        radius2DField.setText("");
        sideAField.setText("");
        sideBField.setText("");
        sideCField.setText("");
        
        // Clear 3D fields
        width3DField.setText("");
        height3DField.setText("");
        depth3DField.setText("");
        radius3DField.setText("");
        
        // Reset selections
        shape2DList.clearSelection();
        shape3DList.clearSelection();
    }
    
    /**
     * Tests various shape operations.
     */
    private void testShapeOperations() {
        parentApp.appendToOutput("=== Testing Shape Operations ===\n");
        
        try {
            // Test 2D shapes
            parentApp.appendToOutput("Testing 2D shapes...\n");
            Shapes.Rectangle rect = new Shapes.Rectangle(5.0, 3.0);
            Circle circle = new Circle(2.5);
            Triangle triangle = new Triangle(3.0, 4.0, 5.0);
            
            parentApp.appendToOutput("Rectangle (5x3): Area=" + rect.getArea() + ", Perimeter=" + rect.getPerimeter() + "\n");
            parentApp.appendToOutput("Circle (r=2.5): Area=" + String.format("%.2f", circle.getArea()) + 
                                   ", Perimeter=" + String.format("%.2f", circle.getPerimeter()) + "\n");
            parentApp.appendToOutput("Triangle (3,4,5): Area=" + String.format("%.2f", triangle.getArea()) + 
                                   ", Perimeter=" + triangle.getPerimeter() + "\n");
            
            // Test 3D shapes
            parentApp.appendToOutput("\nTesting 3D shapes...\n");
            Cuboid cuboid = new Cuboid(2.0, 3.0, 4.0);
            Sphere sphere = new Sphere(2.0);
            Cylinder cylinder = new Cylinder(1.5, 4.0);
            
            parentApp.appendToOutput("Cuboid (2x3x4): Volume=" + cuboid.getVolume() + 
                                   ", Surface Area=" + cuboid.getSurfaceArea() + "\n");
            parentApp.appendToOutput("Sphere (r=2): Volume=" + String.format("%.2f", sphere.getVolume()) + 
                                   ", Surface Area=" + String.format("%.2f", sphere.getSurfaceArea()) + "\n");
            parentApp.appendToOutput("Cylinder (r=1.5, h=4): Volume=" + String.format("%.2f", cylinder.getVolume()) + 
                                   ", Surface Area=" + String.format("%.2f", cylinder.getSurfaceArea()) + "\n");
            
            parentApp.appendToOutput("=== Shape Tests Completed ===\n\n");
            
        } catch (Exception e) {
            parentApp.showErrorDialog("Test Error", "Error during shape testing", e);
        }
    }
    
    /**
     * Returns the list of created 2D shapes.
     */
    public List<Shape2D> getShapes2D() {
        return new ArrayList<>(shapes2D);
    }
    
    /**
     * Returns the list of created 3D shapes.
     */
    public List<Shape3D> getShapes3D() {
        return new ArrayList<>(shapes3D);
    }
    
    /**
     * Resets the manager to initial state.
     */
    public void reset() {
        shapes2D.clear();
        shapes3D.clear();
        list2DModel.clear();
        list3DModel.clear();
        clearForms();
        logger.info("ShapeManagerUI reset");
    }
}
