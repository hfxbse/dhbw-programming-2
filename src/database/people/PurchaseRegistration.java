package database.people;

import database.EntryRegistration;
import database.products.ProductRepository;

public class PurchaseRegistration implements EntryRegistration {
    public static final String DATABASE_ENTRY_PATTERN = "\"person_id\",\"product_id\"";

    public PeopleRepository people;
    public ProductRepository products;

    public PurchaseRegistration(PeopleRepository people, ProductRepository products) {
        this.people = people;
        this.products = products;
    }

    @Override
    public void registerEntry(String[] data) {
        Integer person = Integer.parseInt(data[0]);
        Integer product = Integer.parseInt(data[1]);

        try {
            people.getById(person).purchases.add(products.getById(product));
        } catch (NullPointerException e) {
            throw new RuntimeException("Either person or product or both do not exist at this point.", e);
        }
    }
}
