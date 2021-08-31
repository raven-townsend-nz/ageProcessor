package app;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * This class contains logic for the user to input a filename, and it will be processed and converted to a list
 * of Person objects
 */
public class FileProcessor {

    /** Reads user input */
    private final Scanner cli;

    /** Converts JSON string into an object */
    private final ObjectMapper objectMapper;

    /** List of Person objects generated from the input file */
    private List<Person> personList;

    /** Constructor gets the user to input a valid file path, and then converts this into a list of Person objects */
    public FileProcessor(Scanner cli) {
        this.cli = cli;
        objectMapper = new ObjectMapper();

        boolean fileProcessed = false;
        while (!fileProcessed) {
            try {
                FileReader fileReader = getFileReader();
                personList = readJson(fileReader);
                fileProcessed = true;
            } catch (IOException e) {
                System.out.println("File reading failed. Make sure your data follows the correct format.");
            }
        }

    }

    public List<Person> getPersonList() {
        return personList;
    }


    /**
     * Prompts the user to input a file path, if an invalid path is entered it will keep prompting until a valid one
     * is entered
     * @return a file reader for the user inputted file
     */
    private FileReader getFileReader() {
        FileReader fileReader = null;

        while (fileReader == null) {
            System.out.println("Enter the absolute path to your data file: ");
            String fileName = cli.nextLine();
            try {
                fileReader = new FileReader(fileName);
            } catch (FileNotFoundException e) {
                System.out.println("Invalid file path.");
            }
        }
        return fileReader;
    }


    /**
     * Reads the JSON file and converts it to a list of Person objects using ObjectMapper
     * @param fileReader for the user-selected file
     * @return a list of Person objects
     * @throws IOException thrown if file does not follow the correct format
     */
    private List<Person> readJson(FileReader fileReader) throws IOException {
        BufferedReader reader;
        List<Person> people = new ArrayList<>();
        try {
            reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            while(line != null) {
                Person person = objectMapper.readValue(line, Person.class);
                people.add(person);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException ignored) {
            throw new IOException("Error reading file");
        }
        return people;
    }
}
