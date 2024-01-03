package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookPackage extends JFrame {

    private JComboBox<String> packageDropdown;
    private JTextField passengerCapacityField;
    private JButton continueButton;
    private JButton backButton;

    private List<PassengerDetailsPanel> passengerPanels;

    public BookPackage() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Book Travel Package");
        initializeComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        JPanel headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headingLabel = new JLabel("Book Travel Package");
        headingLabel.setFont(new Font("Montserrat", Font.BOLD, 20));
        headingPanel.add(headingLabel);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(10, 2, 10, 10));

        Font labelFont = new Font("Montserrat", Font.BOLD, 14);
        Font fieldFont = new Font("Montserrat", Font.PLAIN, 16);
        Font buttonFont = new Font("Montserrat", Font.BOLD, 16);

        JLabel packageLabel = new JLabel("Select Travel Package:");
        packageLabel.setFont(labelFont);
        formPanel.add(packageLabel);

        packageDropdown = new JComboBox<>(getTravelPackageNames());
        packageDropdown.setFont(new Font("Montserrat", Font.PLAIN, 14));
        formPanel.add(packageDropdown);

        JLabel passengerCapacityLabel = new JLabel("Passenger Capacity:");
        passengerCapacityLabel.setFont(labelFont);
        formPanel.add(passengerCapacityLabel);

        passengerCapacityField = new JTextField();
        passengerCapacityField.setFont(fieldFont);
        formPanel.add(passengerCapacityField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        continueButton = new JButton("Continue");
        continueButton.setFont(buttonFont);
        continueButton.setPreferredSize(new Dimension(180, 40));
        continueButton.addActionListener(e -> continueToPassengerDetails());
        buttonPanel.add(continueButton);

        backButton = new JButton("Back to Dashboard");
        backButton.setFont(buttonFont);
        backButton.setPreferredSize(new Dimension(180, 40));
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);

        setLayout(new BorderLayout());
        add(headingPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private String[] getTravelPackageNames() {
        List<String> packageNames = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql:///travel", "root", "Ayush@lnmiit")) {
            String query = "SELECT name FROM travel_packages";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        packageNames.add(resultSet.getString("name"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packageNames.toArray(new String[0]);
    }

    private void continueToPassengerDetails() {
        String capacityText = passengerCapacityField.getText();

        if (capacityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the passenger capacity.");
            return;
        }

        int passengerCapacity = Integer.parseInt(capacityText);

        // Ensure the passenger panels list is initialized
        if (passengerPanels == null) {
            passengerPanels = new ArrayList<>();
        } else {
            // Clear existing passenger panels
            passengerPanels.clear();
        }

        // Remove existing components
        getContentPane().removeAll();
        revalidate();
        repaint();

        // Add passenger details panels
        for (int i = 0; i < passengerCapacity; i++) {
            PassengerDetailsPanel passengerPanel = new PassengerDetailsPanel(i + 1);
            passengerPanels.add(passengerPanel);
            add(passengerPanel, BorderLayout.CENTER);

            // Add submit button adjacent to compute bill button
            add(passengerPanel.createSubmitButton(), BorderLayout.EAST);
        }

        // Add a button to compute the total bill
        JButton computeBillButton = new JButton("Compute Total Bill");
        computeBillButton.setFont(new Font("Montserrat", Font.BOLD, 16));
        computeBillButton.setPreferredSize(new Dimension(180, 40));
        computeBillButton.addActionListener(e -> computeTotalBill());
        add(computeBillButton, BorderLayout.SOUTH);


        pack();
    }

    private void computeTotalBill() {
        double totalBill = 0;

        for (PassengerDetailsPanel passengerPanel : passengerPanels) {
            totalBill += passengerPanel.computePassengerBill();
        }

        String packageName = (String) packageDropdown.getSelectedItem();
        double travelPackageCost = getTravelPackageCost(packageName);

        totalBill += travelPackageCost;

        JOptionPane.showMessageDialog(this, "Total Bill: RS." + totalBill);

        // Additional code for saving the total bill or other details if needed
    }

    private double getTravelPackageCost(String packageName) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql:///travel", "root", "Ayush@lnmiit")) {
            String query = "SELECT cost FROM travel_packages WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, packageName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDouble("cost");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        new BookPackage();
    }

    private static class PassengerDetailsPanel extends JPanel {

        private JTextField nameField;
        private JTextField numberField;
        private JComboBox<String> typeDropdown;
        private JComboBox<String> activityDropdown;
        private JButton submitButton; // Moved the submit button to be an instance variable

        public PassengerDetailsPanel(int index) {
            setLayout(new GridLayout(10, 2, 10, 10));

            Font labelFont = new Font("Montserrat", Font.BOLD, 14);
            Font fieldFont = new Font("Montserrat", Font.PLAIN, 16);

            JLabel nameLabel = new JLabel("Passenger " + index + " Name:");
            nameLabel.setFont(labelFont);
            add(nameLabel);

            nameField = new JTextField();
            nameField.setFont(fieldFont);
            add(nameField);

            JLabel numberLabel = new JLabel("Passenger " + index + " Number:");
            numberLabel.setFont(labelFont);
            add(numberLabel);

            numberField = new JTextField();
            numberField.setFont(fieldFont);
            add(numberField);

            JLabel typeLabel = new JLabel("Passenger " + index + " Type:");
            typeLabel.setFont(labelFont);
            add(typeLabel);

            // Populate the dropdown with passenger types
            typeDropdown = new JComboBox<>(new String[]{"standard", "gold", "premium"});
            typeDropdown.setFont(new Font("Montserrat", Font.PLAIN, 14));
            add(typeDropdown);

            JLabel activityLabel = new JLabel("Activity " + index + ":");
            activityLabel.setFont(labelFont);
            add(activityLabel);

            // Populate the dropdown with activity names
            activityDropdown = new JComboBox<>(getActivityNames());
            activityDropdown.setFont(new Font("Montserrat", Font.PLAIN, 14));
            add(activityDropdown);

            // Create the submit button
            submitButton = new JButton("Submit");
            submitButton.setFont(new Font("Montserrat", Font.BOLD, 16));
            submitButton.setPreferredSize(new Dimension(180, 40));
            submitButton.addActionListener(e -> submitPassengerDetails());
        }

        public double computePassengerBill() {
            String passengerType = (String) typeDropdown.getSelectedItem();
            double activityCost = getActivityCost((String) activityDropdown.getSelectedItem());

            switch (passengerType) {
                case "standard":
                    return activityCost;
                case "gold":
                    return activityCost * 0.9; // 10% discount
                case "premium":
                    return 0; // Free for premium passengers
                default:
                    return 0;
            }
        }

        private double getActivityCost(String activityName) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql:///travel", "root", "Ayush@lnmiit")) {
                String query = "SELECT activity_cost FROM locations WHERE activity_name = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, activityName);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            return resultSet.getDouble("activity_cost");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

        private String[] getActivityNames() {
            List<String> activityNames = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection("jdbc:mysql:///travel", "root", "Ayush@lnmiit")) {
                String query = "SELECT DISTINCT activity_name FROM locations";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            activityNames.add(resultSet.getString("activity_name"));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return activityNames.toArray(new String[0]);
        }

        public String getName() {
            return nameField.getText();
        }

        public String getNumber() {
            return numberField.getText();
        }

        public String getPassengerType() {
            return (String) typeDropdown.getSelectedItem();
        }

        public String getActivity() {
            return (String) activityDropdown.getSelectedItem();
        }

        public JButton createSubmitButton() {
            return submitButton;
        }

        private void submitPassengerDetails() {
            // Add code to save passenger details to the database
            String passengerName = getName();
            String passengerNumber = getNumber();
            String passengerType = getPassengerType();
            String activity = getActivity();

            // Get the location ID for the selected activity
            int locationId = getLocationId(activity);

            // Insert passenger details into the database and get the generated passenger ID
            int passengerId = insertPassenger(passengerName, passengerNumber, passengerType, 0);

            // Insert passenger activity into the database
            insertPassengerActivity(locationId, passengerId);

            // Update activity capacity in the locations table
            updateActivityCapacity(locationId);

            JOptionPane.showMessageDialog(this, "Entry saved successfully.");
        }


        private int getLocationId(String activityName) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql:///travel", "root", "Ayush@lnmiit")) {
                String query = "SELECT location_id FROM locations WHERE activity_name = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, activityName);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            return resultSet.getInt("location_id");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }


        private int insertPassenger(String name, String number, String type, double balance) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql:///travel", "root", "Ayush@lnmiit")) {
                String query = "INSERT INTO passengers (name, passenger_number, passenger_type, balance) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, number);
                    preparedStatement.setString(3, type);
                    preparedStatement.setDouble(4, balance);

                    int affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows > 0) {
                        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                return generatedKeys.getInt(1);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }


        private void insertPassengerActivity(int locationId, int passengerId) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql:///travel", "root", "Ayush@lnmiit")) {
                String query = "INSERT INTO passenger_activities (location_id, passenger_id) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, locationId);
                    preparedStatement.setInt(2, passengerId);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void updateActivityCapacity(int locationId) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql:///travel", "root", "Ayush@lnmiit")) {
                String query = "UPDATE locations SET activity_cap = activity_cap - 1 WHERE location_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, locationId);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
