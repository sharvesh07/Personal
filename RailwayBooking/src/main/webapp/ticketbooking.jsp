<!DOCTYPE html>
<html>
<head>
    <title>Train Ticket Booking</title>
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
        form {
            background: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 10px;
            font-weight: bold;
        }
        input[type="text"],
        input[type="number"],
        input[type="date"],
        select,
        button {
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ddd;
            border-radius: 4px;
            width: 100%;
        }
        button {
            background-color: #007BFF;
            color: #fff;
            border: none;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #0056b3;
        }
        hr {
            margin: 20px 0;
        }
        .result {
            color: #007BFF;
        }
    </style>
</head>
<body>
    <h1>Train Ticket Booking</h1>

    <!-- Ticket Booking Form -->
    <form action="TicketBookingServlet" method="post">
        <input type="hidden" name="action" value="add">
        <label for="passengerName">Passenger Name:</label>
        <input type="text" id="passengerName" name="passengerName" required>

        <label for="trainNo">Train No:</label>
        <input type="number" id="trainNo" name="trainNo" required>

        <label for="onboardStation">Onboard Station:</label>
        <input type="text" id="onboardStation" name="onboardStation" required>

        <label for="destinationStation">Destination Station:</label>
        <input type="text" id="destinationStation" name="destinationStation" required>

        <label for="passengerCount">Passenger Count:</label>
        <input type="number" id="passengerCount" name="passengerCount" required>

        <label for="onboardDate">Onboard Date:</label>
        <input type="date" id="onboardDate" name="onboardDate" required>

        <label for="tripNo">Trip No:</label>
        <input type="number" id="tripNo" name="tripNo" required>

        <button type="submit">Book Ticket</button>
    </form>

    <hr>

    <!-- View Ticket Form -->
    <h2>View or Update Ticket</h2>
    <form action="TicketBookingServlet" method="get">
        <input type="hidden" name="action" value="view">
        <label for="viewTicketNo">Ticket No:</label>
        <input type="number" id="viewTicketNo" name="ticketNo" required>
        <button type="submit">View Ticket</button>
    </form>

    <!-- Update Ticket Form -->
    <form action="TicketRecordsServlet" method="post">
        <input type="hidden" name="action" value="update">
        <label for="updateTicketNo">Ticket No:</label>
        <input type="number" id="updateTicketNo" name="ticketNo" required>

        <label for="newPassengerCount">New Passenger Count:</label>
        <input type="number" id="newPassengerCount" name="newPassengerCount" required>

        <button type="submit">Update Ticket</button>
    </form>

    <!-- Cancel Ticket Form -->
    <form action="TicketRecordsServlet" method="post">
        <input type="hidden" name="action" value="delete">
        <label for="cancelTicketNo">Ticket No:</label>
        <input type="number" id="cancelTicketNo" name="ticketNo" required>
        <button type="submit">Cancel Ticket</button>
    </form>

</body>
</html>
