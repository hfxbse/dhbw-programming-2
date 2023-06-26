package main.database.people;

import main.database.Entry;
import main.database.companys.Company;
import main.database.companys.CompanyRepository;
import main.database.products.Product;

import java.util.HashSet;
import java.util.Set;

public class Person extends Entry {
    public Person(Integer id, String name, Gender gender) {
        super(id, name);
        this.gender = gender;
    }

    public enum Gender {
        MALE,
        FEMALE
    }

    public Gender gender;

    final public Set<Person> friends = new HashSet<>();
    final public Set<Product> purchases = new HashSet<>();

    public Set<Product> productNetwork() {
        final Set<Product> products = new HashSet<>();

        for (Person friend : friends) {
            products.addAll(friend.purchases);
        }

        products.removeAll(purchases);

        return products;
    }

    public Set<Company> companyNetwork(CompanyRepository companyRepository) {
        final Set<Company> companies = new HashSet<>();

        for (Product product : productNetwork()) {
            companies.add(companyRepository.findManufacture(product));
        }

        purchases.stream().map(companyRepository::findManufacture).forEach(companies::remove);

        return companies;
    }

    @Override
    public String toString() {
        return String.format(
                "Person{id=%d,name=\"%s\",gender=\"%s\",friends=%s,purchases=%s}",
                id,
                name,
                gender.toString(),
                friends.stream().map(friend -> friend.id).toList(),
                purchases.stream().map(product -> product.id).toList()
        );
    }
}
