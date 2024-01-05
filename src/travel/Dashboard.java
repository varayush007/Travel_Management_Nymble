package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    Dashboard() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);

        // Main Panel
        JPanel p1 = new JPanel();
        p1.setLayout(null);
        p1.setBackground(Color.DARK_GRAY);
        p1.setBounds(0, 0, 1600, 65);
        add(p1);

        // Change the image of the dashboard.png
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/Dashboard.png"));
        Image i2 = i1.getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel icon = new JLabel(i3);
        icon.setBounds(5, 0, 70, 70);
        p1.add(icon);

        // Dashboard label
        JLabel heading = new JLabel("DASHBOARD");
        heading.setBounds(icon.getX() + icon.getWidth() + 5, 10, 300, 40);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Montserrat", Font.BOLD, 25));
        p1.add(heading);

        // Creating a dropdown button for Travel Packages
        JComboBox<String> travelDropdown = new JComboBox<>(new String[]{"Add Travel Package", "Book Travel Package", "View Travel Packages", "Delete Travel Package"});
        travelDropdown.setBounds(heading.getX() + heading.getWidth() + 5, heading.getY(), 230, 42);
        travelDropdown.setBackground(Color.lightGray);
        travelDropdown.setForeground(Color.black);
        travelDropdown.setFont(new Font("Montserrat", Font.PLAIN, 18));
        p1.add(travelDropdown);

        // Add action listener to the dropdown
        travelDropdown.addActionListener(e -> {
            String selectedOption = (String) travelDropdown.getSelectedItem();
            if ("Add Travel Package".equals(selectedOption)) {
                openAddPackageWindow();
            }else if ("Book Travel Package".equals(selectedOption)) {
                openBookPackageWindow();}
            else if ("View Travel Packages".equals(selectedOption)) {
                new ViewPackage();
            } else if ("Delete Travel Package".equals(selectedOption)) {
                openDeletePackageWindow();
            }
        });

        // Destinations button
        JComboBox<String> destDropdown = new JComboBox<>(new String[]{"Add Destination/Activities", "View Destinations/Activities"});
        destDropdown.setBounds(travelDropdown.getX() + travelDropdown.getWidth() + 5, heading.getY(), 280, 42);
        destDropdown.setBackground(Color.lightGray);
        destDropdown.setForeground(Color.black);
        destDropdown.setFont(new Font("Montserrat", Font.PLAIN, 18));
        p1.add(destDropdown);

        destDropdown.addActionListener(e -> {
            String selectedOption = (String) destDropdown.getSelectedItem();
            if ("Add Destination/Activities".equals(selectedOption)) {
                openAddDestinationWindow();
            } else if ("View Destinations/Activities".equals(selectedOption)) {
                openViewDestinationWindow();
            }
        });

        // Passengers button
        JButton passengerListButton = new JButton("View All Passengers");
        passengerListButton.setBounds(destDropdown.getX() + destDropdown.getWidth() + 5, heading.getY(), 200, 42);
        passengerListButton.setBackground(Color.lightGray);
        passengerListButton.setForeground(Color.black);
        passengerListButton.setFont(new Font("Montserrat", Font.PLAIN, 18));
        passengerListButton.setMargin(new Insets(0, 0, 0, 0));
        p1.add(passengerListButton);

        passengerListButton.addActionListener(e -> openPassengerListDetails());


        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/home.jpg"));
        Image i5 = i4.getImage().getScaledInstance(1650, 900, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel image = new JLabel(i6);
        image.setBounds(0, 0, 1500, 900);
        add(image);

        setVisible(true);
    }

    private void openAddPackageWindow() {
        AddPackage addPackagePage = new AddPackage();
        addPackagePage.setVisible(true);
    }

    private void openDeletePackageWindow() {
        DeleteTravelPackage deletePackagePage = new DeleteTravelPackage();
        deletePackagePage.setVisible(true);
    }

    private void openBookPackageWindow() {
        BookPackage bookPackagePage = new BookPackage();
        bookPackagePage.setVisible(true);
    }

    private void openViewDestinationWindow() {
        Viewdestination viewDestinationPage = new Viewdestination();
        viewDestinationPage.setVisible(true);
    }

    private void openAddDestinationWindow(){
        AddDestination addDestinationPage = new AddDestination();
        addDestinationPage.setVisible(true);
    }
    // Method to open the PassengerListDetails page
    private void openPassengerListDetails() {
        PassengerListDetails passengerListDetailsPage = new PassengerListDetails();
        passengerListDetailsPage.setVisible(true);
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}
