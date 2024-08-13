class TrainJourneyDetails {
    private int trainNumber;
    private String[] stations;
    private int capacity;

    public TrainJourneyDetails(int trainNumber, String[] stations, int capacity) {
        this.trainNumber = trainNumber;
        this.stations = stations;
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public String[] getStations() {
        return stations;
    }
}
