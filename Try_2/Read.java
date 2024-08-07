import java.util.Scanner;
import java.io.File;

public class Read{

    static Scanner input = new Scanner(System.in);

    static void pressEnterToContinue() {
        System.out.println("Press Enter to continue...");
        input.nextLine();  // Wait for Enter key press
        clearScreen(); // Clear the screen after pressing Enter
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void main(String[] args){
        while (true){
            try{
                File f = new File ("Text.txt");
                Scanner input = new Scanner(f);
                while(input.hasNextLine()){
                    System.out.println(input.nextLine());
                }
                pressEnterToContinue();
            }
            catch(Exception E){
                System.out.println("File Not Found");
            }
           
        }
    }
}