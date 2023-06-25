package database;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

abstract public class EntryRepository<T> implements EntryRegistration, Iterable<T> {
    final private Map<Integer, T> entries = new HashMap<>();

    public abstract T createEntry(Integer id, String[] data);

    @Override
    final public void registerEntry(String[] data) {
        Integer id = Integer.parseInt(data[0], 10);

        if (entries.containsKey(id)) return;

        entries.put(id, createEntry(id, data));
    }

    public T getById(Integer id) {
        return entries.get(id);
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
