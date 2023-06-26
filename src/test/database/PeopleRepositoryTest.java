package test.database;

import main.database.people.PeopleRepository;
import main.database.people.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeopleRepositoryTest {
    PeopleRepository people = new PeopleRepository();

    @Test
    @DisplayName("Creates new male person entry")
    void createMaleEntry() {
        Person product = people.createEntry(0, new String[]{"0", "name", "Male"});

        assertAll(
                "Created male person.",
                () -> assertEquals(0, product.id),
                () -> assertEquals("name", product.name),
                () -> assertEquals(Person.Gender.MALE, product.gender)
        );
    }

    @Test
    @DisplayName("Creates new female person entry")
    void createFemaleEntry() {
        Person product = people.createEntry(0, new String[]{"0", "name", "Female"});

        assertAll(
                "Created female person.",
                () -> assertEquals(0, product.id),
                () -> assertEquals("name", product.name),
                () -> assertEquals(Person.Gender.FEMALE, product.gender)
        );
    }
}
