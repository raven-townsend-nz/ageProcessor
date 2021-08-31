package app;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


/**
 * Defines a Command Line Interface app to process a file of age data and print various summary datq
 */
public class App {

    private final Scanner cli;

    private DataProcessor data;

    /**
     * Default constructor. Initialise the CLI
     */
    public App() {
        cli = new Scanner(System.in);
    }

    /**
     * Main entry point of the App. Does not expect any argument
     *
     * @param args none expected
     */
    public static void main(String[] args) {
        App app = new App();
        app.runCli();
    }


    /**
     * Main entry point for the command line interface
     */
    private void runCli() {

        boolean quit = false;
        while (!quit) {

            boolean newFile = false;
            FileReader fileReader = getFileReader();
            try {
                data = new DataProcessor(fileReader);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                newFile = true;
            }

            while (!quit && !newFile) {
                displayMenu();
                String input = cli.nextLine();
                int menuItem;
                try {
                    menuItem = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    menuItem = -1;
                }
                switch (menuItem) {
                    case 1:
                        data.oldestPerson();
                        break;

                    case 2:
                        data.averageAge();
                        break;

                    case 3:
                        data.youngestPersonPerCountry();
                        break;

                    case 4:
                        data.averageAgePerCountry();
                        break;

                    case 5:
                        data.nzAgeBands();
                        break;

                    case 9:
                        newFile = true;
                        break;

                    case 0:
                        quit = true;
                        System.out.println("See you later!");
                        break;

                    default:
                        System.out.println("Unknown value entered");
                }
            }
        }
    }

    private FileReader getFileReader() {
        FileReader fileReader = null;

        while (fileReader == null) {
            System.out.println("Enter the path to your data file: ");
            String fileName = cli.nextLine();
            try {
                fileReader = new FileReader(fileName);
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Invalid file path.");
            }
        }
        return fileReader;
    }

    private void displayMenu() {
        System.out.println(
                "\nWhat do you want to do?\n"
                + "\t 1. Find the oldest person\n"
                + "\t 2. Calculate the average age\n"
                + "\t 3. Find the youngest person per country\n"
                + "\t 4. Find the average age per country\n"
                + "\t 5. Count age bands in New Zealand\n"
                + "\t 9. Choose a new file\n"
                + "\t 0. Exit\n"
                + "\n"
                + "Your Answer: ");
    }
}
