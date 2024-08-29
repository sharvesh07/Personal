<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Open Multiple Links</title>
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
        }

        .container {
            text-align: center;
            background-color: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            max-width: 90%;
            width: 350px;
        }

        h2 {
            margin-bottom: 20px;
            color: #333;
            font-size: 22px;
        }

        .button-container {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .button-container button {
            padding: 12px 20px;
            font-size: 16px;
            color: #fff;
            background: linear-gradient(135deg, #007bff, #0056b3);
            border: none;
            border-radius: 8px;
            cursor: pointer;
            transition: background 0.3s ease, transform 0.2s ease;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
        }

        .button-container button:hover {
            background: linear-gradient(135deg, #0056b3, #007bff);
            transform: translateY(-2px);
        }

        .button-container button:focus {
            outline: none;
            box-shadow: 0 0 5px rgba(0, 123, 255, 0.7);
        }

        .button-container button i {
            font-size: 18px;
        }
    </style>
    <script type="text/javascript">
        function openLink(url) {
            window.location.href = url; // Navigate to the link within the same tab
        }
    </script>
    <!-- Adding Font Awesome for icons (optional) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" integrity="sha512-Fo3rlrZj/k6a13l2Q/qy8Gkwf5hCA45p4g1RjX5X2VPtN+LSb7SNzQe1oj7+5iN7x/v8nRh8eFfY6VW3G4YjZw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>
<body>
    <div class="container">
        <h2>Choose a Link to Open</h2>
        <div class="button-container">
            <!-- Button 1 -->
            <button type="button" onclick="openLink('trainsetup.jsp')">
                <i class="fas fa-link"></i> Train Setup
            </button>

            <!-- Button 2 -->
            <button type="button" onclick="openLink('trainschedule.jsp')">
                <i class="fas fa-link"></i> Train Schedule
            </button>

            <!-- Button 3 -->
            <button type="button" onclick="openLink('ticketbooking.jsp')">
                <i class="fas fa-link"></i> Ticket Booking
            </button>

            <!-- Button 4 -->
            <button type="button" onclick="openLink('filterTickets.jsp')">
                <i class="fas fa-link"></i> Search Record
            </button>
        </div>
    </div>
</body>
</html>
