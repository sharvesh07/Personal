<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.List, com.ticketsearching.TicketRecord" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Filter Ticket Records</title>
    <style>
        /* Import Google Fonts */
        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500&display=swap');

        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .container {
            max-width: 800px;
            width: 100%;
            padding: 30px;
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            margin: 20px;
        }

        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
            font-size: 24px;
        }

        form {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 15px;
            margin-bottom: 20px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }

        label {
            font-weight: 500;
            color: #555;
            margin-bottom: 5px;
        }

        input[type="text"], input[type="date"], select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            box-sizing: border-box;
        }

        input[type="submit"] {
            background-color: #007bff;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s ease;
            grid-column: span 2;
        }

        input[type="submit"]:hover {
            background-color: #0056b3;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        table, th, td {
            border: 1px solid #ddd;
        }

        th, td {
            padding: 12px;
            text-align: left;
        }

        th {
            background-color: #007bff;
            color: white;
            font-weight: 500;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .no-records {
            text-align: center;
            font-size: 18px;
            color: #888;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Filter Ticket Records</h1>
        <form action="SearchTicketServlet" method="get">
            <div class="form-group">
                <label for="ticket_no">Ticket No:</label>
                <input type="text" id="ticket_no" name="ticket_no">
            </div>
            <div class="form-group">
                <label for="passenger_name">Passenger Name:</label>
                <input type="text" id="passenger_name" name="passenger_name">
            </div>
            <div class="form-group">
                <label for="train_no">Train No:</label>
                <input type="text" id="train_no" name="train_no">
            </div>
            <div class="form-group">
                <label for="onboard_station">Onboard Station:</label>
                <input type="text" id="onboard_station" name="onboard_station">
            </div>
            <div class="form-group">
                <label for="destination_station">Destination Station:</label>
                <input type="text" id="destination_station" name="destination_station">
            </div>
            <div class="form-group">
                <label for="ticket_status">Ticket Status:</label>
                <select id="ticket_status" name="ticket_status">
                    <option value="">Any</option>
                    <option value="booked">Booked</option>
                    <option value="canceled">Canceled</option>
                    <option value="completed">Completed</option>
                </select>
            </div>
            <div class="form-group">
                <label for="onboard_date">Onboard Date (YYYY-MM-DD):</label>
                <input type="date" id="onboard_date" name="onboard_date">
            </div>
            <div class="form-group">
                <label for="booking_date">Booking Date (YYYY-MM-DD):</label>
                <input type="date" id="booking_date" name="booking_date">
            </div>
            <div class="form-group">
                <label for="trip_no">Trip No:</label>
                <input type="text" id="trip_no" name="trip_no">
            </div>
            <input type="submit" value="Search">
        </form>

        <!-- Display the search results -->
        <%
            List<TicketRecord> ticketRecords = (List<TicketRecord>) request.getAttribute("ticketRecords");
            if (ticketRecords != null && !ticketRecords.isEmpty()) {
        %>
        <h2>Search Results</h2>
        <table>
            <thead>
                <tr>
                    <th>Ticket No</th>
                    <th>Passenger Name</th>
                    <th>Train No</th>
                    <th>Onboard Station</th>
                    <th>Destination Station</th>
                    <th>Passenger Count</th>
                    <th>Onboard Date</th>
                    <th>Trip No</th>
                    <th>Booking Date</th>
                    <th>Ticket Status</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (TicketRecord record : ticketRecords) {
                %>
                <tr>
                    <td><%= record.getTicketNo() %></td>
                    <td><%= record.getPassengerName() %></td>
                    <td><%= record.getTrainNo() %></td>
                    <td><%= record.getOnboardStation() %></td>
                    <td><%= record.getDestinationStation() %></td>
                    <td><%= record.getPassengerCount() %></td>
                    <td><%= record.getOnboardDate() %></td>
                    <td><%= record.getTripNo() %></td>
                    <td><%= record.getBookingDate() %></td>
                    <td><%= record.getTicketStatus() %></td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
        <%
            } else {
        %>
        <p class="no-records">No records found</p>
        <%
            }
        %>
    </div>
</body>
</html>
