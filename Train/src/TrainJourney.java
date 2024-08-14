import java.io.*;
import java.util.*;

public class TrainJourney {

    private List<Passenger> passengerList = new ArrayList<>();
    private List<Train> trains = new ArrayList<>();
    private List<Passenger> trainPassengerList = new ArrayList<>();
    private String filePath = "passenger_data.txt";
    private String journeyFilePath = "train_journey_details.txt";
    private int nextTicketNumber = 1;

    public static void main(String[] args) {
        TrainJourney journey = new TrainJourney();
        journey.loadTrainJourneys();
        journey.loadPassengerData();

        journey.run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        boolean continueJourney = true;

        while (continueJourney) {
            System.out.println("\nAvailable trains:");
            for (Train train : trains) {
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

            Train train = findTrainByNumber(trainNumber);
            if (train == null) {
                System.out.println("Invalid train number. Try again.");
                continue;
            }

            if (train.move()) {
                String previousStation = train.getPreviousStation();  // Get the previous station before moving
                dispatchPassengers(previousStation);  // Alight passengers at the previous station

                String currentStation = train.getCurrentStation();  // Move the train and get the new station
                System.out.println("Train " + trainNumber + " moved to station: " + currentStation +
                        " (Direction: " + (train.isMovingForward() ? "Forward" : "Backward") + ")");

                boardNewPassengers(currentStation, trainNumber, train.isMovingForward());  // Board passengers at the current station
                showTrainPassengers();  // Show the list of passengers currently on the train
            } else {
                System.out.println("Train " + trainNumber + " has reached its final destination.");
            }

            pressEnterToContinue();
        }

        System.out.println("Journey completed. Thank you for using the Train Journey system.");
        scanner.close();
    }

    private void loadTrainJourneys() {
        try (BufferedReader reader = new BufferedReader(new FileReader(journeyFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    try {
                        int trainNumber = Integer.parseInt(parts[0]);
                        String[] stations = parts[1].split(",");
                        List<String> stationList = new ArrayList<>(Arrays.asList(stations));
                        int trainCapacity = Integer.parseInt(parts[2]);
                        Train train = new Train(trainNumber, stationList, trainCapacity);
                        trains.add(train);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid format in train journey details: " + e.getMessage());
                    }
                } else {
                    System.err.println("Invalid format in train journey details.");
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while loading train journey details.");
            e.printStackTrace();
        }
    }

    private void loadPassengerData() {
        passengerList.clear(); // Clear the existing passenger list before loading
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int maxTicketNumber = 0; // Track the highest ticket number
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    try {
                        Passenger passenger = new Passenger(
                                parts[0],
                                Integer.parseInt(parts[1]),
                                Integer.parseInt(parts[2]),
                                parts[3],
                                parts[4],
                                Integer.parseInt(parts[5])
                        );
                        passengerList.add(passenger);
                        //trainPassengerList.add(passenger);
                        if (passenger.getTicket_number() > maxTicketNumber) {
                            maxTicketNumber = passenger.getTicket_number(); // Update max ticket number
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format in passenger data: " + e.getMessage());
                    }
                } else {
                    System.err.println("Invalid format in passenger data.");
                }
            }
            nextTicketNumber = maxTicketNumber + 1; // Initialize the next ticket number
        } catch (IOException e) {
            System.err.println("An error occurred while loading passenger data.");
            e.printStackTrace();
        }
    }

    private void dispatchPassengers(String currentStation) {
        Iterator<Passenger> iterator = trainPassengerList.iterator();
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getDestination_station().equals(currentStation)) {
                iterator.remove();
                System.out.println("Passenger alighted: " + passenger);
            }
        }
    }

    private void boardNewPassengers(String currentStation, int trainNumber, boolean movingForward) {
        Train train = findTrainByNumber(trainNumber);
        if (train == null) {
            System.out.println("Train not found.");
            return;
        }

        Iterator<Passenger> iterator = passengerList.iterator();
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            //System.out.println("*");
            if (passenger.getOnboard_station().equals(currentStation)) {
                if (movingForward) {
                    if (train.canPassengerCompleteJourney(passenger)) {
                        if (getTotalPassengers(trainNumber) + passenger.getFamilySize() <= train.getCapacity()) {
                            trainPassengerList.add(passenger);
                            iterator.remove();
                            System.out.println("Passenger boarded: " + passenger);
                        } else {
                            System.out.println("Train is at full capacity. Passenger could not board: " + passenger);
                        }
                    } else {
                        System.out.println("Passenger's journey is not feasible: " + passenger);
                    }
                } else {
                    // Handle backward direction
                    System.out.println("Handling backward direction (not yet implemented).");
                }
            }
        }
        updatePassengerFile();
    }

    private void updatePassengerFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Passenger passenger : passengerList) {
                writer.write(String.join(";",
                        passenger.getPassenger_name(),
                        String.valueOf(passenger.getTicket_number()),
                        String.valueOf(passenger.getTrain_number()),
                        passenger.getOnboard_station(),
                        passenger.getDestination_station(),
                        String.valueOf(passenger.getFamilySize())));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("An error occurred while updating passenger data.");
            e.printStackTrace();
        }
    }

    private void showTrainPassengers() {
        System.out.println("\nPassengers currently on the train:");
        if (trainPassengerList.isEmpty()) {
            System.out.println("No passengers on the train.");
        } else {
            for (Passenger passenger : trainPassengerList) {
                System.out.println(passenger);
            }
        }
    }

    private Train findTrainByNumber(int trainNumber) {
        for (Train train : trains) {
            if (train.getTrainNumber() == trainNumber) {
                return train;
            }
        }
        return null; // Train not found
    }

    private void pressEnterToContinue() {
        System.out.println("Press Enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean canBoardPassenger(Passenger passenger) {
        Train train = findTrainByNumber(passenger.getTrain_number());
        if (train == null) {
            System.out.println("Train not found.");
            return false;
        }
        int onboardIndex = train.getStations().indexOf(passenger.getOnboard_station());
        int destinationIndex = train.getStations().indexOf(passenger.getDestination_station());
        if (onboardIndex == -1 || destinationIndex == -1 || onboardIndex >= destinationIndex) {
            System.out.println("Passenger's onboard station is ahead of destination station. Cannot board.");
            return false;
        }
        return true;
    }

    public void addNewPassenger(String name, int trainNumber, String onboardStation, String destinationStation, int familySize) {
        Passenger newPassenger = new Passenger(name, nextTicketNumber++, trainNumber, onboardStation, destinationStation, familySize);
        passengerList.add(newPassenger);
        updatePassengerFile();
        System.out.println("New passenger added: " + newPassenger);
    }

    public void removePassenger(int ticketNumber) {
        Iterator<Passenger> iterator = passengerList.iterator();
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getTicket_number() == ticketNumber) {
                iterator.remove();
                System.out.println("Passenger removed: " + passenger);
                break;
            }
        }
        updatePassengerFile();
    }

    private int getTotalPassengers(int trainNumber) {
        int total = 0;
        for (Passenger passenger : trainPassengerList) {
            if (passenger.getTrain_number() == trainNumber) {
                total += passenger.getFamilySize();
            }
        }
        return total;
    }
}
