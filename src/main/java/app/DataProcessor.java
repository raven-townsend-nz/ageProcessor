package app;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Stores a total and a count which can be added to. Calling getAverage will return total / count.
 * Useful for calculating a running average of Person's age.
 */
class AgeAverage {

    private int total = 0;
    private int count = 0;

    public AgeAverage() {};

    /** Add a person's age to the running average */
    public void add(Person person) {
        total += person.getAge();
        count += 1;
    }

    /** Get the current running average to 2dp */
    public String getAverage() {
        if (count == 0) {
            return "0.00";
        } else {
            return new Formatter().format("%.2f", (double) total / count).toString();
        }
    }
}

public class DataProcessor {

    /** File reader containing all the age-related json data */
    private ObjectMapper objectMapper;

    private List<Person> people = new ArrayList<>();

    /** This variable is used to cache the return value of the oldestPerson method, in case it is called again */
    private String oldestPersonString = null;

    /** This variable is used to cache the return value of the averageAge method, in case it is called again */
    private String averageAgeString = null;

    /** This variable is used to cache the return value of the youngestPerCountry method, in case it is called again */
    private String youngestPerCountryString = null;

    /** This variable is used to cache the return value of the averagePerCountry method, in case it is called again */
    private String averagePerCountryString = null;

    /** This variable is used to cache the return value of the nzAgeBands method, in case it is called again */
    private String nzAgeBandsString = null;

    public DataProcessor(FileReader fileReader) throws IOException {
        objectMapper = new ObjectMapper();
        readJson(fileReader);
    }

    private void readJson(FileReader fileReader) throws IOException {
        BufferedReader reader;
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
    }

    public void oldestPerson() {
        if (oldestPersonString == null) {
            Person maxAgePerson = people.get(0);
            for (Person person : people) {
                if (person.getAge() > maxAgePerson.getAge()) {
                    maxAgePerson = person;
                }
            }
            oldestPersonString =
                    "The details of the oldest person are:\n" +
                    maxAgePerson;
        }
        System.out.println(oldestPersonString);
    }

    public void averageAge() {
        if (averageAgeString == null) {
            AgeAverage average = new AgeAverage();
            for (Person person : people) {
                average.add(person);
            }
            averageAgeString = "Average age: " + average.getAverage();
        }
        System.out.println(averageAgeString);
    }

    public void youngestPersonPerCountry() {
        if (youngestPerCountryString == null) {
            HashMap<String, Person> map = new HashMap<>();
            for (Person person: people) {
                map.putIfAbsent(person.getCountry(), person);
                if (person.getAge() > map.get(person.getCountry()).getAge()) {
                    map.put(person.getCountry(), person);
                }
            }

            StringBuilder sb = new StringBuilder();
            Iterator<Map.Entry<String, Person>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Person> pair = it.next();
                sb.append("Youngest person in ")
                  .append(pair.getKey())
                  .append(": ")
                  .append(pair.getValue().toString())
                  .append("\n");
                it.remove(); // avoids a ConcurrentModificationException
            }
            youngestPerCountryString = sb.toString();
        }
        System.out.println(youngestPerCountryString);
    }

    public void averageAgePerCountry() {
        if (youngestPerCountryString == null) {
            HashMap<String, AgeAverage> map = new HashMap<>();
            for (Person person: people) {
                if (map.containsKey(person.getCountry())) {
                    map.get(person.getCountry()).add(person);
                } else {
                    AgeAverage average = new AgeAverage();
                    average.add(person);
                    map.put(person.getCountry(), average);
                }
            }

            StringBuilder sb = new StringBuilder();
            Iterator<Map.Entry<String, AgeAverage>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, AgeAverage> pair = it.next();
                sb.append("Average age in ")
                        .append(pair.getKey())
                        .append(": ")
                        .append(pair.getValue().getAverage())
                        .append("\n");
                it.remove(); // avoids a ConcurrentModificationException
            }
            youngestPerCountryString = sb.toString();
        }
        System.out.println(youngestPerCountryString);
    }

    public void nzAgeBands() {
        String country = "New Zealand";
        int ageBandWidth = 10;

        if (nzAgeBandsString == null) {
            HashMap<String, Integer> map = new HashMap<>();
            for (Person person : people) {
                if (person.getCountry().equals(country)) {
                    int ageBandLowerLimit = person.getAge() / ageBandWidth;
                    String ageBand = ageBandLowerLimit + " - " + (ageBandLowerLimit + ageBandWidth - 1);
                    if (map.containsKey(ageBand)) {
                        map.put(ageBand, map.get(ageBand) + 1);
                    } else {
                        map.put(ageBand, 1);
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Integer> pair = it.next();
                sb.append("Number of people in the age band ")
                        .append(pair.getKey())
                        .append(": ")
                        .append(pair.getValue())
                        .append("\n");
                it.remove(); // avoids a ConcurrentModificationException
            }
            nzAgeBandsString = sb.toString();
        }
    }
}
