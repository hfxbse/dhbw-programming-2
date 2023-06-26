package test.database.people;

import main.database.people.PeopleRepository;
import main.database.people.PurchaseRegistration;
import main.database.products.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseRegistrationTest {
    final static ProductRepository products = new ProductRepository();
    final static PeopleRepository people = new PeopleRepository();

    final static PurchaseRegistration registration = new PurchaseRegistration(people, products);

    @BeforeAll
    static void setup() {
        products.registerEntry(new String[]{"0", "product1"});
        products.registerEntry(new String[]{"1", "product2"});

        people.registerEntry(new String[]{"2", "person1", "Male"});
        people.registerEntry(new String[]{"3", "person2", "Female"});
    }

    @Test
    @DisplayName("Registers purchase")
    void registerPurchase() {
        registration.registerEntry(new String[]{"3", "0"});

        assertAll(
                "Correct purchase registered",
                () -> assertTrue(people.getById(3).purchases.contains(products.getById(0))),
                () -> assertFalse(people.getById(3).purchases.contains(products.getById(1))),
                () -> assertFalse(people.getById(2).purchases.contains(products.getById(0))),
                () -> assertFalse(people.getById(2).purchases.contains(products.getById(1)))
        );
    }
}
