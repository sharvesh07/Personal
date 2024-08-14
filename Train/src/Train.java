import java.util.List;

public class Train {
    private int trainNumber;
    private int capacity;
    private List<String> stations;
    private int currentStationIndex;
    private boolean movingForward;

    public Train(int trainNumber, List<String> stations, int capacity) {
        this.trainNumber = trainNumber;
        this.capacity = capacity;
        this.stations = stations;
        this.currentStationIndex = 0; // Assuming the train starts at the first station
        this.movingForward = true; // Assuming the train starts moving forward
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public List<String> getStations() {
        return stations;
    }

    public int getCurrentStationIndex() {
        return currentStationIndex;
    }

    public String getCurrentStation() {
        return stations.get(currentStationIndex);
    }

    public boolean isMovingForward() {
        return movingForward;
    }

    public boolean move() {
        if (isMovingForward()) {
            if (currentStationIndex < stations.size() - 1) {
                currentStationIndex++;
                return true;
            } else {
                // The train has reached the last station
                setMovingForward(false);  // Change direction to backward
                return false; // Indicates the final destination has been reached
            }
        } else {
            if (currentStationIndex > 0) {
                currentStationIndex--;
                return true;
            } else {
                // The train has reached the first station
                setMovingForward(true);  // Change direction to forward
                return false; // Indicates the final destination has been reached
            }
        }
    }

    public void setMovingForward(boolean movingForward) {
        this.movingForward = movingForward;
    }



    public boolean canPassengerCompleteJourney(Passenger passenger) {
        int onboardIndex = stations.indexOf(passenger.getOnboard_station());
        int destinationIndex = stations.indexOf(passenger.getDestination_station());

        // Check if the indices are valid
        if (onboardIndex == -1 || destinationIndex == -1 || onboardIndex >= destinationIndex) {
            return false; // Invalid journey
        }

        // Simulate the journey
        for (int i = onboardIndex; i < destinationIndex; i++) {
            int currentStationCapacity = getCapacityAtStation(i);
            if (currentStationCapacity < passenger.getFamilySize()) {
                return false; // Not enough capacity at this station
            }
        }

        return true; // Journey is feasible
    }

    private int getCapacityAtStation(int stationIndex) {
        // For simplicity, assume capacity is constant. In a real scenario, you would track the capacity dynamically.
        return this.capacity;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public String toString() {
        return "Train Number: " + trainNumber + ", Capacity: " + capacity + ", Stations: " + stations + ", Current Station Index: " + currentStationIndex + ", Moving Forward: " + movingForward;
    }


    public String getPreviousStation() {
        int currentStationIndex = getCurrentStationIndex();

        if (isMovingForward()) {
            // If moving forward, the previous station is one index behind the current station
            if (currentStationIndex > 0) {
                return stations.get(currentStationIndex - 1);
            } else {
                // If the train is at the first station, there is no previous station
                return "No previous station (at starting point)";
            }
        } else {
            // If moving backward, the previous station is one index ahead (as we are moving backward)
            if (currentStationIndex < stations.size() - 1) {
                return stations.get(currentStationIndex + 1);
            } else {
                // If the train is at the last station, there is no previous station
                return "No previous station (at final destination)";
            }
        }
    }

}
