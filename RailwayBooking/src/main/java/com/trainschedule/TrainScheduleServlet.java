package com.trainschedule;

import com.database.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/TrainScheduleServlet")
public class TrainScheduleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            switch (action) {
                case "create":
                    insertData(request, out);
                    break;
                case "update":
                    updateData(request, out);
                    break;
                case "delete":
                    deleteData(request, out);
                    break;
                case "populate":
                    populateTrainStations(response);
                    break;
                case "updateStations":
                    // Assuming TrainDataUpdater is a class with methods for handling train stations updates
                    TrainDataUpdater.updateTrainStationsForFutureDates();
                    break;
                case "loadTripLog":
                    // Assuming TrainDataUpdater is a class with methods for handling trip log updates
                    TrainDataUpdater.loadTrainTripLog();
                    break;
                default:
                    out.println("Unknown action");
                    break;
            }
        } catch (SQLException e) {
            out.println("SQL Exception: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try {
            readData(out);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void insertData(HttpServletRequest request, PrintWriter out) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        int trainNo = Integer.parseInt(request.getParameter("trainNo"));
        int tripNo = Integer.parseInt(request.getParameter("tripNo"));
        String tripStartTime = request.getParameter("tripStartTime");
        String tripEndTime = request.getParameter("tripEndTime");
        String tripDate = request.getParameter("tripDate");

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO TrainSchedule (train_no, trip_no, trip_start_time, trip_end_time, trip_date) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, trainNo);
            preparedStatement.setInt(2, tripNo);
            preparedStatement.setString(3, tripStartTime);
            preparedStatement.setString(4, tripEndTime);
            preparedStatement.setString(5, tripDate);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                out.println("Data inserted successfully.");
            } else {
                out.println("Data insertion failed.");
            }
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement);
        }
    }

    private void updateData(HttpServletRequest request, PrintWriter out) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        int trainNo = Integer.parseInt(request.getParameter("trainNo"));
        int tripNo = Integer.parseInt(request.getParameter("tripNo"));
        String newStartTime = request.getParameter("newStartTime");
        String newEndTime = request.getParameter("newEndTime");
        String newTripDate = request.getParameter("newTripDate");

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE TrainSchedule SET trip_start_time = ?, trip_end_time = ?, trip_date = ? WHERE train_no = ? AND trip_no = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newStartTime);
            preparedStatement.setString(2, newEndTime);
            preparedStatement.setString(3, newTripDate);
            preparedStatement.setInt(4, trainNo);
            preparedStatement.setInt(5, tripNo);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                out.println("Data updated successfully.");
            } else {
                out.println("Data update failed.");
            }
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement);
        }
    }

    private void deleteData(HttpServletRequest request, PrintWriter out) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        int trainNo = Integer.parseInt(request.getParameter("trainNo"));
        int tripNo = Integer.parseInt(request.getParameter("tripNo"));

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM TrainSchedule WHERE train_no = ? AND trip_no = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, trainNo);
            preparedStatement.setInt(2, tripNo);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                out.println("Data deleted successfully.");
            } else {
                out.println("Data deletion failed.");
            }
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement);
        }
    }

    private void readData(PrintWriter out) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            statement = connection.createStatement();
            String sql = "SELECT * FROM TrainSchedule";
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int trainNo = resultSet.getInt("train_no");
                int tripNo = resultSet.getInt("trip_no");
                String tripStartTime = resultSet.getString("trip_start_time");
                String tripEndTime = resultSet.getString("trip_end_time");
                String tripDate = resultSet.getString("trip_date");

                out.println("Train No: " + trainNo + ", Trip No: " + tripNo +
                        ", Start Time: " + tripStartTime + ", End Time: " + tripEndTime +
                        ", Date: " + tripDate + "<br>");
            }
        } finally {
            DatabaseConnection.closeResources(connection, statement, resultSet);
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
