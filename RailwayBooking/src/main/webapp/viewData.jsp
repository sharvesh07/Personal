<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Train Setup Data</title>
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
            background: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            color: #333;
        }
        .data-item {
            margin-bottom: 10px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background: #f9f9f9;
        }
        a {
            color: #007bff;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Train Setup Data</h1>

        <%
            // Retrieve and display the train setup data
            java.util.List<String> data = (java.util.List<String>) request.getAttribute("trainSetupData");
            if (data != null && !data.isEmpty()) {
                for (String record : data) {
                    out.println("<div class='data-item'>" + record + "</div>");
                }
            } else {
                out.println("<p>No data available.</p>");
            }
        %>

        <a href="trainsetup.jsp">Back to Home</a>
    </div>
</body>
</html>
