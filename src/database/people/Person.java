package database.people;

import database.Entry;
import database.products.Product;

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
    final public Set<Product> purchases = new HashSet<>();

    @Override
    public String toString() {
        return String.format(
                "Person{id=%d,name=\"%s\",gender=\"%s\",friends=%s,purchases=%s}",
                id,
                name,
                gender.toString(),
                friends.stream().map(friend -> friend.id).toList(),
                purchases.stream().map(product -> product.id).toList()
        );
    }
}
