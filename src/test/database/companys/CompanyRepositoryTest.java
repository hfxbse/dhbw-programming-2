package test.database.companys;

import main.database.companys.Company;
import main.database.companys.CompanyRepository;
import main.database.products.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompanyRepositoryTest {
    final static CompanyRepository companies = new CompanyRepository();

    final static Product[] products = {
            new Product(0, "product1"),
            new Product(1, "product2"),
            new Product(2, "product3"),
            new Product(4, "product4")
    };

    @BeforeAll
    static void setup() {
        companies.registerEntry(new String[]{"3", "manufacture1"});
        companies.getById(3).products.addAll(List.of(products[0], products[1]));

        companies.registerEntry(new String[]{"4", "manufacture2"});
        companies.getById(4).products.add(products[2]);
    }

    @Test
    @DisplayName("Creates new company entry")
    void createCompanyEntry() {
        Company company = companies.createEntry(0, new String[]{"0", "name"});

        assertAll(
                "Created company.",
                () -> assertEquals(0, company.id),
                () -> assertEquals("name", company.name),
                () -> assertTrue(company.products.isEmpty())
        );
    }

    @Test
    @DisplayName("Finds manufacture of product")
    void findManufactureOfProduct() {
        final Company manufacture = companies.findManufacture(products[1]);

        assertAll(
                "Found manufacture.",
                () -> assertEquals(3, manufacture.id),
                () -> assertEquals("manufacture1", manufacture.name),
                () -> assertEquals(2, manufacture.products.size())
        );
    }

    @Test
    @DisplayName("Returns null if no manufacture can be found")
    void manufactureUnknown() {
        final Company manufacture = companies.findManufacture(products[3]);
        assertNull(manufacture);
    }
}
