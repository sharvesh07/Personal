import java.util.ArrayList;
import java.util.Arrays;

public class Booking_Logic {

    private ArrayList<String> stations;
    private int trainCapacity;
    private int[] passengerCountArr;

    public Booking_Logic(ArrayList<String> stations, int trainCapacity) {
        this.stations = new ArrayList<>(stations);
        this.trainCapacity = trainCapacity;
        this.passengerCountArr = new int[stations.size() - 1]; // One less segment than stations
    }

    public void checkBooking(ArrayList<Passenger> passengers) {
        for (Passenger p : passengers) {
            bookPassenger(p);
        }
    }

    public boolean isCapacityAvailable(String onboardStation, String destinationStation, int count) {
        int obIndex = stations.indexOf(onboardStation);
        int desIndex = stations.indexOf(destinationStation);

        if (obIndex < 0 || desIndex < 0 || obIndex >= desIndex) {
            System.out.println("Invalid route from " + onboardStation + " to " + destinationStation);
            return false;
        }

        // Check capacity for each segment of the journey
        for (int i = obIndex; i < desIndex; i++) {
            if (passengerCountArr[i] + count > trainCapacity) {
                return false; // Capacity exceeded on this segment
            }
        }

        return true;
    }

    private void updateBooking(String onboardStation, String destinationStation, int count) {
        int obIndex = stations.indexOf(onboardStation);
        int desIndex = stations.indexOf(destinationStation);

        if (obIndex < 0 || desIndex < 0 || obIndex >= desIndex) {
            System.out.println("Invalid route from " + onboardStation + " to " + destinationStation);
            return;
        }

        // Update capacity for each segment of the journey
        for (int i = obIndex; i < desIndex; i++) {
            passengerCountArr[i] += count;
        }
    }

    private void bookPassenger(Passenger p) {
        if (isCapacityAvailable(p.getOnboard_station(), p.getDestination_station(), p.getFamilySize())) {
            updateBooking(p.getOnboard_station(), p.getDestination_station(), p.getFamilySize());
            System.out.println("Passenger ID " + p.getTicket_number() + " booked successfully from " + p.getOnboard_station() + " to " + p.getDestination_station());
        } else {
            System.out.println("Insufficient capacity for passenger ID: " + p.getTicket_number());
        }
    }

}
