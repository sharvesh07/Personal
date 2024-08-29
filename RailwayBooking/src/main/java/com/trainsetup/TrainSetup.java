package com.trainsetup;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;

import com.database.DatabaseConnection;

public class TrainSetup {

    // Create operation: Insert data into TrainSetup and Stations tables
    public void insertData(int trainNo, int trainCapacity, String stations) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            // SQL INSERT query for TrainSetup table
            String sql = "INSERT INTO TrainSetup (train_no, train_capacity, stations_list) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, trainNo);
            preparedStatement.setInt(2, trainCapacity);
            preparedStatement.setString(3, stations);

            // Execute the insert operation
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully.");
            } else {
                System.out.println("Data insertion failed.");
            }

            // Insert stations into Stations table
            String[] stationsList = stations.split(",");
            for (String station : stationsList) {
                String sql2 = "INSERT INTO Stations (train_no, station_name) VALUES (?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql2);
                stmt.setInt(1, trainNo);
                stmt.setString(2, station.trim());
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement);
        }
    }

    // Read operation: Fetch and display all records from TrainSetup table
    public void readData() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();

            // SQL SELECT query
            String sql = "SELECT * FROM TrainSetup";
            resultSet = statement.executeQuery(sql);

            // Display the results
            while (resultSet.next()) {
                int trainNo = resultSet.getInt("train_no");
                int trainCapacity = resultSet.getInt("train_capacity");
                String stationsList = resultSet.getString("stations_list");
                System.out.println("Train No: " + trainNo + ", Train Capacity: " + trainCapacity + ", Stations: " + stationsList);
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(connection, statement, resultSet);
        }
    }

    // Update operation: Update records in TrainSetup table
    public void updateData(int trainNo, int newTrainCapacity, String newStations) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            // SQL UPDATE query
            String sql = "UPDATE TrainSetup SET train_capacity = ?, stations_list = ? WHERE train_no = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, newTrainCapacity);
            preparedStatement.setString(2, newStations);
            preparedStatement.setInt(3, trainNo);

            // Execute the update operation
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data updated successfully.");
            } else {
                System.out.println("Data update failed.");
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement);
        }
    }

    // Delete operation: Delete records from TrainSetup table
    public void deleteData(int trainNo) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            // SQL DELETE query
            String sql = "DELETE FROM TrainSetup WHERE train_no = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, trainNo);

            // Execute the delete operation
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data deleted successfully.");
            } else {
                System.out.println("Data deletion failed.");
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement);
        }
    }

    // Populate TrainStations table with data from TrainSetup and TrainSchedule
    public void populateTrainStations() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            // SQL query to fetch required data from TrainSetup and TrainSchedule
            String sql = "INSERT INTO TrainStations (train_no, trip_no, station_name, available_seats) " +
                    "SELECT ts.train_no, ts2.trip_no, s.station_name, ts.train_capacity " +
                    "FROM TrainSetup ts " +
                    "JOIN TrainSchedule ts2 ON ts.train_no = ts2.train_no " +
                    "JOIN Stations s ON s.train_no = ts.train_no " +
                    "WHERE ts.train_no IS NOT NULL";

            preparedStatement = connection.prepareStatement(sql);

            // Execute the insert operation
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("TrainStations table populated successfully.");
            } else {
                System.out.println("No data was inserted into TrainStations.");
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement);
        }
    }

    // Main method for user interaction
    public static void main(String[] args) {
        TrainSetup trainSetup = new TrainSetup();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Create (Insert Data)");
            System.out.println("2. Read (View Data)");
            System.out.println("3. Update (Modify Data)");
            System.out.println("4. Delete (Remove Data)");
            System.out.println("5. Populate Train Stations");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over

            switch (choice) {
                case 1:
                    // Create
                    System.out.print("Enter Train No: ");
                    int trainNo = scanner.nextInt();
                    System.out.print("Enter Train Capacity: ");
                    int trainCapacity = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    System.out.print("Enter Stations (comma-separated): ");
                    String stations = scanner.nextLine();

                    trainSetup.insertData(trainNo, trainCapacity, stations);
                    break;

                case 2:
                    // Read
                    trainSetup.readData();
                    break;

                case 3:
                    // Update
                    System.out.print("Enter Train No to Update: ");
                    int updateTrainNo = scanner.nextInt();
                    System.out.print("Enter New Train Capacity: ");
                    int newTrainCapacity = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    System.out.print("Enter New Stations (comma-separated): ");
                    String newStations = scanner.nextLine();

                    trainSetup.updateData(updateTrainNo, newTrainCapacity, newStations);
                    break;

                case 4:
                    // Delete
                    System.out.print("Enter Train No to Delete: ");
                    int deleteTrainNo = scanner.nextInt();
                    trainSetup.deleteData(deleteTrainNo);
                    break;

                case 5:
                    // Populate Train Stations
                    trainSetup.populateTrainStations();
                    break;

                case 6:
                    // Exit
                    System.out.println("Exiting the program...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
