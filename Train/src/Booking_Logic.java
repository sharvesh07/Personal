import java.util.ArrayList;

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

    public boolean isCapacityAvailable(String onboardStation, String destinationStation, int familySize) {
        int onboardIndex = stations.indexOf(onboardStation);
        int destinationIndex = stations.indexOf(destinationStation);

        System.out.println("Onboard Station: " + onboardStation + " (Index: " + onboardIndex + ")");
        System.out.println("Destination Station: " + destinationStation + " (Index: " + destinationIndex + ")");
        System.out.println("Station List: " + stations);

        if (onboardIndex == -1 || destinationIndex == -1 || onboardIndex >= destinationIndex) {
            System.out.println("Invalid route from " + onboardStation + " to " + destinationStation);
            return false;
        }

        // Check if there is enough capacity on the train for the family
        System.out.println("Checking capacity between " + onboardStation + " and " + destinationStation);
        return true; // Placeholder: Replace with actual capacity check logic
    }


    public void updateBooking(String onboardStation, String destinationStation, int count) {
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
