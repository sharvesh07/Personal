import java.util.Scanner;
import java.util.ArrayList;

class ticket{

    private String passenger_name;
    private int ticket_number;
    private int train_number;
    private String onbaord_station;
    private String destination_station;

    public ticket(String passenger_name, int ticket_number, int train_number, String onbaord_station, String destination_station){
        this.passenger_name = passenger_name;
        this.ticket_number = ticket_number;
        this.train_number = train_number;
        this.onbaord_station = onbaord_station;
        this.destination_station = destination_station;
    }

    public void show_details(){
        System.out.println("Passenger name: " + this.passenger_name);
        System.out.println("Ticket number: " + this.ticket_number);
        System.out.println("Train number: " + this.train_number);
        System.out.println("Onbaord station: " + this.onbaord_station);
        System.out.println("Destination station: " + this.destination_station);
    }

    public int get_ticket_number(){
        return this.ticket_number;
    }

    public String get_onboard_station(){
        return this.onbaord_station;
    }

    public String get_destination_station(){
        return this.destination_station;
    }

}

public class Main{

    static void show_menu(){
        System.out.println("1. Book a ticket");
        System.out.println("2. Cancel a ticket");
        System.out.println("3. Show all tickets");
        System.out.println("4. Exit");
    }

    public static void main(String[] args){
        
        //int train_capacity;
        int total_tickets = 0;

        ArrayList<String> stations = new ArrayList<String>();
        
        stations.add("Station_A");
        stations.add("Station_B");
        stations.add("Station_C");
        stations.add("Station_D");
        stations.add("Station_E");
        stations.add("Station_F");
        stations.add("Station_G");
        stations.add("Station_H");
        stations.add("Station_I");
        stations.add("Station_J");

        Scanner input = new Scanner(System.in);

        //System.out.println("Enter the train capacity: ");
        //train_capacity = input.nextInt();

        int station_index = 0;
        String current_station = stations.get(station_index);

        System.out.println("Enter the Onboarding Staion: ");
        String onbaord_station = input.next();

        System.out.println("Enter the Destination Staion: ");
        String destination_station = input.next();

        if (!stations.contains(onbaord_station)){
            System.out.println("Invalid Onbaord station");
            return;
        }

        if (onbaord_station.equals(destination_station)){
            System.out.println("Onbaord station and Destination station cannot be same");
            return;
        }

        else if (stations.indexOf(onbaord_station) > stations.indexOf(destination_station)){
            System.out.println("Onbaord station cannot be after Destination station");
            return;
        }

    }

}