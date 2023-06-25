package database.companys;

import database.Entry;
import database.products.Product;

import java.util.HashSet;
import java.util.Set;

public class Company extends Entry {
    public Company(Integer id, String name) {
        super(id, name);
    }

    final public Set<Product> products = new HashSet<>();

    @Override
    public String toString() {
        return String.format(
                "Company{id=%d,name=\"%s\",products=%s}",
                id,
                name,
                products.stream().map(product -> product.id).toList()
        );
    }
}