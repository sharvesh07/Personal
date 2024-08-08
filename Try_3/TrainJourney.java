import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TrainJourney {
    static List<Passenger> passengerList = new ArrayList<>();
    static Map<Integer, List<String>> trainJourneys = new HashMap<>();
    static Map<Integer, Integer> trainCapacities = new HashMap<>();
    static Map<Integer, Integer> trainCurrentStations = new HashMap<>();
    static List<Passenger> trainPassengerList = new ArrayList<>();
    static String filePath = "passenger_data.txt";
    static String journeyFilePath = "train_journey_details.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        loadTrainJourneys();

        // Initialize current stations for each train
        for (Integer trainNumber : trainJourneys.keySet()) {
            trainCurrentStations.put(trainNumber, 0); // Start at the first station
        }

        boolean continueJourney = true;
        while (continueJourney) {
            System.out.println("\nAvailable trains:");
            for (Integer trainNumber : trainJourneys.keySet()) {
                System.out.println("Train Number: " + trainNumber + " (Current Station: " +
                        trainJourneys.get(trainNumber).get(trainCurrentStations.get(trainNumber)) + ")");
            }

            System.out.print("Enter the train number to move to the next station (or -1 to exit): ");
            int trainNumber = scanner.nextInt();
            if (trainNumber == -1) {
                continueJourney = false;
                break;
            }

            if (!trainJourneys.containsKey(trainNumber)) {
                System.out.println("Invalid train number. Try again.");
                continue;
            }

            int currentStationIndex = trainCurrentStations.get(trainNumber);
            List<String> stations = trainJourneys.get(trainNumber);

            if (currentStationIndex < stations.size() - 1) {
                // Move to the next station
                trainCurrentStations.put(trainNumber, currentStationIndex + 1);
                String nextStation = stations.get(currentStationIndex + 1);
                System.out.println("Train " + trainNumber + " moved to station: " + nextStation);
                loadPassengerData();  // Load passenger data to check for updates
                dispatchPassengers(nextStation);
                boardNewPassengers(nextStation);
                showTrainPassengers();
            } else {
                System.out.println("Train " + trainNumber + " has reached its final destination.");
            }

            pressEnterToContinue();
        }

        System.out.println("Journey completed. Thank you for using the Train Journey system.");
        scanner.close();
    }

    private static void loadTrainJourneys() {
        try (BufferedReader reader = new BufferedReader(new FileReader(journeyFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int trainNumber = Integer.parseInt(parts[0]);
                String[] stations = parts[1].split(",");
                List<String> stationList = new ArrayList<>(Arrays.asList(stations));
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

    private static void dispatchPassengers(String currentStation) {
        Iterator<Passenger> iterator = trainPassengerList.iterator();
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getDestination_station().equals(currentStation)) {
                iterator.remove();
                System.out.println("Passenger alighted: " + passenger);
            }
        }
    }

    private static void boardNewPassengers(String currentStation) {
        Iterator<Passenger> iterator = passengerList.iterator();
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getOnboard_station().equals(currentStation)) {
                int trainNumber = passenger.getTrain_number();
                if (getTotalPassengers(trainNumber) + passenger.getFamilySize() <= trainCapacities.get(trainNumber)) {
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

    private static int getTotalPassengers(int trainNumber) {
        int total = 0;
        for (Passenger passenger : trainPassengerList) {
            if (passenger.getTrain_number() == trainNumber) {
                total += passenger.getFamilySize();
            }
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

    private static void showTrainPassengers() {
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
