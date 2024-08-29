package com.trainsetup;

import com.database.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/TrainSetupServlet")
public class TrainSetupServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            int trainNo = Integer.parseInt(request.getParameter("trainNo"));
            int trainCapacity = Integer.parseInt(request.getParameter("trainCapacity"));
            String stations = request.getParameter("stations");
            insertData(trainNo, trainCapacity, stations, response);
        } else if ("update".equals(action)) {
            int trainNo = Integer.parseInt(request.getParameter("trainNo"));
            int newTrainCapacity = Integer.parseInt(request.getParameter("newTrainCapacity"));
            String newStations = request.getParameter("newStations");
            updateData(trainNo, newTrainCapacity, newStations, response);
        } else if ("delete".equals(action)) {
            int trainNo = Integer.parseInt(request.getParameter("trainNo"));
            deleteData(trainNo, response);
        } else if ("populate".equals(action)) {
            populateTrainStations(response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        readData(response);
    }

    private void insertData(int trainNo, int trainCapacity, String stations, HttpServletResponse response) throws IOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            String sql = "INSERT INTO TrainSetup (train_no, train_capacity, stations_list) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, trainNo);
            preparedStatement.setInt(2, trainCapacity);
            preparedStatement.setString(3, stations);

            int rowsAffected = preparedStatement.executeUpdate();

            response.getWriter().println(rowsAffected > 0 ? "Data inserted successfully." : "Data insertion failed.");

            String[] stationsList = stations.split(",");
            for (String station : stationsList) {
                String sql2 = "INSERT INTO Stations (train_no, station_name) VALUES (?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql2);
                stmt.setInt(1, trainNo);
                stmt.setString(2, station.trim());
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            response.getWriter().println("SQL Exception: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement);
        }
    }

    private void readData(HttpServletResponse response) throws IOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM TrainSetup";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            StringBuilder output = new StringBuilder();
            while (resultSet.next()) {
                int trainNo = resultSet.getInt("train_no");
                int trainCapacity = resultSet.getInt("train_capacity");
                String stationsList = resultSet.getString("stations_list");
                output.append("Train No: ").append(trainNo).append(", Train Capacity: ").append(trainCapacity)
                        .append(", Stations: ").append(stationsList).append("\n");
            }

            response.getWriter().println(output.toString());

        } catch (SQLException e) {
            response.getWriter().println("SQL Exception: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, resultSet);
        }
    }

    private void updateData(int trainNo, int newTrainCapacity, String newStations, HttpServletResponse response) throws IOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            String sql = "UPDATE TrainSetup SET train_capacity = ?, stations_list = ? WHERE train_no = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, newTrainCapacity);
            preparedStatement.setString(2, newStations);
            preparedStatement.setInt(3, trainNo);

            int rowsAffected = preparedStatement.executeUpdate();

            response.getWriter().println(rowsAffected > 0 ? "Data updated successfully." : "Data update failed.");

        } catch (SQLException e) {
            response.getWriter().println("SQL Exception: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement);
        }
    }

    private void deleteData(int trainNo, HttpServletResponse response) throws IOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            String sql = "DELETE FROM TrainSetup WHERE train_no = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, trainNo);

            int rowsAffected = preparedStatement.executeUpdate();

            response.getWriter().println(rowsAffected > 0 ? "Data deleted successfully." : "Data deletion failed.");

        } catch (SQLException e) {
            response.getWriter().println("SQL Exception: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement);
        }
    }

    private void populateTrainStations(HttpServletResponse response) throws IOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            String sql = "INSERT INTO TrainStations (train_no, trip_no, station_name, available_seats) " +
                    "SELECT ts.train_no, ts2.trip_no, s.station_name, ts.train_capacity " +
                    "FROM TrainSetup ts " +
                    "JOIN TrainSchedule ts2 ON ts.train_no = ts2.train_no " +
                    "JOIN Stations s ON s.train_no = ts.train_no " +
                    "WHERE NOT EXISTS (" +
                    "    SELECT 1 " +
                    "    FROM TrainStations ts_existing " +
                    "    WHERE ts_existing.train_no = ts.train_no " +
                    "      AND ts_existing.trip_no = ts2.trip_no " +
                    "      AND ts_existing.station_name = s.station_name" +
                    ")";

            preparedStatement = connection.prepareStatement(sql);

            int rowsAffected = preparedStatement.executeUpdate();

            response.getWriter().println(rowsAffected > 0 ? "TrainStations table populated successfully." : "No new data was inserted into TrainStations.");

        } catch (SQLException e) {
            response.getWriter().println("SQL Exception: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement);
        }
    }
}
