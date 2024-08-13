import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Booking_Check {

    private String filePath;
    private String journeyFilePath;
    private Map<Integer, Train> trainMap = new HashMap<>();
    private ArrayList<Passenger> passengerList = new ArrayList<>();

    public Booking_Check(String filePath, String journeyFilePath) {
        this.filePath = filePath;
        this.journeyFilePath = journeyFilePath;
        loadTrainJourneys();
        loadPassengerData();
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
                        ArrayList<String> stationList = new ArrayList<>(Arrays.asList(stations));
                        int trainCapacity = Integer.parseInt(parts[2]);
                        Train train = new Train(trainNumber, stationList, trainCapacity);
                        trainMap.put(trainNumber, train);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format in train journey details: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while loading train journey details: " + e.getMessage());
        }
    }

    public void loadPassengerData() {
        passengerList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
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
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format in passenger data: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while loading passenger data: " + e.getMessage());
        }
    }

    public void bookCheck(int trainNumber) {
        Train train = trainMap.get(trainNumber);
        if (train == null) {
            System.out.println("Invalid train number: " + trainNumber);
            return;
        }

        Booking_Logic logic = new Booking_Logic((ArrayList<String>) train.getStations(), train.getCapacity());
        ArrayList<Passenger> passengers = getPassengersForTrain(trainNumber);
        logic.checkBooking(passengers);
    }

    public ArrayList<Passenger> getPassengersForTrain(int trainNumber) {
        ArrayList<Passenger> trainPassengers = new ArrayList<>();
        for (Passenger p : passengerList) {
            if (p.getTrain_number() == trainNumber) {
                trainPassengers.add(p);
            }
        }
        return trainPassengers;
    }

    public boolean isBookingAllowed(Passenger passenger) {
        Train train = trainMap.get(passenger.getTrain_number());
        if (train == null) return false;

        ArrayList<String> stations = (ArrayList<String>) train.getStations();
        if (!stations.contains(passenger.getOnboard_station()) ||
                !stations.contains(passenger.getDestination_station())) {
            return false;
        }

        Booking_Logic logic = new Booking_Logic(stations, train.getCapacity());
        return logic.isCapacityAvailable(passenger.getOnboard_station(), passenger.getDestination_station(), passenger.getFamilySize());
    }

    public static void main(String[] args) {
        Booking_Check bookingCheck = new Booking_Check("passenger_data.txt", "train_journey_details.txt");
        bookingCheck.bookCheck(1);
    }
}
