package database.people;

public class Person {
    public Person(Integer id, String name, Gender gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    public enum Gender {
        MALE,
        FEMALE
    }

    public Integer id;
    public String name;
    public Gender gender;


    @Override
    public String toString() {
        return String.format("Person{id=%d,name=\"%s\",gender=\"%s\"}", id, name, gender.toString());
    }
}
