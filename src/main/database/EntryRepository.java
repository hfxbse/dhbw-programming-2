package main.database;

import java.util.*;
import java.util.function.Consumer;

abstract public class EntryRepository<T extends Entry> implements EntryRegistration, Iterable<T> {
    final protected Map<Integer, T> entries = new HashMap<>();

    /**
     * Creates the matching entry.
     *
     * @param id The parsed entry ID.
     * @param data The entry values as string. This includes also the ID.
     * @return The newly created entry.
     */
    public abstract T createEntry(Integer id, String[] data);

    /**
     * Add a new entry to the entry map, where the key is the id of the entry.
     * If an entry with the same id does already exist, it will be ignored
     *
     * @param data Entry values as a list of strings.
     */
    @Override
    final public void registerEntry(String[] data) {
        Integer id = Integer.parseInt(data[0], 10);

        if (entries.containsKey(id)) return;

        entries.put(id, createEntry(id, data));
    }

    /**
     * Gets the entry with the matching ID.
     *
     * @param id ID of the entry to be retrieved.
     * @return The matching entry. If no entry with the given ID exists, null.
     */
    public T getById(Integer id) {
        return entries.get(id);
    }

    /**
     * Finds entries which contain the query. Casing is ignored for the search.
     *
     * @param query String which should be contained within the entry name.
     * @return List of entries which contain the given query. Empty when no matching entry was found.
     */
    public List<T> findByName(String query) {
        return entries.values().stream()
                .filter(entry -> entry.name.toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    @Override
    public Iterator<T> iterator() {
        return entries.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        entries.values().forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return entries.values().spliterator();
    }
}
