<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Train Setup</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        .container {
            width: 80%;
            margin: auto;
            overflow: hidden;
        }
        h1, h3 {
            color: #333;
        }
        form {
            background: #fff;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        label {
            display: block;
            margin: 10px 0 5px;
        }
        input[type="number"], input[type="text"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        input[type="submit"] {
            background: #333;
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background: #555;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Train Setup Operations</h1>

        <!-- Create Form -->
        <form action="TrainSetupServlet" method="post">
            <h3>Create (Insert Data)</h3>
            <label for="trainNoCreate">Train No:</label>
            <input type="number" name="trainNo" id="trainNoCreate" required placeholder="Enter Train Number"><br>
            <label for="trainCapacity">Train Capacity:</label>
            <input type="number" name="trainCapacity" id="trainCapacity" required placeholder="Enter Train Capacity"><br>
            <label for="stations">Stations (comma-separated):</label>
            <input type="text" name="stations" id="stations" required placeholder="Enter Stations"><br>
            <input type="hidden" name="action" value="create">
            <input type="submit" value="Insert Data">
        </form>

        <!-- Update Form -->
        <form action="TrainSetupServlet" method="post">
            <h3>Update (Modify Data)</h3>
            <label for="trainNoUpdate">Train No:</label>
            <input type="number" name="trainNo" id="trainNoUpdate" required placeholder="Enter Train Number"><br>
            <label for="newTrainCapacity">New Train Capacity:</label>
            <input type="number" name="newTrainCapacity" id="newTrainCapacity" required placeholder="Enter New Train Capacity"><br>
            <label for="newStations">New Stations (comma-separated):</label>
            <input type="text" name="newStations" id="newStations" required placeholder="Enter New Stations"><br>
            <input type="hidden" name="action" value="update">
            <input type="submit" value="Update Data">
        </form>

        <!-- Delete Form -->
        <form action="TrainSetupServlet" method="post">
            <h3>Delete (Remove Data)</h3>
            <label for="trainNoDelete">Train No:</label>
            <input type="number" name="trainNo" id="trainNoDelete" required placeholder="Enter Train Number"><br>
            <input type="hidden" name="action" value="delete">
            <input type="submit" value="Delete Data">
        </form>

        <!-- Populate Train Stations Form -->
        <form action="TrainSetupServlet" method="post">
            <h3>Populate Train Stations</h3>
            <input type="hidden" name="action" value="populate">
            <input type="submit" value="Populate Train Stations">
        </form>

        <!-- Read Form -->
        <form action="TrainSetupServlet" method="get">
            <h3>Read (View Data)</h3>
            <input type="submit" value="View Data">
        </form>
    </div>
</body>
</html>
