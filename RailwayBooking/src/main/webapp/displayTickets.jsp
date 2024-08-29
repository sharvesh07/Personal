<%@ page import="java.util.List" %>
<%@ page import="com.ticketsearching.TicketRecord" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ticket Records</title>
    <style>
        /* Import Google Fonts */
        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500&display=swap');

        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f0f0f0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            padding: 20px;
        }

        .container {
            background-color: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            max-width: 90%;
            width: 1000px;
        }

        h1 {
            color: #333;
            margin-bottom: 20px;
            font-size: 24px;
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
            font-size: 14px;
        }

        th {
            background-color: #007bff;
            color: #fff;
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
        <h1>Ticket Records</h1>

        <%
            List<TicketRecord> ticketRecords = (List<TicketRecord>) request.getAttribute("ticketRecords");
            if (ticketRecords != null && !ticketRecords.isEmpty()) {
        %>
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
