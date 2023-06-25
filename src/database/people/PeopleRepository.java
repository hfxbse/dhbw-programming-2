package database.people;

import database.EntryRepository;
import database.people.Person.Gender;

public class PeopleRepository extends EntryRepository<Person> {
    public static final String DATABASE_ENTRY_PATTERN = "\"person_id\", \"person_name\", \"person_gender\"";

    @Override
    public Person createEntry(Integer id, String[] data) {
        return new Person(id, data[1], data[2].equals("Male") ? Gender.MALE : Gender.FEMALE);
    }
}
