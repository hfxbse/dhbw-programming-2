package test.database.people;

import main.database.companys.Company;
import main.database.companys.CompanyRepository;
import main.database.people.PeopleRepository;
import main.database.products.Product;
import main.database.products.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    final static PeopleRepository people = new PeopleRepository();
    final static ProductRepository products = new ProductRepository();
    final static CompanyRepository companies = new CompanyRepository();


    @BeforeAll
    static void setup() {
        people.registerEntry(new String[]{"0", "person1", "Male"});
        people.registerEntry(new String[]{"1", "person2", "Female"});
        people.registerEntry(new String[]{"2", "person3", "Female"});
        people.registerEntry(new String[]{"3", "person4", "Male"});

        products.registerEntry(new String[]{"4", "product1"});
        products.registerEntry(new String[]{"5", "product2"});
        products.registerEntry(new String[]{"6", "product3"});
        products.registerEntry(new String[]{"7", "product4"});

        companies.registerEntry(new String[]{"8", "company1"});
        companies.registerEntry(new String[]{"9", "company2"});
        companies.registerEntry(new String[]{"10", "company3"});

        people.getById(0).friends.addAll(List.of(people.getById(1), people.getById(2)));
        people.getById(1).friends.addAll(List.of(people.getById(0), people.getById(1)));
        people.getById(2).friends.addAll(List.of(people.getById(0), people.getById(1)));

        people.getById(1).friends.add(people.getById(3));
        people.getById(3).friends.add(people.getById(1));

        people.getById(0).purchases.addAll(List.of(
                products.getById(4),
                products.getById(5)
        ));

        people.getById(1).purchases.addAll(List.of(
                products.getById(4),
                products.getById(6)
        ));

        people.getById(2).purchases.addAll(List.of(
                products.getById(5),
                products.getById(6),
                products.getById(7)
        ));

        people.getById(3).purchases.addAll(List.of(
                products.getById(4),
                products.getById(5),
                products.getById(6),
                products.getById(7)
        ));

        companies.getById(8).products.addAll(List.of(
                products.getById(4),
                products.getById(5)
        ));

        companies.getById(9).products.add(products.getById(6));
        companies.getById(10).products.add(products.getById(7));
    }

    @Test
    @DisplayName("Returns product network for first person")
    void getProductNetworkForFirstPerson() {
        final Set<Product> products = people.getById(0).productNetwork();

        assertAll(
                "Product network for first person",
                () -> assertFalse(products.contains(PersonTest.products.getById(4))),
                () -> assertFalse(products.contains(PersonTest.products.getById(5))),
                () -> assertTrue(products.contains(PersonTest.products.getById(6))),
                () -> assertTrue(products.contains(PersonTest.products.getById(7)))
        );
    }

    @Test
    @DisplayName("Returns product network for second person")
    void getProductNetworkForSecondPerson() {
        final Set<Product> products = people.getById(1).productNetwork();

        assertAll(
                "Product network for second person",
                () -> assertFalse(products.contains(PersonTest.products.getById(4))),
                () -> assertTrue(products.contains(PersonTest.products.getById(5))),
                () -> assertFalse(products.contains(PersonTest.products.getById(6))),
                () -> assertTrue(products.contains(PersonTest.products.getById(7)))
        );
    }

    @Test
    @DisplayName("Returns product network for last person")
    void getProductNetworkForLastPerson() {
        final Set<Product> products = people.getById(3).productNetwork();
        assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Returns company network for second person")
    void getCompanyNetworkForSecondPerson() {
        final Set<Company> companies = people.getById(1).companyNetwork(PersonTest.companies);

        assertAll(
                "Product network for second person",
                () -> assertFalse(companies.contains(PersonTest.companies.getById(8))),
                () -> assertFalse(companies.contains(PersonTest.companies.getById(9))),
                () -> assertTrue(companies.contains(PersonTest.companies.getById(10)))
        );
    }
}
