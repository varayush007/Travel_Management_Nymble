package travel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Viewdestination extends JFrame {

    private int currentIndex;

    public Viewdestination() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Destinations");

        Font montserratFontBold20 = new Font("Montserrat", Font.BOLD, 20);
        Font montserratFontBold16 = new Font("Montserrat", Font.BOLD, 16);

        JLabel headingLabel = new JLabel("Destinations", SwingConstants.CENTER);
        headingLabel.setFont(montserratFontBold20);

        JTextArea destinationDetailsArea = new JTextArea();
        destinationDetailsArea.setFont(montserratFontBold16);
        destinationDetailsArea.setEditable(false);
        destinationDetailsArea.setLineWrap(true);
        destinationDetailsArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(destinationDetailsArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton nextButton = new JButton("Next");
        JButton backButton = new JButton("Back to Dashboard");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(headingLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(centerPanel);

        nextButton.addActionListener(e -> {
            currentIndex = (currentIndex + 1) % getDestinationCount();
            showDestinationDetails(destinationDetailsArea);
        });

        backButton.addActionListener(e -> {
            dispose();
            new Dashboard();
        });

        setLocationRelativeTo(null);
        setVisible(true);

        showDestinationDetails(destinationDetailsArea);
    }

    private void showDestinationDetails(JTextArea destinationDetailsArea) {
        String destinationDetails = getDestinationDetails(currentIndex);
        destinationDetailsArea.setText(destinationDetails);
    }

    private int getDestinationCount() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql:///travel", "root", "Ayush@lnmiit")) {
            String query = "SELECT COUNT(*) FROM locations";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getDestinationDetails(int index) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql:///travel", "root", "Ayush@lnmiit")) {
            String query = "SELECT l.*, tp.name AS travel_package_name FROM locations l " +
                    "JOIN travel_packages tp ON l.travel_package_id = tp.id LIMIT ?, 1";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, index);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return buildDestinationDetails(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "No destination details available";
    }

    private String buildDestinationDetails(ResultSet resultSet) throws SQLException {
        return "Destination Name: " + resultSet.getString("destination_name") + "\n\n" +
                "Activity Name: " + resultSet.getString("activity_name") + "\n\n" +
                "Activity Cost: RS." + resultSet.getDouble("activity_cost") + "\n\n" +
                "Activity Capacity: " + resultSet.getInt("activity_cap") + "\n\n" +
                "Travel Package: " + resultSet.getString("travel_package_name");
    }

    public static void main(String[] args) {
        new Viewdestination();
    }
}
