package main.database;

public class Entry {
    public Integer id;
    public String name;

    public Entry(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Entry{id=%d,name=\"%s\"}", id, name);
    }
}
