package app;


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

/**
 * This class contains methods to read a JSON file of age data and then calculate various metrics of the data
 */
public class DataProcessor {

    private List<Person> people;

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


    public DataProcessor(List<Person> people) {
        this.people = people;
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
                    "The oldest person is: " +
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
                if (person.getAge() < map.get(person.getCountry()).getAge()) {
                    map.put(person.getCountry(), person);
                }
            }

            StringBuilder sb = new StringBuilder();
            Iterator<Map.Entry<String, Person>> it = map.entrySet().iterator();
            sb.append("Youngest person for each country:\n");
            while (it.hasNext()) {
                Map.Entry<String, Person> pair = it.next();
                sb.append("\t")
                  .append(pair.getKey())
                  .append(": ")
                  .append(pair.getValue().toStringWithoutCountry())
                  .append("\n");
                it.remove(); // avoids a ConcurrentModificationException
            }
            youngestPerCountryString = sb.toString();
        }
        System.out.println(youngestPerCountryString);
    }

    public void averageAgePerCountry() {
        if (averagePerCountryString == null) {
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
            sb.append("Average age in each country:\n");
            while (it.hasNext()) {
                Map.Entry<String, AgeAverage> pair = it.next();
                sb.append("\t")
                        .append(pair.getKey())
                        .append(": ")
                        .append(pair.getValue().getAverage())
                        .append("\n");
                it.remove(); // avoids a ConcurrentModificationException
            }
            averagePerCountryString = sb.toString();
        }
        System.out.println(averagePerCountryString);
    }

    public void nzAgeBands() {
        String country = "New Zealand";
        int ageBandWidth = 10;

        if (nzAgeBandsString == null) {

            int highestBand = -1;
            HashMap<String, Integer> map = new HashMap<>();
            for (Person person : people) {
                if (person.getCountry().equals(country)) {
                    int ageBandLowerLimit = person.getAge() / ageBandWidth * 10;
                    String ageBand = getAgeBandString(ageBandLowerLimit, ageBandWidth);
                    if (map.containsKey(ageBand)) {
                        map.put(ageBand, map.get(ageBand) + 1);
                    } else {
                        map.put(ageBand, 1);
                        if (ageBandLowerLimit > highestBand) {
                            highestBand = ageBandLowerLimit;
                        }
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i <= highestBand) {
                String band = getAgeBandString(i, ageBandWidth);
                sb.append("\t")
                        .append(band)
                        .append(": ")
                        .append(map.get(band))
                        .append("\n");
                i += ageBandWidth;
            }
            nzAgeBandsString = sb.toString();
        }
        System.out.println(nzAgeBandsString);
    }

    /** Converts an age band lower limit and a width into a string for the age band. */
    private String getAgeBandString(int lowerLimit, int width) {
        return lowerLimit + " - " + (lowerLimit + width - 1);
    }
}
