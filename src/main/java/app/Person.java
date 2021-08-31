package app;


/** Class that maps to the expected format of the input JSON file */
public class Person {

    // Attributes which map to expected JSON fields
    private String firstName;
    private String lastName;
    private String country;
    private int age;


    /** Getter required for object mapping */
    public String getFirstName() {
        return firstName;
    }


    /** Getter required for object mapping */

    public String getLastName() {
        return lastName;
    }


    /** Getter required for object mapping */
    public String getCountry() {
        return country;
    }


    /** Getter required for object mapping */
    public int getAge() {
        return age;
    }


    public String toString() {
        return firstName + " " + lastName + ", " + age + " (" + country + ")";
    }

    public String toStringWithoutCountry() {
        return firstName + " " + lastName + ", " + age;
    }
}
