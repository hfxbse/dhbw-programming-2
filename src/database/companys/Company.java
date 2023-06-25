package database.companys;

import database.Entry;

public class Company extends Entry {
    public Company(Integer id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return String.format("Company{id=%d,name=\"%s\"}", id, name);
    }
}