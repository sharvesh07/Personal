package com.trainschedule;

import com.database.DatabaseConnection;

import java.sql.*;
import java.util.Scanner;

public class TrainScheduleManager {

    // Create operation: Insert data into TrainSchedule table
    public void insertData(int trainNo, int tripNo, String tripStartTime, String tripEndTime, String tripDate) {
        String sql = "INSERT INTO TrainSchedule (train_no, trip_no, trip_start_time, trip_end_time, trip_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, trainNo);
            preparedStatement.setInt(2, tripNo);
            preparedStatement.setString(3, tripStartTime);
            preparedStatement.setString(4, tripEndTime);
            preparedStatement.setString(5, tripDate);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Data inserted successfully." : "Data insertion failed.");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    // Read operation: Fetch and display all records from TrainSchedule table
    public void readData() {
        String sql = "SELECT * FROM TrainSchedule";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int trainNo = resultSet.getInt("train_no");
                int tripNo = resultSet.getInt("trip_no");
                String tripStartTime = resultSet.getString("trip_start_time");
                String tripEndTime = resultSet.getString("trip_end_time");
                String tripDate = resultSet.getString("trip_date");

                System.out.println(String.format("Train No: %d, Trip No: %d, Start Time: %s, End Time: %s, Date: %s",
                        trainNo, tripNo, tripStartTime, tripEndTime, tripDate));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    // Update operation: Update records in TrainSchedule table
    public void updateData(int trainNo, int tripNo, String newStartTime, String newEndTime, String newTripDate) {
        String sql = "UPDATE TrainSchedule SET trip_start_time = ?, trip_end_time = ?, trip_date = ? WHERE train_no = ? AND trip_no = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, newStartTime);
            preparedStatement.setString(2, newEndTime);
            preparedStatement.setString(3, newTripDate);
            preparedStatement.setInt(4, trainNo);
            preparedStatement.setInt(5, tripNo);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Data updated successfully." : "Data update failed.");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    // Delete operation: Delete records from TrainSchedule table
    public void deleteData(int trainNo, int tripNo) {
        String sql = "DELETE FROM TrainSchedule WHERE train_no = ? AND trip_no = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, trainNo);
            preparedStatement.setInt(2, tripNo);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Data deleted successfully." : "Data deletion failed.");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }

    // Main method for user interaction
    public static void main(String[] args) {
        TrainScheduleManager manager = new TrainScheduleManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nTrain Schedule Menu:");
            System.out.println("1. Create (Insert Data)");
            System.out.println("2. Read (Display Data)");
            System.out.println("3. Update (Modify Data)");
            System.out.println("4. Delete (Remove Data)");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over

            switch (choice) {
                case 1:
                    // Create
                    System.out.print("Enter Train No: ");
                    int trainNo = scanner.nextInt();
                    System.out.print("Enter Trip No: ");
                    int tripNo = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    System.out.print("Enter Trip Start Time (HH:MM:SS): ");
                    String tripStartTime = scanner.nextLine();
                    System.out.print("Enter Trip End Time (HH:MM:SS): ");
                    String tripEndTime = scanner.nextLine();
                    System.out.print("Enter Trip Date (YYYY-MM-DD): ");
                    String tripDate = scanner.nextLine();

                    manager.insertData(trainNo, tripNo, tripStartTime, tripEndTime, tripDate);
                    break;

                case 2:
                    // Read
                    manager.readData();
                    break;

                case 3:
                    // Update
                    System.out.print("Enter Train No to update: ");
                    int updateTrainNo = scanner.nextInt();
                    System.out.print("Enter Trip No to update: ");
                    int updateTripNo = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    System.out.print("Enter new Trip Start Time (HH:MM:SS): ");
                    String newStartTime = scanner.nextLine();
                    System.out.print("Enter new Trip End Time (HH:MM:SS): ");
                    String newEndTime = scanner.nextLine();
                    System.out.print("Enter new Trip Date (YYYY-MM-DD): ");
                    String newTripDate = scanner.nextLine();

                    manager.updateData(updateTrainNo, updateTripNo, newStartTime, newEndTime, newTripDate);
                    break;

                case 4:
                    // Delete
                    System.out.print("Enter Train No to delete: ");
                    int deleteTrainNo = scanner.nextInt();
                    System.out.print("Enter Trip No to delete: ");
                    int deleteTripNo = scanner.nextInt();

                    manager.deleteData(deleteTrainNo, deleteTripNo);
                    break;

                case 5:
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
