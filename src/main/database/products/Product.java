package main.database.products;

import main.database.Entry;

public class Product extends Entry {
    public Product(Integer id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return String.format("Product{id=%d,name=\"%s\"}", id, name);
    }
}