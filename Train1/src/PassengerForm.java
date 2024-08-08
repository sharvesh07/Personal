import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

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
            scanner.nextLine(); // Consume newlines


            switch (choice) {
                case 1:
                    createNewTickets();
                    break;
                case 2:
                    cancelTickets();
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
        System.out.println("1. Book tickets");
        System.out.println("2. Cancel tickets");
        System.out.println("3. Show passenger list");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void createNewTickets() {
        clearScreen();
        System.out.println("\nBooking tickets for a family:-");

        System.out.print("Enter passenger name: ");
        String passengerName = scanner.nextLine();

        System.out.print("Enter train number: ");
        int trainNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter onboard station: ");
        String onboardStation = scanner.nextLine();

        System.out.print("Enter destination station: ");
        String destinationStation = scanner.nextLine();

        System.out.print("Enter family size: ");
        int familySize = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        lastTicketNumber++;
        Passenger passenger = new Passenger(passengerName, lastTicketNumber, trainNumber, onboardStation, destinationStation, familySize);
        passengerList.add(passenger);
        savePassengerData(passenger);

        System.out.println("Tickets booked successfully for " + familySize + " person(s). Ticket Number: " + lastTicketNumber);
    }

    private static void cancelTickets() {
        clearScreen();
        System.out.print("Enter ticket number to cancel: ");
        int ticketNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Iterator<Passenger> iterator = passengerList.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getTicket_number() == ticketNumber) {
                iterator.remove();
                System.out.println("Ticket for " + passenger.getFamilySize() + " person(s) canceled successfully.");
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
            writer.write(passenger.getPassenger_name() + "," + passenger.getTicket_number() + "," + passenger.getTrain_number() + "," + passenger.getOnboard_station() + "," + passenger.getDestination_station() + "," + passenger.getFamilySize());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("An error occurred while saving passenger data.");
            e.printStackTrace();
        }
    }

    private static void updatePassengerFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Passenger passenger : passengerList) {
                writer.write(passenger.getPassenger_name() + "," + passenger.getTicket_number() + "," + passenger.getTrain_number() + "," + passenger.getOnboard_station() + "," + passenger.getDestination_station() + "," + passenger.getFamilySize());
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
