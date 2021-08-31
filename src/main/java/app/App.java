package app;

import java.util.Scanner;


/**
 * Defines a Command Line Interface app to process a file of age data and print various summary datq
 */
public class App {

    /** Reads user input */
    private final Scanner cli;

    /** Contains methods to find statistics such as the oldest person */
    private DataProcessor data;

    /** If set to true the program will end */
    private boolean quit = false;

    /** If set to true the program will allow the user to enter a new file path */
    private boolean newFile = false;


    /**
     * Default constructor. Initialise the CLI
     */
    public App() {
        cli = new Scanner(System.in);
    }


    /**
     * Main entry point of the App. Does not expect any argument
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
        while (!quit) {
            FileProcessor fp = new FileProcessor(cli);
            data = new DataProcessor(fp.getPersonList());
            while (!quit && !newFile) {
                displayMenu();
                processUserInput();
            }
        }
    }


    /** Displays the menu to the user, with numbers corresponding to what actions to take */
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


    /**
     * Reads what the user has typed and calls the appropriate function. Returns the boolean 'newFile', so that the
     * function which calls this method can tell if the user has entered the new file command.
     */
    private void processUserInput() {
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
