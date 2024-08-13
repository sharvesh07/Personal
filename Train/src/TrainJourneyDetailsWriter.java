import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TrainJourneyDetailsWriter {
    static String journeyFilePath = "train_journey_details.txt";

    public static void main(String[] args) {
        List<TrainJourneyDetails> journeys = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of trains: ");
        int numberOfTrains = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numberOfTrains; i++) {
            System.out.print("Enter train number: ");
            int trainNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter number of stations for train " + trainNumber + ": ");
            int numberOfStations = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String[] stations = new String[numberOfStations];
            System.out.println("Enter the stations for train " + trainNumber + ":");
            for (int j = 0; j < numberOfStations; j++) {
                System.out.print("Station " + (j + 1) + ": ");
                stations[j] = scanner.nextLine();
            }

            System.out.println("Enter Train Capacity: ");
            int capacity = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            journeys.add(new TrainJourneyDetails(trainNumber, stations, capacity));
        }

        writeJourneyDetails(journeys);

        System.out.println("Train journey details have been written to " + journeyFilePath);
        scanner.close();
    }

    private static void writeJourneyDetails(List<TrainJourneyDetails> journeys) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(journeyFilePath))) {
            for (TrainJourneyDetails journey : journeys) {
                writer.write(journey.getTrainNumber() + ";" + String.join(",", journey.getStations()) + ";" + journey.getCapacity());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing train journey details.");
            e.printStackTrace();
        }
    }
}

