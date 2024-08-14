import java.io.*;
import java.util.*;

public class BookingSystem {

    private Scanner scanner = new Scanner(System.in);
    private String passengerFilePath;
    private String journeyFilePath;
    private Map<Integer, Train> trainMap = new HashMap<>();
    private ArrayList<Passenger> passengerList = new ArrayList<>();
    private int nextTicketNumber = 1; // Start ticket numbering from 1

    public BookingSystem(String passengerFilePath, String journeyFilePath) {
        this.passengerFilePath = passengerFilePath;
        this.journeyFilePath = journeyFilePath;
        loadTrainJourneys();
        loadPassengerData();
        initializeNextTicketNumber();
    }

    // Initialize next ticket number based on the highest existing ticket number
    private void initializeNextTicketNumber() {
        int maxTicketNumber = 0;
        for (Passenger p : passengerList) {
            if (p.getTicket_number() > maxTicketNumber) {
                maxTicketNumber = p.getTicket_number();
            }
        }
        nextTicketNumber = maxTicketNumber + 1; // Set next available ticket number
    }

    private void loadTrainJourneys() {
        try (BufferedReader reader = new BufferedReader(new FileReader(journeyFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int trainNumber = Integer.parseInt(parts[0]);
                String[] stations = parts[1].split(",");
                List<String> stationList = new ArrayList<>(Arrays.asList(stations));
                int trainCapacity = Integer.parseInt(parts[2]);
                Train train = new Train(trainNumber, stationList, trainCapacity);
                trainMap.put(trainNumber, train); // Populate trainMap instead of TrainJourney.trains
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading train journey details.");
            e.printStackTrace();
        }
    }

    private void loadPassengerData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(passengerFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    try {
                        String passenger_name = parts[0];
                        int ticket_number = Integer.parseInt(parts[1]);
                        int train_number = Integer.parseInt(parts[2]);
                        String onboard_station = parts[3];
                        String destination_station = parts[4];
                        int familySize = Integer.parseInt(parts[5]);

                        Passenger p = new Passenger(passenger_name, ticket_number, train_number, onboard_station, destination_station, familySize);
                        passengerList.add(p);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format in passenger data: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading passenger data: " + e.getMessage());
        }
    }

    private void savePassengerData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(passengerFilePath))) {
            for (Passenger p : passengerList) {
                writer.write(String.join(";",
                        p.getPassenger_name(),
                        String.valueOf(p.getTicket_number()),
                        String.valueOf(p.getTrain_number()),
                        p.getOnboard_station(),
                        p.getDestination_station(),
                        String.valueOf(p.getFamilySize())));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving passenger data: " + e.getMessage());
        }
    }

    public boolean isBookingAllowed(Passenger p) {
        Train train = trainMap.get(p.getTrain_number());
        if (train == null) {
            System.out.println("Train number " + p.getTrain_number() + " not found.");
            return false;
        }

        Booking_Logic bookingLogic = new Booking_Logic(new ArrayList<>(train.getStations()), train.getCapacity());
        return bookingLogic.isCapacityAvailable(p.getOnboard_station(), p.getDestination_station(), p.getFamilySize());
    }

    public boolean bookTicket(Passenger passenger) {
        Train train = trainMap.get(passenger.getTrain_number());

        if (train == null) {
            System.out.println("Train not found.");
            return false;
        }

        // Check if passenger can board the train
        if (!isBookingAllowed(passenger)) {
            return false;
        }

        // Check capacity before adding passenger
        int currentCapacity = getTotalPassengers(passenger.getTrain_number());
        int familySize = passenger.getFamilySize();
        if (currentCapacity + familySize > train.getCapacity()) {
            System.out.println("Not enough capacity on the train.");
            return false;
        }

        // Add passenger to the passenger list
        passengerList.add(passenger);
        savePassengerData(); // Save passenger data after booking

        return true;
    }

    private int getTotalPassengers(int trainNumber) {
        int count = 0;
        for (Passenger p : passengerList) {
            if (p.getTrain_number() == trainNumber) {
                count += p.getFamilySize();
            }
        }
        return count;
    }

    public void showAllPassengers() {
        for (Passenger p : passengerList) {
            System.out.println(p);
        }
    }

    public void bookTicketFromForm() {
        System.out.print("Enter Passenger Name: ");
        String passenger_name = scanner.nextLine().trim();

        System.out.print("Enter Train Number: ");
        int train_number;
        try {
            train_number = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid train number. Please enter a valid number.");
            return;
        }

        System.out.print("Enter Onboard Station: ");
        String onboard_station = scanner.nextLine().trim();

        System.out.print("Enter Destination Station: ");
        String destination_station = scanner.nextLine().trim();

        System.out.print("Enter Family Size: ");
        int familySize;
        try {
            familySize = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid family size. Please enter a valid number.");
            return;
        }

        int ticket_number = nextTicketNumber++;

        Passenger passenger = new Passenger(passenger_name, ticket_number, train_number, onboard_station, destination_station, familySize);

        boolean bookingStatus = bookTicket(passenger);
        if (bookingStatus) {
            System.out.println("Booking successful.");
        } else {
            System.out.println("Booking failed.");
        }
    }

    private void cancelTickets() {
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
                savePassengerData();  // Save passenger data after cancellation
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Ticket not found.");
        }
    }

    public static void main(String[] args) {
        BookingSystem bookingSystem = new BookingSystem("passenger_data.txt", "train_journey_details.txt");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Book Ticket");
            System.out.println("2. Show All Passengers");
            System.out.println("3. Cancel Ticket");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    bookingSystem.bookTicketFromForm();
                    break;
                case 2:
                    bookingSystem.showAllPassengers();
                    break;
                case 3:
                    bookingSystem.cancelTickets();
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
