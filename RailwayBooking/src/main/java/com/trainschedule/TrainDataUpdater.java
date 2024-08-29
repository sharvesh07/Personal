package com.trainschedule;

import com.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TrainDataUpdater {

    public static void updateTrainStationsForFutureDates() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement insertStatement = null;

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();

            // Fetch data from Stations and TrainSchedule for future dates
            String query = "SELECT s.train_no, s.station_name, ts.trip_no, ts.trip_date " +
                    "FROM Stations s " +
                    "JOIN TrainSchedule ts ON s.train_no = ts.train_no " +
                    "WHERE ts.trip_date > CURDATE()";
            resultSet = statement.executeQuery(query);

            // Prepare insert/update statement for TrainStations
            String insertQuery = "INSERT INTO TrainStations (train_no, trip_no, station_name, station_index, available_seats, trip_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE available_seats = VALUES(available_seats)";

            insertStatement = connection.prepareStatement(insertQuery);

            while (resultSet.next()) {
                int trainNo = resultSet.getInt("train_no");
                String stationName = resultSet.getString("station_name");
                int tripNo = resultSet.getInt("trip_no");
                LocalDate tripDate = resultSet.getDate("trip_date").toLocalDate();

                // Determine stationIndex based on trainNo and stationName
                int stationIndex = getStationIndex(trainNo, stationName);
                int availableSeats = getAvailableSeats(trainNo);

                // Set parameters for the insert/update statement
                insertStatement.setInt(1, trainNo);
                insertStatement.setInt(2, tripNo);
                insertStatement.setString(3, stationName);
                insertStatement.setInt(4, stationIndex);
                insertStatement.setInt(5, availableSeats);
                insertStatement.setDate(6, Date.valueOf(tripDate));

                // Execute the insert/update
                insertStatement.executeUpdate();
            }

            System.out.println("TrainStations table updated successfully for future dates.");

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(connection, statement, resultSet);
            DatabaseConnection.closeResources(connection, insertStatement);
        }
    }

    private static int getStationIndex(int trainNo, String stationName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int stationIndex = -1;

        try {
            connection = DatabaseConnection.getConnection();
            String stationsQuery = "SELECT station_name, station_index FROM TrainStations WHERE train_no = ? ORDER BY station_index";
            preparedStatement = connection.prepareStatement(stationsQuery);
            preparedStatement.setInt(1, trainNo);
            resultSet = preparedStatement.executeQuery();

            Map<String, Integer> stations = new HashMap<>();
            while (resultSet.next()) {
                String name = resultSet.getString("station_name");
                int index = resultSet.getInt("station_index");
                stations.put(name, index);
            }

            stationIndex = stations.getOrDefault(stationName, -1);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, resultSet);
        }

        return stationIndex;
    }

    private static int getAvailableSeats(int trainNo) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int availableSeats = 0;

        try {
            connection = DatabaseConnection.getConnection();
            String capacityQuery = "SELECT train_capacity FROM TrainSetup WHERE train_no = ?";
            preparedStatement = connection.prepareStatement(capacityQuery);
            preparedStatement.setInt(1, trainNo);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int trainCapacity = resultSet.getInt("train_capacity");

                String bookedSeatsQuery = "SELECT COALESCE(SUM(passenger_count), 0) AS booked_seats " +
                        "FROM TicketRecords WHERE train_no = ? AND ticket_status = 'booked'";
                preparedStatement = connection.prepareStatement(bookedSeatsQuery);
                preparedStatement.setInt(1, trainNo);
                resultSet = preparedStatement.executeQuery();

                int bookedSeats = 0;
                if (resultSet.next()) {
                    bookedSeats = resultSet.getInt("booked_seats");
                }

                availableSeats = trainCapacity - bookedSeats;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, resultSet);
        }

        return availableSeats;
    }

    public static void loadTrainTripLog() {
        Connection connection = null;
        Statement selectStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            selectStatement = connection.createStatement();

            String selectTrainScheduleSQL = "SELECT ts.train_no, ts.trip_no, ts.trip_start_time, ts.trip_end_time, ts.trip_date "
                    + "FROM TrainSchedule ts "
                    + "JOIN TrainSetup setup ON ts.train_no = setup.train_no";

            String insertTrainTripLogSQL = "INSERT INTO TrainTripLog (train_no, trip_no, trip_start_time, trip_end_time, log_date) "
                    + "VALUES (?, ?, ?, ?, ?)";

            resultSet = selectStatement.executeQuery(selectTrainScheduleSQL);
            insertStatement = connection.prepareStatement(insertTrainTripLogSQL);

            while (resultSet.next()) {
                int trainNo = resultSet.getInt("train_no");
                int tripNo = resultSet.getInt("trip_no");
                String tripStartTime = resultSet.getString("trip_start_time");
                String tripEndTime = resultSet.getString("trip_end_time");
                String tripDate = resultSet.getString("trip_date");

                insertStatement.setInt(1, trainNo);
                insertStatement.setInt(2, tripNo);
                insertStatement.setString(3, tripStartTime);
                insertStatement.setString(4, tripEndTime);
                insertStatement.setString(5, tripDate);

                try {
                    insertStatement.executeUpdate();
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("Duplicate entry found: " + e.getMessage());
                }
            }

            System.out.println("TrainTripLog table loaded successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, selectStatement, resultSet);
            DatabaseConnection.closeResources(connection, insertStatement);
        }
    }

    public static void main(String[] args) {
        updateTrainStationsForFutureDates();
        loadTrainTripLog();
    }
}
