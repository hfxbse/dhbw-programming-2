package test.database.people;

import main.database.people.FriendshipRegistration;
import main.database.people.PeopleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FriendshipRegistrationTest {
    final static PeopleRepository people = new PeopleRepository();
    final static FriendshipRegistration registration = new FriendshipRegistration(people);

    @BeforeAll
    static void setup() {
        people.registerEntry(new String[]{"0", "person1", "Male"});
        people.registerEntry(new String[]{"1", "person2", "Female"});
        people.registerEntry(new String[]{"2", "person3", "Female"});
    }

    @Test
    @DisplayName("Registers friendship for both people")
    void registerFriendship() {
        registration.registerEntry(new String[]{"0", "2"});

        assertAll(
                "Correct friendship registered",
                () -> assertTrue(people.getById(0).friends.contains(people.getById(2))),
                () -> assertTrue(people.getById(2).friends.contains(people.getById(0))),
                () -> assertFalse(people.getById(0).friends.contains(people.getById(1))),
                () -> assertFalse(people.getById(2).friends.contains(people.getById(1))),
                () -> assertFalse(people.getById(1).friends.contains(people.getById(0))),
                () -> assertFalse(people.getById(1).friends.contains(people.getById(2)))
        );
    }
}
