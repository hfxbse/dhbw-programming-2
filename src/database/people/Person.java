package database.people;

import database.Entry;

public class Person extends Entry {
    public Person(Integer id, String name, Gender gender) {
        super(id, name);
        this.gender = gender;
    }

    public enum Gender {
        MALE,
        FEMALE
    }

    public Gender gender;

    @Override
    public String toString() {
        return String.format("Person{id=%d,name=\"%s\",gender=\"%s\"}", id, name, gender.toString());
    }
}
