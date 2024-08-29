<%@ page import="java.sql.Date" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Ticket</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        h1 {
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        a {
            color: #007bff;
            text-decoration: none;
            margin-top: 20px;
            display: inline-block;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <h1>Ticket Details</h1>
    <table>
        <tr>
            <th>Ticket No</th>
            <td><%= request.getAttribute("ticketNo") != null ? request.getAttribute("ticketNo") : "N/A" %></td>
        </tr>
        <tr>
            <th>Passenger Name</th>
            <td><%= request.getAttribute("passengerName") != null ? request.getAttribute("passengerName") : "N/A" %></td>
        </tr>
        <tr>
            <th>Train No</th>
            <td><%= request.getAttribute("trainNo") != null ? request.getAttribute("trainNo") : "N/A" %></td>
        </tr>
        <tr>
            <th>Onboard Station</th>
            <td><%= request.getAttribute("onboardStation") != null ? request.getAttribute("onboardStation") : "N/A" %></td>
        </tr>
        <tr>
            <th>Destination Station</th>
            <td><%= request.getAttribute("destinationStation") != null ? request.getAttribute("destinationStation") : "N/A" %></td>
        </tr>
        <tr>
            <th>Passenger Count</th>
            <td><%= request.getAttribute("passengerCount") != null ? request.getAttribute("passengerCount") : "N/A" %></td>
        </tr>
        <tr>
            <th>Onboard Date</th>
            <td><%= request.getAttribute("onboardDate") != null ? ((Date) request.getAttribute("onboardDate")).toString() : "N/A" %></td>
        </tr>
        <tr>
            <th>Trip No</th>
            <td><%= request.getAttribute("tripNo") != null ? request.getAttribute("tripNo") : "N/A" %></td>
        </tr>
        <tr>
            <th>Booking Date</th>
            <td><%= request.getAttribute("bookingDate") != null ? ((Date) request.getAttribute("bookingDate")).toString() : "N/A" %></td>
        </tr>
        <tr>
            <th>Ticket Status</th>
            <td><%= request.getAttribute("ticketStatus") != null ? request.getAttribute("ticketStatus") : "N/A" %></td>
        </tr>
    </table>
    <a href="index.jsp">Back to Home</a>
</body>
</html>
