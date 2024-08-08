import java.util.ArrayList;
import java.util.List;

public class Train {
    private int trainNumber;
    private List<String> stations;
    private int currentStationIndex;
    private int capacity;
    private boolean movingForward;

    public Train(int trainNumber, List<String> stations, int capacity) {
        this.trainNumber = trainNumber;
        this.stations = new ArrayList<>(stations);
        this.currentStationIndex = 0; // Start at the first station
        this.capacity = capacity;
        this.movingForward = true; // Initial direction is forward
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public List<String> getStations() {
        return stations;
    }

    public String getCurrentStation() {
        return stations.get(currentStationIndex);
    }

    public boolean move() {
        if (movingForward) {
            if (currentStationIndex < stations.size() - 1) {
                currentStationIndex++;
                return true; // Successfully moved forward
            } else {
                // Reached the final station, switch direction
                movingForward = false;
                return move(); // Try to move backward
            }
        } else {
            if (currentStationIndex > 0) {
                currentStationIndex--;
                return true; // Successfully moved backward
            } else {
                // Reached the first station, switch direction
                movingForward = true;
                return move(); // Try to move forward
            }
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isMovingForward() {
        return movingForward;
    }

    public int getCurrentStationIndex() {
        return currentStationIndex;
    }
}
