public class Passenger {
    private String passenger_name;
    private int ticket_number;
    private int train_number;
    private String onboard_station;
    private String destination_station;
    private int familySize;

    public Passenger(String passenger_name, int ticket_number, int train_number, String onboard_station, String destination_station, int familySize) {
        this.passenger_name = passenger_name;
        this.ticket_number = ticket_number;
        this.train_number = train_number;
        this.onboard_station = onboard_station;
        this.destination_station = destination_station;
        this.familySize = familySize;
    }

    public String getPassenger_name() {
        return passenger_name;
    }

    public int getTicket_number() {
        return ticket_number;
    }

    public int getTrain_number() {
        return train_number;
    }

    public String getOnboard_station() {
        return onboard_station;
    }

    public String getDestination_station() {
        return destination_station;
    }

    public int getFamilySize() {
        return familySize;
    }

    @Override
    public String toString() {
        return passenger_name + " (Ticket Number: " + ticket_number + ", Train Number: " + train_number +
                ", Onboard Station: " + onboard_station + ", Destination Station: " + destination_station +
                ", Family Size: " + familySize + ")";
    }

}
