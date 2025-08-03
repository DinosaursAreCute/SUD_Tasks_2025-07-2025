package Runner.ui;

import Utils.Logger;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

/**
 * UI Manager for Test operations.
 * Provides interface for running automated tests on all system components.
 * 
 * @author Senior Java Developer
 * @version 1.0
 */
public class TestRunnerUI {
    private static final Logger logger = new Logger("TestRunnerUI");
    
    private final MainApplicationSwing parentApp;
    
    // UI Components
    private JPanel mainPanel;
    private JList<String> testClassList;
    private DefaultListModel<String> testListModel;
    private JTextArea testResultsArea;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    // Test classes
    private final String[] TEST_CLASSES = {
        "Employees.TestEmployee",
        "Employees.TestDepartment", 
        "Shapes.TestShapes",
        "Vehicles.TestVehicle",
        "CompanyManagement.CompanyManagementTest"
    };
    
    /**
     * Constructor for TestRunnerUI.
     * @param parentApp Reference to the main application
     */
    public TestRunnerUI(MainApplicationSwing parentApp) {
        this.parentApp = parentApp;
        this.testListModel = new DefaultListModel<>();
        initializeTestList();
        logger.info("TestRunnerUI initialized");
    }
    
    /**
     * Initializes the test list with available test classes.
     */
    private void initializeTestList() {
        for (String testClass : TEST_CLASSES) {
            testListModel.addElement(testClass);
        }
    }
    
    /**
     * Creates and returns the main content panel for test management.
     * @return JPanel containing all test management controls
     */
    public JPanel createContent() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create test list panel
        JPanel testListPanel = createTestListPanel();
        
        // Create results panel
        JPanel resultsPanel = createResultsPanel();
        
        // Create control panel
        JPanel controlPanel = createControlPanel();
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(testListPanel);
        splitPane.setRightComponent(resultsPanel);
        splitPane.setDividerLocation(300);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Creates the test list panel.
     */
    private JPanel createTestListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Available Test Classes"));
        
        testClassList = new JList<>(testListModel);
        testClassList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        testClassList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateTestInfo();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(testClassList);
        scrollPane.setPreferredSize(new Dimension(280, 400));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add test info panel
        JPanel infoPanel = createTestInfoPanel();
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates the test information panel.
     */
    private JPanel createTestInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Test Information"));
        
        JTextArea infoArea = new JTextArea("""
                Test Classes Overview:
                
                • TestEmployee: Tests employee hierarchy and inheritance
                • TestDepartment: Tests department management functionality
                • TestShapes: Tests geometric shape calculations
                • TestVehicle: Tests vehicle management and GPS functionality
                • CompanyManagementTest: Tests integrated company operations
                
                Select one or more test classes and click 'Run Selected Tests'
                or use 'Run All Tests' to execute the complete test suite.
                """);
        infoArea.setEditable(false);
        infoArea.setWrapStyleWord(true);
        infoArea.setLineWrap(true);
        infoArea.setOpaque(false);
        infoArea.setFont(infoArea.getFont().deriveFont(Font.PLAIN, 11f));
        
        panel.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(280, 150));
        
        return panel;
    }
    
    /**
     * Creates the results panel.
     */
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Test Results"));
        
        testResultsArea = new JTextArea();
        testResultsArea.setEditable(false);
        testResultsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        testResultsArea.setBackground(Color.BLACK);
        testResultsArea.setForeground(Color.GREEN);
        
        JScrollPane scrollPane = new JScrollPane(testResultsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add progress panel
        JPanel progressPanel = createProgressPanel();
        panel.add(progressPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates the progress panel.
     */
    private JPanel createProgressPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("Ready");
        
        statusLabel = new JLabel("Ready to run tests");
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC));
        
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates the control panel with buttons.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton runSelectedButton = new JButton("Run Selected Tests");
        runSelectedButton.addActionListener(e -> runSelectedTests());
        
        JButton runAllButton = new JButton("Run All Tests");
        runAllButton.addActionListener(e -> runAllTests());
        
        JButton clearResultsButton = new JButton("Clear Results");
        clearResultsButton.addActionListener(e -> clearResults());
        
        JButton exportResultsButton = new JButton("Export Results");
        exportResultsButton.addActionListener(e -> exportResults());
        
        JButton performanceTestButton = new JButton("Performance Test");
        performanceTestButton.addActionListener(e -> runPerformanceTests());
        
        panel.add(runSelectedButton);
        panel.add(runAllButton);
        panel.add(clearResultsButton);
        panel.add(exportResultsButton);
        panel.add(performanceTestButton);
        
        return panel;
    }
    
    /**
     * Updates test information based on selection.
     */
    private void updateTestInfo() {
        java.util.List<String> selected = testClassList.getSelectedValuesList();
        if (!selected.isEmpty()) {
            statusLabel.setText("Selected " + selected.size() + " test class(es)");
        } else {
            statusLabel.setText("No test classes selected");
        }
    }
    
    /**
     * Runs the selected test classes.
     */
    private void runSelectedTests() {
        java.util.List<String> selectedTests = testClassList.getSelectedValuesList();
        if (selectedTests.isEmpty()) {
            parentApp.showWarningDialog("Selection Error", "Please select at least one test class.");
            return;
        }
        
        // Run tests in background thread
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("=== Running Selected Tests ===\n");
                progressBar.setMaximum(selectedTests.size());
                progressBar.setValue(0);
                
                for (int i = 0; i < selectedTests.size(); i++) {
                    String testClass = selectedTests.get(i);
                    statusLabel.setText("Running: " + testClass);
                    publish("Running test: " + testClass + "\n");
                    
                    String result = runSingleTest(testClass);
                    publish(result + "\n");
                    
                    progressBar.setValue(i + 1);
                }
                
                return null;
            }
            
            @Override
            protected void process(java.util.List<String> chunks) {
                for (String chunk : chunks) {
                    testResultsArea.append(chunk);
                    testResultsArea.setCaretPosition(testResultsArea.getDocument().getLength());
                }
            }
            
            @Override
            protected void done() {
                statusLabel.setText("Selected tests completed");
                progressBar.setString("Completed");
                parentApp.appendToOutput("Selected test execution completed\n");
            }
        };
        
        worker.execute();
    }
    
    /**
     * Runs all available test classes.
     */
    public void runAllTests() {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("=== Running All Tests ===\n");
                progressBar.setMaximum(TEST_CLASSES.length);
                progressBar.setValue(0);
                
                for (int i = 0; i < TEST_CLASSES.length; i++) {
                    String testClass = TEST_CLASSES[i];
                    statusLabel.setText("Running: " + testClass);
                    publish("Running test: " + testClass + "\n");
                    
                    String result = runSingleTest(testClass);
                    publish(result + "\n");
                    
                    progressBar.setValue(i + 1);
                }
                
                publish("=== All Tests Completed ===\n");
                return null;
            }
            
            @Override
            protected void process(java.util.List<String> chunks) {
                for (String chunk : chunks) {
                    testResultsArea.append(chunk);
                    testResultsArea.setCaretPosition(testResultsArea.getDocument().getLength());
                }
            }
            
            @Override
            protected void done() {
                statusLabel.setText("All tests completed");
                progressBar.setString("All Tests Completed");
                parentApp.appendToOutput("Complete test suite execution finished\n");
            }
        };
        
        worker.execute();
    }
    
    /**
     * Runs a single test class and captures its output.
     */
    private String runSingleTest(String className) {
        try {
            // Capture console output
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            PrintStream originalErr = System.err;
            PrintStream captureStream = new PrintStream(baos);
            
            System.setOut(captureStream);
            System.setErr(captureStream);
            
            long startTime = System.currentTimeMillis();
            
            try {
                // Load and run the test class
                Class<?> testClass = Class.forName(className);
                Method mainMethod = testClass.getMethod("main", String[].class);
                mainMethod.invoke(null, (Object) new String[]{});
                
            } catch (ClassNotFoundException e) {
                return "ERROR: Test class not found: " + className;
            } catch (NoSuchMethodException e) {
                return "ERROR: No main method found in: " + className;
            } catch (Exception e) {
                return "ERROR running " + className + ": " + e.getMessage();
            } finally {
                // Restore original streams
                System.setOut(originalOut);
                System.setErr(originalErr);
            }
            
            long endTime = System.currentTimeMillis();
            String output = baos.toString();
            
            return String.format("Test: %s (%.2f seconds)\nOutput:\n%s\n--- End of %s ---\n", 
                                className, (endTime - startTime) / 1000.0, output, className);
            
        } catch (Exception e) {
            logger.error("Error running test " + className + ": " + e.getMessage());
            return "ERROR: Failed to execute test " + className + ": " + e.getMessage();
        }
    }
    
    /**
     * Runs performance tests to measure system performance.
     */
    private void runPerformanceTests() {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("=== Performance Testing ===\n");
                statusLabel.setText("Running performance tests...");
                
                // Test object creation performance
                publish("Testing object creation performance...\n");
                
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < 1000; i++) {
                    try {
                        new Employees.OfficeWorker(5000 + i, "Test Employee " + i, 3000);
                    } catch (Exception ignored) {
                        // Ignore validation errors for performance testing
                    }
                }
                long endTime = System.currentTimeMillis();
                publish("Created 1000 OfficeWorker objects in " + (endTime - startTime) + "ms\n");
                
                // Test shape calculations
                publish("Testing shape calculation performance...\n");
                startTime = System.currentTimeMillis();
                for (int i = 0; i < 1000; i++) {
                    Shapes.Circle circle = new Shapes.Circle(i + 1);
                    circle.getArea();
                    circle.getPerimeter();
                }
                endTime = System.currentTimeMillis();
                publish("Performed 2000 shape calculations in " + (endTime - startTime) + "ms\n");
                
                // Memory usage
                Runtime runtime = Runtime.getRuntime();
                long totalMemory = runtime.totalMemory();
                long freeMemory = runtime.freeMemory();
                long usedMemory = totalMemory - freeMemory;
                
                publish(String.format("Memory usage: Used %.2f MB / Total %.2f MB\n", 
                                    usedMemory / (1024.0 * 1024.0), totalMemory / (1024.0 * 1024.0)));
                
                publish("=== Performance Tests Completed ===\n");
                return null;
            }
            
            @Override
            protected void process(java.util.List<String> chunks) {
                for (String chunk : chunks) {
                    testResultsArea.append(chunk);
                    testResultsArea.setCaretPosition(testResultsArea.getDocument().getLength());
                }
            }
            
            @Override
            protected void done() {
                statusLabel.setText("Performance tests completed");
                parentApp.appendToOutput("Performance testing completed\n");
            }
        };
        
        worker.execute();
    }
    
    /**
     * Clears the test results area.
     */
    private void clearResults() {
        testResultsArea.setText("");
        progressBar.setValue(0);
        progressBar.setString("Ready");
        statusLabel.setText("Results cleared");
    }
    
    /**
     * Exports test results to a file.
     */
    private void exportResults() {
        if (testResultsArea.getText().trim().isEmpty()) {
            parentApp.showWarningDialog("Export Error", "No test results to export.");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File("test_results.txt"));
        
        int result = fileChooser.showSaveDialog(parentApp);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
                    writer.write("Test Results Export - " + new java.util.Date() + "\n");
                    writer.write("=" .repeat(50) + "\n\n");
                    writer.write(testResultsArea.getText());
                }
                parentApp.showInfoDialog("Export Success", "Test results exported to: " + file.getAbsolutePath());
                logger.info("Test results exported to: " + file.getAbsolutePath());
            } catch (java.io.IOException e) {
                parentApp.showErrorDialog("Export Failed", "Could not export test results", e);
            }
        }
    }
    
    /**
     * Resets the test runner to initial state.
     */
    public void reset() {
        clearResults();
        testClassList.clearSelection();
        statusLabel.setText("Test runner reset");
        logger.info("TestRunnerUI reset");
    }
}
