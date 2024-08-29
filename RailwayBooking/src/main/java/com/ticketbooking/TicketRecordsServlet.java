package com.ticketbooking;

import com.database.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/TicketBookingServlet")
public class TicketRecordsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            switch (action) {
                case "add":
                    addTicket(request, response);
                    break;
                case "view":
                    viewTicket(request, response);
                    break;
                case "update":
                    updateTicket(request, response);
                    break;
                case "delete":
                    deleteTicket(request, response);
                    break;
                default:
                    response.sendRedirect("index.jsp");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void addTicket(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        String passengerName = request.getParameter("passengerName");
        int trainNo = Integer.parseInt(request.getParameter("trainNo"));
        String onboardStation = request.getParameter("onboardStation");
        String destinationStation = request.getParameter("destinationStation");
        int passengerCount = Integer.parseInt(request.getParameter("passengerCount"));
        Date onboardDate = Date.valueOf(request.getParameter("onboardDate"));
        int tripNo = Integer.parseInt(request.getParameter("tripNo"));

        // Automatically set booking date and ticket status
        Date bookingDate = new Date(System.currentTimeMillis());
        String ticketStatus = "booked";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check availability for the entire journey
            boolean available = checkJourneyAvailability(conn, trainNo, tripNo, onboardStation, destinationStation, passengerCount, onboardDate);
            if (!available) {
                request.setAttribute("errorMessage", "Not enough available seats for the entire journey.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            String sql = "INSERT INTO TicketRecords (passenger_name, train_no, onboard_station_name, destination_station_name, passenger_count, onboard_date, trip_no, booking_date, ticket_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, passengerName);
                pstmt.setInt(2, trainNo);
                pstmt.setString(3, onboardStation);
                pstmt.setString(4, destinationStation);
                pstmt.setInt(5, passengerCount);
                pstmt.setDate(6, onboardDate);
                pstmt.setInt(7, tripNo);
                pstmt.setDate(8, bookingDate);
                pstmt.setString(9, ticketStatus);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            long ticketNo = rs.getLong(1);
                            request.setAttribute("successMessage", "Ticket booked successfully. Ticket No: " + ticketNo);
                            updateAvailableSeats(conn, trainNo, tripNo, onboardStation, destinationStation, passengerCount, onboardDate); // Update available seats
                        }
                    }
                } else {
                    request.setAttribute("errorMessage", "Booking failed.");
                }
                request.getRequestDispatcher("result.jsp").forward(request, response);
            }
        }
    }

    private void viewTicket(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        long ticketNo = Long.parseLong(request.getParameter("ticketNo"));

        String sql = "SELECT * FROM TicketRecords WHERE ticket_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, ticketNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("ticketNo", rs.getLong("ticket_no"));
                    request.setAttribute("passengerName", rs.getString("passenger_name"));
                    request.setAttribute("trainNo", rs.getInt("train_no"));
                    request.setAttribute("onboardStation", rs.getString("onboard_station_name"));
                    request.setAttribute("destinationStation", rs.getString("destination_station_name"));
                    request.setAttribute("passengerCount", rs.getInt("passenger_count"));
                    request.setAttribute("onboardDate", rs.getDate("onboard_date"));
                    request.setAttribute("tripNo", rs.getInt("trip_no"));
                    request.setAttribute("bookingDate", rs.getDate("booking_date"));
                    request.setAttribute("ticketStatus", rs.getString("ticket_status"));
                    request.getRequestDispatcher("viewTicket.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Ticket not found.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
            }
        }
    }

    private void updateTicket(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        long ticketNo = Long.parseLong(request.getParameter("ticketNo"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            TicketDetails currentTicket = getCurrentTicketDetails(conn, ticketNo);
            if (currentTicket == null) {
                request.setAttribute("errorMessage", "Ticket not found.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Check if the onboard date is the current date
            Date today = new Date(System.currentTimeMillis());
            if (currentTicket.onboardDate.equals(today)) {
                request.setAttribute("errorMessage", "Cannot update ticket with today's onboarding date.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            int newPassengerCount = Integer.parseInt(request.getParameter("passengerCount"));

            // Check if the new passenger count exceeds train capacity
            if (newPassengerCount > currentTicket.passengerCount) {
                boolean isAvailable = checkJourneyAvailability(conn, currentTicket.trainNo, currentTicket.tripNo,
                        currentTicket.onboardStation, currentTicket.destinationStation, newPassengerCount - currentTicket.passengerCount, currentTicket.onboardDate);

                if (!isAvailable) {
                    request.setAttribute("errorMessage", "Not enough available seats for the new passenger count.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }

                // Book the new seats
                updateAvailableSeats(conn, currentTicket.trainNo, currentTicket.tripNo, currentTicket.onboardStation,
                        currentTicket.destinationStation, newPassengerCount - currentTicket.passengerCount, currentTicket.onboardDate);
            } else if (newPassengerCount < currentTicket.passengerCount) {
                // Release the old seats
                updateAvailableSeats(conn, currentTicket.trainNo, currentTicket.tripNo, currentTicket.onboardStation,
                        currentTicket.destinationStation, -(currentTicket.passengerCount - newPassengerCount), currentTicket.onboardDate);
            }

            // Update the passenger count and status
            String sql = "UPDATE TicketRecords SET passenger_count = ?, ticket_status = 'updated' WHERE ticket_no = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, newPassengerCount);
                pstmt.setLong(2, ticketNo);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    request.setAttribute("successMessage", "Ticket updated successfully.");
                    request.getRequestDispatcher("result.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Ticket not found.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
            }
        }
    }


    private void deleteTicket(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        long ticketNo = Long.parseLong(request.getParameter("ticketNo"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Pass the Connection object to getCurrentTicketDetails
            TicketDetails currentTicket = getCurrentTicketDetails(conn, ticketNo);
            if (currentTicket == null) {
                request.setAttribute("errorMessage", "Ticket not found.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Check if the onboarding date is the current date
            Date today = new Date(System.currentTimeMillis());
            if (currentTicket.onboardDate.equals(today)) {
                request.setAttribute("errorMessage", "Cannot cancel ticket with today's onboarding date.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Proceed to update the ticket status to "canceled"
            String updateStatusSql = "UPDATE TicketRecords SET ticket_status = 'canceled' WHERE ticket_no = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateStatusSql)) {
                pstmt.setLong(1, ticketNo);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    // Release the seats for the canceled ticket
                    updateAvailableSeats(conn, currentTicket.trainNo, currentTicket.tripNo, currentTicket.onboardStation,
                            currentTicket.destinationStation, -currentTicket.passengerCount, currentTicket.onboardDate);

                    request.setAttribute("successMessage", "Ticket canceled successfully.");
                    request.getRequestDispatcher("result.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Ticket not found.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
            }
        }
    }


    private TicketDetails getCurrentTicketDetails(Connection conn, long ticketNo) throws SQLException {
        String sql = "SELECT * FROM TicketRecords WHERE ticket_no = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, ticketNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new TicketDetails(
                            rs.getLong("ticket_no"),
                            rs.getString("passenger_name"),
                            rs.getInt("train_no"),
                            rs.getString("onboard_station_name"),
                            rs.getString("destination_station_name"),
                            rs.getInt("passenger_count"),
                            rs.getDate("onboard_date"),
                            rs.getInt("trip_no"),
                            rs.getDate("booking_date"),
                            rs.getString("ticket_status")
                    );
                }
            }
        }
        return null;
    }

    private boolean checkJourneyAvailability(Connection conn, int trainNo, int tripNo, String onboardStation, String destinationStation, int passengerCount, Date onboardDate) throws SQLException {
        String sql = "SELECT available_seats FROM TrainStations WHERE train_no = ? AND trip_no = ? AND station_name = ? AND date = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainNo);
            pstmt.setInt(2, tripNo);
            pstmt.setString(3, onboardStation);
            pstmt.setDate(4, onboardDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int availableSeats = rs.getInt("available_seats");
                    return passengerCount <= availableSeats;
                }
            }
        }
        return false;
    }

    private void updateAvailableSeats(Connection conn, int trainNo, int tripNo, String onboardStation, String destinationStation, int passengerCount, Date onboardDate) throws SQLException {
        String sql = "UPDATE TrainStations SET available_seats = available_seats - ? WHERE train_no = ? AND trip_no = ? AND station_name = ? AND date = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, passengerCount);
            pstmt.setInt(2, trainNo);
            pstmt.setInt(3, tripNo);
            pstmt.setString(4, onboardStation);
            pstmt.setDate(5, onboardDate);
            pstmt.executeUpdate();
        }
    }

    private static class TicketDetails {
        long ticketNo;
        String passengerName;
        int trainNo;
        String onboardStation;
        String destinationStation;
        int passengerCount;
        Date onboardDate;
        int tripNo;
        Date bookingDate;
        String ticketStatus;

        TicketDetails(long ticketNo, String passengerName, int trainNo, String onboardStation, String destinationStation, int passengerCount, Date onboardDate, int tripNo, Date bookingDate, String ticketStatus) {
            this.ticketNo = ticketNo;
            this.passengerName = passengerName;
            this.trainNo = trainNo;
            this.onboardStation = onboardStation;
            this.destinationStation = destinationStation;
            this.passengerCount = passengerCount;
            this.onboardDate = onboardDate;
            this.tripNo = tripNo;
            this.bookingDate = bookingDate;
            this.ticketStatus = ticketStatus;
        }
    }
}
