import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Passenger {
    private String passenger_name;
    private int ticket_number;
    private int train_number;
    private String onboard_station;
    private String destination_station;

    public Passenger(String passenger_name, int ticket_number, int train_number, String onboard_station, String destination_station) {
        this.passenger_name = passenger_name;
        this.ticket_number = ticket_number;
        this.train_number = train_number;
        this.onboard_station = onboard_station;
        this.destination_station = destination_station;
    }

    public String getPassenger_name() {
        return passenger_name;
    }

    public int getTicket_number() {
        return ticket_number;
    }

    public String getOnboard_station() {
        return onboard_station;
    }

    public String getDestination_station() {
        return destination_station;
    }

    @Override
    public String toString() {
        return passenger_name + " (Ticket Number: " + ticket_number + ", Onboard Station: " + onboard_station + ", Destination Station: " + destination_station + ")";
    }
}

public class TrainJourney {
    static List<Passenger> passengerList = new ArrayList<>();
    static List<Passenger> trainPassengerList = new ArrayList<>();
    static String filePath = "passenger_data.txt";
    static int trainCapacity = 2;

    public static void main(String[] args) {
        ArrayList<String> stationsList = new ArrayList<>();
        stationsList.add("Chennai");
        stationsList.add("Bangalore");
        stationsList.add("Mumbai");
        stationsList.add("Delhi");
        stationsList.add("Kolkata");
        stationsList.add("Hyderabad");

        for (String currentStation : stationsList) {
            clearScreen();
            System.out.println("Train arrived at: " + currentStation);
            loadPassengerData();  // Load passenger data at each station to check for updates
            dispatchPassengers(currentStation);
            boardNewPassengers(currentStation);
            showTrainPassengers();
            pressEnterToContinue();
        }

        System.out.println("Journey completed. Thank you for using the Train Journey system.");
    }

    private static void loadPassengerData() {
        passengerList.clear(); // Clear the existing passenger list before loading
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Passenger passenger = new Passenger(parts[0], Integer.parseInt(parts[1]), 0, parts[2], parts[3]);
                    passengerList.add(passenger);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading passenger data.");
            e.printStackTrace();
        }
    }

    private static void dispatchPassengers(String currentStation) {
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

    private static void boardNewPassengers(String currentStation) {
        // Add passengers whose onboarding station matches the current station
        Iterator<Passenger> iterator = passengerList.iterator();
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getOnboard_station().equals(currentStation)) {
                if (trainPassengerList.size() < trainCapacity) {
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

    private static void updatePassengerFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Passenger passenger : passengerList) {
                writer.write(passenger.getPassenger_name() + "," + passenger.getTicket_number() + "," + passenger.getOnboard_station() + "," + passenger.getDestination_station());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating passenger data.");
            e.printStackTrace();
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
