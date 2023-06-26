package test.database;

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
        companies.registerEntry(new String[]{"0", "first"});
        companies.registerEntry(new String[]{"1", "second"});

        companies.registerEntry(new String[]{"3", "manufacture1"});
        companies.getById(3).products.addAll(List.of(products[0], products[1]));

        companies.registerEntry(new String[]{"4", "manufacture2"});
        companies.getById(4).products.add(products[2]);
    }

    @SuppressWarnings("SameParameterValue")
    void assertCompany(String heading, int id, String name, int productCount, Company company) {
        assertAll(
                heading,
                () -> assertEquals(id, company.id),
                () -> assertEquals(name, company.name),
                () -> assertEquals(productCount, company.products.size())
        );
    }

    void assertCompany(String heading, int id, String name, Company company) {
        assertAll(
                heading,
                () -> assertEquals(id, company.id),
                () -> assertEquals(name, company.name),
                () -> assertTrue(company.products.isEmpty())
        );
    }

    @Test
    @DisplayName("Creates new company entry")
    void createCompanyEntry() {
        Company company = companies.createEntry(0, new String[]{"0", "name"});
        assertCompany("Created company.", 0, "name", company);
    }

    @Test
    @DisplayName("Registers new company entry")
    void registerCompany() {
        companies.registerEntry(new String[]{"2", "third"});
        final Company company = companies.getById(2);
        assertCompany("Registered company.", 2, "third", company);
    }


    @Test
    @DisplayName("Ignores duplicated company entries")
    void overrideCompany() {
        companies.registerEntry(new String[]{"0", "override"});
        final Company company = companies.getById(0);
        assertCompany("Duplicated company", 0, "first", company);
    }

    @Test
    @DisplayName("Gets company by id")
    void getCompanyById() {
        final Company company = companies.getById(0);
        assertCompany("Retrieved company", 0, "first", company);
    }

    @Test
    @DisplayName("Returns null if company with given id does not exists")
    void retrieveMissingCompanyId() {
        final Company company = companies.getById(-1);
        assertNull(company);
    }

    @Test
    @DisplayName("Finds both companies when searching for \"s\"")
    void findByQuery() {
        final List<Company> result = companies.findByName("s");
        assertEquals(List.of(companies.getById(0), companies.getById(1)), result);
    }

    @Test
    @DisplayName("Finds single companies when searching for \"second\"")
    void findByExactName() {
        final List<Company> result = companies.findByName("second");
        assertEquals(List.of(companies.getById(1)), result);
    }

    @Test
    @DisplayName("Finds manufacture of product")
    void findManufactureOfProduct() {
        final Company manufacture = companies.findManufacture(products[1]);
        assertCompany("Found manufacture", 3, "manufacture1", 2, manufacture);
    }

    @Test
    @DisplayName("Returns null if no manufacture can be found")
    void manufactureUnknown() {
        final Company manufacture = companies.findManufacture(products[3]);
        assertNull(manufacture);
    }
}
