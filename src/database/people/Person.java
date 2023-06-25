package database.people;

import database.Entry;

import java.util.HashSet;
import java.util.Set;

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

    final public Set<Person> friends = new HashSet<>();

    @Override
    public String toString() {
        return String.format(
                "Person{id=%d,name=\"%s\",gender=\"%s\",friends=%s}",
                id,
                name,
                gender.toString(),
                friends.stream().map(friend -> friend.id).toList()
        );
    }
}
