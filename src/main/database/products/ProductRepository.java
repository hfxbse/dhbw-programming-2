package main.database.products;

import main.database.EntryRepository;

public class ProductRepository extends EntryRepository<Product> {
    public static final String DATABASE_ENTRY_PATTERN = "\"product_id\",\"product_name\"";

    @Override
    public Product createEntry(Integer id, String[] data) {
        return new Product(id, data[1]);
    }
}
