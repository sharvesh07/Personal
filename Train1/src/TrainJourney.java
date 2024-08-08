import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TrainJourney {
    static List<Passenger> passengerList = new ArrayList<>();
    static Map<Integer, Train> trains = new HashMap<>();
    static List<Passenger> trainPassengerList = new ArrayList<>();
    static String filePath = "passenger_data.txt";
    static String journeyFilePath = "train_journey_details.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        loadTrainJourneys();

        boolean continueJourney = true;
        while (continueJourney) {
            System.out.println("\nAvailable trains:");
            for (Train train : trains.values()) {
                System.out.println("Train Number: " + train.getTrainNumber() +
                        " (Current Station: " + train.getCurrentStation() +
                        ", Direction: " + (train.isMovingForward() ? "Forward" : "Backward") + ")");
            }

            System.out.print("Enter the train number to move to the next station (or -1 to exit): ");
            int trainNumber = scanner.nextInt();
            if (trainNumber == -1) {
                continueJourney = false;
                break;
            }

            if (!trains.containsKey(trainNumber)) {
                System.out.println("Invalid train number. Try again.");
                continue;
            }

            Train train = trains.get(trainNumber);

            if (train.move()) {
                String currentStation = train.getCurrentStation();
                System.out.println("Train " + trainNumber + " moved to station: " + currentStation +
                        " (Direction: " + (train.isMovingForward() ? "Forward" : "Backward") + ")");
                loadPassengerData();  // Load passenger data to check for updates
                dispatchPassengers(currentStation);
                boardNewPassengers(currentStation, trainNumber, train.isMovingForward());
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
                Train train = new Train(trainNumber, stationList, 10); // Default capacity; adjust if needed
                trains.put(trainNumber, train);
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

    private static void boardNewPassengers(String currentStation, int trainNumber, boolean movingForward) {
        Train train = trains.get(trainNumber);
        Iterator<Passenger> iterator = passengerList.iterator();
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getOnboard_station().equals(currentStation)) {
                if (movingForward) {
                    // Move forward only if the station index matches or is before current station
                    if (train.getStations().indexOf(passenger.getOnboard_station()) <= train.getCurrentStationIndex()) {
                        if (getTotalPassengers(trainNumber) + passenger.getFamilySize() <= train.getCapacity()) {
                            trainPassengerList.add(passenger);
                            iterator.remove();
                            System.out.println("Passenger boarded: " + passenger);
                        } else {
                            System.out.println("Train is at full capacity. Passenger could not board: " + passenger);
                        }
                    }
                } else {
                    // Move backward only if the station index matches or is after current station
                    if (train.getStations().indexOf(passenger.getOnboard_station()) >= train.getCurrentStationIndex()) {
                        if (getTotalPassengers(trainNumber) + passenger.getFamilySize() <= train.getCapacity()) {
                            trainPassengerList.add(passenger);
                            iterator.remove();
                            System.out.println("Passenger boarded: " + passenger);
                        } else {
                            System.out.println("Train is at full capacity. Passenger could not board: " + passenger);
                        }
                    }
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
