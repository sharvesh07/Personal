import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

public class Write{

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
        while(true){
            try{
                File f = new File("Text.txt");
                FileWriter fw = new FileWriter(f);
                
                System.out.println("Enter Text");
                String text = input.nextLine();

                fw.write("\n" + text);
                System.out.println("Text Written");
                fw.close();
                pressEnterToContinue();
            }
            catch(Exception E){
                System.out.println("File not Found");
            }
        }
    }
}