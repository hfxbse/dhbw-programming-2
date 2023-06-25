package database;

import java.util.HashMap;
import java.util.Map;

abstract public class EntryRepository<T> implements EntryRegistration {
    final private Map<Integer, T> entries = new HashMap<>();

    public abstract T createEntry(Integer id, String[] data);

    @Override
    final public void registerEntry(String[] data) {
        Integer id = Integer.parseInt(data[0], 10);

        if (entries.containsKey(id)) return;

        entries.put(id, createEntry(id, data));
        System.out.println(entries.get(id));
    }

    public T getById(Integer id) {
        return entries.get(id);
    }
}
