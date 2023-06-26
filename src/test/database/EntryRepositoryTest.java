package test.database;

import main.database.Entry;
import main.database.EntryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntryRepositoryTest {
    private static class TestRepository extends EntryRepository<Entry> {
        @Override
        public Entry createEntry(Integer id, String[] data) {
            return new Entry(id, data[1]);
        }
    }

    final static TestRepository repository = new TestRepository();


    void assertEntry(String heading, int id, String name, Entry entry) {
        assertAll(
                heading,
                () -> assertEquals(id, entry.id),
                () -> assertEquals(name, entry.name)
        );
    }

    @BeforeAll
    static void setup() {
        repository.registerEntry(new String[]{"0", "first"});
        repository.registerEntry(new String[]{"1", "second"});
    }

    @Test
    @DisplayName("Registers new entry")
    void registerEntry() {
        repository.registerEntry(new String[]{"2", "third"});
        final Entry entry = repository.getById(2);
        assertEntry("Registered entry.", 2, "third", entry);
    }


    @Test
    @DisplayName("Ignores duplicated entries")
    void overrideEntry() {
        repository.registerEntry(new String[]{"0", "override"});
        final Entry entry = repository.getById(0);
        assertEntry("Duplicated entry", 0, "first", entry);
    }

    @Test
    @DisplayName("Gets entry by id")
    void getEntryById() {
        final Entry entry = repository.getById(0);
        assertEntry("Retrieved entry", 0, "first", entry);
    }

    @Test
    @DisplayName("Returns null if entry with given id does not exists")
    void retrieveMissingEntryId() {
        final Entry entry = repository.getById(-1);
        assertNull(entry);
    }

    @Test
    @DisplayName("Finds both entry when searching for \"S\"")
    void findByQuery() {
        final List<Entry> result = repository.findByName("S");
        assertEquals(List.of(repository.getById(0), repository.getById(1)), result);
    }

    @Test
    @DisplayName("Finds single entry when searching for \"second\"")
    void findByExactName() {
        final List<Entry> result = repository.findByName("second");
        assertEquals(List.of(repository.getById(1)), result);
    }
}
