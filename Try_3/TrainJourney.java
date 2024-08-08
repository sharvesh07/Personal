import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TrainJourney {
    static List<Passenger> passengerList = new ArrayList<>();
    static Map<Integer, List<String>> trainJourneys = new HashMap<>();
    static Map<Integer, Integer> trainCapacities = new HashMap<>();
    static String filePath = "passenger_data.txt";
    static String journeyFilePath = "train_journey_details.txt";

    public static void main(String[] args) {
        // Load train journeys and capacities
        loadTrainJourneys();

        // Process each train
        for (Map.Entry<Integer, List<String>> entry : trainJourneys.entrySet()) {
            int trainNumber = entry.getKey();
            List<String> stationsList = entry.getValue();
            int trainCapacity = trainCapacities.getOrDefault(trainNumber, 10);

            List<Passenger> trainPassengerList = new ArrayList<>();
            System.out.println("Processing Train Number: " + trainNumber);

            for (String currentStation : stationsList) {
                clearScreen();
                System.out.println("Train " + trainNumber + " arrived at: " + currentStation);
                loadPassengerData();  // Load passenger data at each station to check for updates
                dispatchPassengers(currentStation, trainPassengerList);
                boardNewPassengers(currentStation, trainPassengerList, trainCapacity);
                showTrainPassengers(trainPassengerList);
                pressEnterToContinue();
            }

            System.out.println("Journey for Train Number " + trainNumber + " completed.");
        }

        System.out.println("All journeys completed. Thank you for using the Train Journey system.");
    }

    private static void loadTrainJourneys() {
        try (BufferedReader reader = new BufferedReader(new FileReader(journeyFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int trainNumber = Integer.parseInt(parts[0]);
                String[] stations = parts[1].split(",");
                List<String> stationList = new ArrayList<>();
                for (String station : stations) {
                    stationList.add(station);
                }
                trainJourneys.put(trainNumber, stationList);
                trainCapacities.put(trainNumber, 10); // Default capacity; adjust if needed
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading train journey details.");
            e.printStackTrace();
        }
    }

    private static void loadPassengerData() {
        passengerList.clear(); // Clear the existing passenger list before loading
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Passenger passenger = new Passenger(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3], parts[4], Integer.parseInt(parts[5]));
                    passengerList.add(passenger);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading passenger data.");
            e.printStackTrace();
        }
    }

    private static void dispatchPassengers(String currentStation, List<Passenger> trainPassengerList) {
        // Remove passengers whose destination station matches the current station
        Iterator<Passenger> iterator = trainPassengerList.iterator();
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getDestination_station().equals(currentStation)) {
                iterator.remove();
                System.out.println("Passenger alighted: " + passenger);
            }
        }
    }

    private static void boardNewPassengers(String currentStation, List<Passenger> trainPassengerList, int trainCapacity) {
        // Add passengers whose onboarding station matches the current station
        Iterator<Passenger> iterator = passengerList.iterator();
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getOnboard_station().equals(currentStation)) {
                if (getTotalPassengers(trainPassengerList) + passenger.getFamilySize() <= trainCapacity) {
                    trainPassengerList.add(passenger);
                    iterator.remove();
                    System.out.println("Passenger boarded: " + passenger);
                } else {
                    System.out.println("Train is at full capacity. Passenger could not board: " + passenger);
                }
            }
        }
        updatePassengerFile();
    }

    private static int getTotalPassengers(List<Passenger> trainPassengerList) {
        // Calculate the total number of passengers currently on the train
        int total = 0;
        for (Passenger passenger : trainPassengerList) {
            total += passenger.getFamilySize();
        }
        return total;
    }

    private static void updatePassengerFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Passenger passenger : passengerList) {
                writer.write(passenger.getPassenger_name() + "," + passenger.getTicket_number() + "," + passenger.getTrain_number() + "," + passenger.getOnboard_station() + "," + passenger.getDestination_station() + "," + passenger.getFamilySize());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating passenger data.");
            e.printStackTrace();
        }
    }

    private static void showTrainPassengers(List<Passenger> trainPassengerList) {
        System.out.println("\nPassengers currently on the train:");
        if (trainPassengerList.isEmpty()) {
            System.out.println("No passengers on the train.");
        } else {
            for (Passenger passenger : trainPassengerList) {
                System.out.println(passenger);
            }
        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void pressEnterToContinue() {
        System.out.println("Press Enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
