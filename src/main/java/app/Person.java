package app;

public class Person {

    private String firstName;
    private String lastName;
    private String country;
    private int age;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCountry() {
        return country;
    }

    public int getAge() {
        return age;
    }

    public String toString() {
        return
                "Name: " + firstName + lastName + ", Country: " + country + ", Age: " + age;
    }
}
