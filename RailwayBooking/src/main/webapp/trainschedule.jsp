<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Train Schedule Management</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .container {
            width: 80%;
            margin: 0 auto;
        }
        h1 {
            text-align: center;
            color: #333;
        }
        form {
            margin-bottom: 20px;
            padding: 15px;
            background: #fff;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        h2 {
            color: #007bff;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .form-group input {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
        }
        .button {
            padding: 10px 20px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        .button:hover {
            background-color: #0056b3;
        }
        .output {
            margin-top: 20px;
            color: #007bff;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Train Schedule Management</h1>

    <!-- Insert Data Form -->
    <form action="TrainScheduleServlet" method="post">
        <h2>Insert Data</h2>
        <input type="hidden" name="action" value="create">
        <div class="form-group">
            <label for="trainNo">Train No:</label>
            <input type="number" id="trainNo" name="trainNo" required min="1">
        </div>
        <div class="form-group">
            <label for="tripNo">Trip No:</label>
            <input type="number" id="tripNo" name="tripNo" required min="1">
        </div>
        <div class="form-group">
            <label for="tripStartTime">Trip Start Time:</label>
            <input type="time" id="tripStartTime" name="tripStartTime" required>
        </div>
        <div class="form-group">
            <label for="tripEndTime">Trip End Time:</label>
            <input type="time" id="tripEndTime" name="tripEndTime" required>
        </div>
        <div class="form-group">
            <label for="tripDate">Trip Date:</label>
            <input type="date" id="tripDate" name="tripDate" required>
        </div>
        <button type="submit" class="button">Insert Data</button>
    </form>

    <!-- Update Data Form -->
    <form action="TrainScheduleServlet" method="post">
        <h2>Update Data</h2>
        <input type="hidden" name="action" value="update">
        <div class="form-group">
            <label for="updateTrainNo">Train No:</label>
            <input type="number" id="updateTrainNo" name="trainNo" required min="1">
        </div>
        <div class="form-group">
            <label for="updateTripNo">Trip No:</label>
            <input type="number" id="updateTripNo" name="tripNo" required min="1">
        </div>
        <div class="form-group">
            <label for="newStartTime">New Trip Start Time:</label>
            <input type="time" id="newStartTime" name="newStartTime" required>
        </div>
        <div class="form-group">
            <label for="newEndTime">New Trip End Time:</label>
            <input type="time" id="newEndTime" name="newEndTime" required>
        </div>
        <div class="form-group">
            <label for="newTripDate">New Trip Date:</label>
            <input type="date" id="newTripDate" name="newTripDate" required>
        </div>
        <button type="submit" class="button">Update Data</button>
    </form>

    <!-- Delete Data Form -->
    <form action="TrainScheduleServlet" method="post">
        <h2>Delete Data</h2>
        <input type="hidden" name="action" value="delete">
        <div class="form-group">
            <label for="deleteTrainNo">Train No:</label>
            <input type="number" id="deleteTrainNo" name="trainNo" required min="1">
        </div>
        <div class="form-group">
            <label for="deleteTripNo">Trip No:</label>
            <input type="number" id="deleteTripNo" name="tripNo" required min="1">
        </div>
        <button type="submit" class="button">Delete Data</button>
    </form>

    <!-- View Data Button -->
    <form action="TrainScheduleServlet" method="get">
        <h2>View Data</h2>
        <button type="submit" class="button">View All Train Schedule Data</button>
    </form>

    <!-- Populate Train Stations Button -->
    <form action="TrainScheduleServlet" method="post">
        <h2>Populate Train Stations</h2>
        <input type="hidden" name="action" value="populate">
        <button type="submit" class="button">Populate Train Stations Table</button>
    </form>

    <!-- Update Train Stations Button -->
    <form action="TrainScheduleServlet" method="post">
        <h2>Update Train Stations for Future Dates</h2>
        <input type="hidden" name="action" value="updateStations">
        <button type="submit" class="button">Update Train Stations</button>
    </form>

    <!-- Load Trip Log Button -->
    <form action="TrainScheduleServlet" method="post">
        <h2>Load Trip Log</h2>
        <input type="hidden" name="action" value="loadTripLog">
        <button type="submit" class="button">Load Trip Log</button>
    </form>

    <div class="output">
        <%
            String message = (String) request.getAttribute("message");
            if (message != null) {
                out.println("<p>" + message + "</p>");
            }
        %>
    </div>
</div>
</body>
</html>
