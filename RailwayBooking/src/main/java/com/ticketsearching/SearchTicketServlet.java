package com.ticketsearching;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.database.DatabaseConnection;
@WebServlet("/SearchTicketServlet")
public class SearchTicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ticketNo = request.getParameter("ticket_no");
        String passengerName = request.getParameter("passenger_name");
        String trainNo = request.getParameter("train_no");
        String onboardStation = request.getParameter("onboard_station");
        String destinationStation = request.getParameter("destination_station");
        String ticketStatus = request.getParameter("ticket_status");
        String onboardDate = request.getParameter("onboard_date");
        String bookingDate = request.getParameter("booking_date");
        String tripNo = request.getParameter("trip_no");

        List<TicketRecord> ticketRecords = new ArrayList<>();

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM TicketRecords WHERE 1=1");

        if (ticketNo != null && !ticketNo.isEmpty()) {
            queryBuilder.append(" AND ticket_no = ?");
        }
        if (passengerName != null && !passengerName.isEmpty()) {
            queryBuilder.append(" AND passenger_name = ?");
        }
        if (trainNo != null && !trainNo.isEmpty()) {
            queryBuilder.append(" AND train_no = ?");
        }
        if (onboardStation != null && !onboardStation.isEmpty()) {
            queryBuilder.append(" AND onboard_station_name = ?");
        }
        if (destinationStation != null && !destinationStation.isEmpty()) {
            queryBuilder.append(" AND destination_station_name = ?");
        }
        if (ticketStatus != null && !ticketStatus.isEmpty()) {
            queryBuilder.append(" AND ticket_status = ?");
        }
        if (onboardDate != null && !onboardDate.isEmpty()) {
            queryBuilder.append(" AND onboard_date = ?");
        }
        if (bookingDate != null && !bookingDate.isEmpty()) {
            queryBuilder.append(" AND booking_date = ?");
        }
        if (tripNo != null && !tripNo.isEmpty()) {
            queryBuilder.append(" AND trip_no = ?");
        }

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter();
             Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {

            int paramIndex = 1;

            // Set the parameters dynamically based on input
            if (ticketNo != null && !ticketNo.isEmpty()) {
                preparedStatement.setInt(paramIndex++, Integer.parseInt(ticketNo));
            }
            if (passengerName != null && !passengerName.isEmpty()) {
                preparedStatement.setString(paramIndex++, passengerName);
            }
            if (trainNo != null && !trainNo.isEmpty()) {
                preparedStatement.setInt(paramIndex++, Integer.parseInt(trainNo));
            }
            if (onboardStation != null && !onboardStation.isEmpty()) {
                preparedStatement.setString(paramIndex++, onboardStation);
            }
            if (destinationStation != null && !destinationStation.isEmpty()) {
                preparedStatement.setString(paramIndex++, destinationStation);
            }
            if (ticketStatus != null && !ticketStatus.isEmpty()) {
                preparedStatement.setString(paramIndex++, ticketStatus);
            }
            if (onboardDate != null && !onboardDate.isEmpty()) {
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(onboardDate));
            }
            if (bookingDate != null && !bookingDate.isEmpty()) {
                preparedStatement.setDate(paramIndex++, java.sql.Date.valueOf(bookingDate));
            }
            if (tripNo != null && !tripNo.isEmpty()) {
                preparedStatement.setInt(paramIndex++, Integer.parseInt(tripNo));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Fetch results into the ticketRecords list
                while (resultSet.next()) {
                    TicketRecord record = new TicketRecord(
                            resultSet.getInt("ticket_no"),
                            resultSet.getString("passenger_name"),
                            resultSet.getInt("train_no"),
                            resultSet.getString("onboard_station_name"),
                            resultSet.getString("destination_station_name"),
                            resultSet.getInt("passenger_count"),
                            resultSet.getDate("onboard_date"),
                            resultSet.getInt("trip_no"),
                            resultSet.getDate("booking_date"),
                            resultSet.getString("ticket_status")
                    );
                    ticketRecords.add(record);
                }

                // Generate HTML response
                out.println("<html><head><title>Search Results</title>");
                out.println("<style>");
                out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
                out.println("h1 { color: #333; }");
                out.println("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
                out.println("table, th, td { border: 1px solid #ddd; }");
                out.println("th, td { padding: 8px; text-align: left; }");
                out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
                out.println("th { background-color: #4CAF50; color: white; }");
                out.println("</style></head><body>");
                out.println("<h1>Search Results</h1>");
                out.println("<table>");
                out.println("<thead>");
                out.println("<tr>");
                out.println("<th>Ticket No</th>");
                out.println("<th>Passenger Name</th>");
                out.println("<th>Train No</th>");
                out.println("<th>Onboard Station</th>");
                out.println("<th>Destination Station</th>");
                out.println("<th>Passenger Count</th>");
                out.println("<th>Onboard Date</th>");
                out.println("<th>Trip No</th>");
                out.println("<th>Booking Date</th>");
                out.println("<th>Ticket Status</th>");
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");

                for (TicketRecord record : ticketRecords) {
                    out.println("<tr>");
                    out.println("<td>" + record.getTicketNo() + "</td>");
                    out.println("<td>" + record.getPassengerName() + "</td>");
                    out.println("<td>" + record.getTrainNo() + "</td>");
                    out.println("<td>" + record.getOnboardStation() + "</td>");
                    out.println("<td>" + record.getDestinationStation() + "</td>");
                    out.println("<td>" + record.getPassengerCount() + "</td>");
                    out.println("<td>" + record.getOnboardDate() + "</td>");
                    out.println("<td>" + record.getTripNo() + "</td>");
                    out.println("<td>" + record.getBookingDate() + "</td>");
                    out.println("<td>" + record.getTicketStatus() + "</td>");
                    out.println("</tr>");
                }

                out.println("</tbody>");
                out.println("</table>");
                out.println("</body></html>");

                // Print formatted output to console
                System.out.println("-----------------------------------------------------------------");
                System.out.println("|                       TICKET RECORDS                         |");
                System.out.println("-----------------------------------------------------------------");
                for (TicketRecord record : ticketRecords) {
                    System.out.println("-----------------------------------------------------------------");
                    System.out.printf("| Ticket No:          %s%n", record.getTicketNo());
                    System.out.printf("| Passenger Name:     %s%n", record.getPassengerName());
                    System.out.printf("| Train No:           %s%n", record.getTrainNo());
                    System.out.printf("| Onboard Station:    %s%n", record.getOnboardStation());
                    System.out.printf("| Destination Station:%s%n", record.getDestinationStation());
                    System.out.printf("| Passenger Count:    %s%n", record.getPassengerCount());
                    System.out.printf("| Onboard Date:       %s%n", record.getOnboardDate());
                    System.out.printf("| Trip No:            %s%n", record.getTripNo());
                    System.out.printf("| Booking Date:       %s%n", record.getBookingDate());
                    System.out.printf("| Ticket Status:      %s%n", record.getTicketStatus());
                    System.out.println("-----------------------------------------------------------------");
                }
                System.out.println("Total records found: " + ticketRecords.size());
                System.out.println("-----------------------------------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
}
