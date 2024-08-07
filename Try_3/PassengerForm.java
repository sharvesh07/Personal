import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

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

public class PassengerForm {
    static List<Passenger> passengerList = new ArrayList<>();
    static String filePath = "passenger_data.txt";
    static int lastTicketNumber = 0;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        do {
            clearScreen();
            showMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createNewTicket();
                    break;
                case 2:
                    cancelTicket();
                    break;
                case 3:
                    showPassengerList();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
            if (choice != 4) pressEnterToContinue();
        } while (choice != 4);

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("1. Book a ticket");
        System.out.println("2. Cancel a ticket");
        System.out.println("3. Show passenger list");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void createNewTicket() {
        clearScreen();
        System.out.print("Enter passenger name: ");
        String passengerName = scanner.nextLine();

        System.out.print("Enter train number: ");
        int trainNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter onboard station: ");
        String onboardStation = scanner.nextLine();

        System.out.print("Enter destination station: ");
        String destinationStation = scanner.nextLine();

        lastTicketNumber++;
        Passenger passenger = new Passenger(passengerName, lastTicketNumber, trainNumber, onboardStation, destinationStation);
        passengerList.add(passenger);
        savePassengerData(passenger);

        System.out.println("Ticket booked successfully. Ticket Number: " + lastTicketNumber);
    }

    private static void cancelTicket() {
        clearScreen();
        System.out.print("Enter ticket number to cancel: ");
        int ticketNumber = scanner.nextInt();

        Iterator<Passenger> iterator = passengerList.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getTicket_number() == ticketNumber) {
                iterator.remove();
                System.out.println("Ticket canceled successfully.");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Ticket not found.");
        }

        updatePassengerFile();
    }

    private static void showPassengerList() {
        clearScreen();
        if (passengerList.isEmpty()) {
            System.out.println("No passengers found.");
        } else {
            for (Passenger passenger : passengerList) {
                System.out.println(passenger);
            }
        }
    }

    private static void savePassengerData(Passenger passenger) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(passenger.getPassenger_name() + "," + passenger.getTicket_number() + "," + passenger.getOnboard_station() + "," + passenger.getDestination_station());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("An error occurred while saving passenger data.");
            e.printStackTrace();
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
