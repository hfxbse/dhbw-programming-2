package test.database.companys;

import main.database.companies.CompanyRepository;
import main.database.companies.ManufactureRegistration;
import main.database.products.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManufactureRegistrationTest {
    final static ProductRepository products = new ProductRepository();
    final static CompanyRepository companies = new CompanyRepository();

    final static ManufactureRegistration registration = new ManufactureRegistration(companies, products);

    @BeforeAll
    static void setup() {
        products.registerEntry(new String[]{"0", "product1"});
        products.registerEntry(new String[]{"1", "product2"});

        companies.registerEntry(new String[]{"2", "company1"});
        companies.registerEntry(new String[]{"3", "company2"});
    }

    @Test
    @DisplayName("Registers product manufacture")
    void registerManufacture() {
        registration.registerEntry(new String[]{"0", "3"});

        assertAll(
                "Correct manufacture registered",
                () -> assertTrue(companies.getById(3).products.contains(products.getById(0))),
                () -> assertFalse(companies.getById(3).products.contains(products.getById(1))),
                () -> assertFalse(companies.getById(2).products.contains(products.getById(0))),
                () -> assertFalse(companies.getById(2).products.contains(products.getById(1)))
        );
    }
}
