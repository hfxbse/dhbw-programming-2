package test.database;

import main.database.products.Product;
import main.database.products.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRepositoryTest {
    ProductRepository products = new ProductRepository();

    @Test
    @DisplayName("Creates new product entry")
    void createProductEntry() {
        Product product = products.createEntry(0, new String[]{"0", "name"});

        assertAll(
                "Created product.",
                () -> assertEquals(0, product.id),
                () -> assertEquals("name", product.name)
        );
    }
}
