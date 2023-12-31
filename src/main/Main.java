package main;

import main.arguments.ArgumentParser;
import main.arguments.ArgumentPattern;
import main.database.DatabaseParser;
import main.database.companies.CompanyRepository;
import main.database.companies.ManufactureRegistration;
import main.database.people.FriendshipRegistration;
import main.database.products.ProductRepository;
import main.database.people.PeopleRepository;
import main.database.people.PurchaseRegistration;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;

public class Main {
    private final static ArgumentParser argumentParser = new ArgumentParser(
            new ArgumentPattern[]{
                    new ArgumentPattern("datenbank", "file")
            },
            new ArgumentPattern[]{
                new ArgumentPattern("personensuche", "person's name"),
                new ArgumentPattern("produktsuche", "product's name"),
                new ArgumentPattern("firmensuche", "company's name"),
                new ArgumentPattern("produktnetzwerk", "person's ID", true),
                new ArgumentPattern("firmennetzwerk", "person's ID", true),
            }
    );

    public static void main(String[] args) {
        final Map<String, String> arguments = argumentParser.parse(args);
        if (arguments == null) {
            System.exit(1);
        }

        PeopleRepository people = new PeopleRepository();
        CompanyRepository companies = new CompanyRepository();
        ProductRepository products = new ProductRepository();

        final File database = new File(arguments.get("datenbank"));
        arguments.remove("datenbank");

        try {
            DatabaseParser.parse(database, Map.of(
                    PeopleRepository.DATABASE_ENTRY_PATTERN, people,
                    CompanyRepository.DATABASE_ENTRY_PATTERN, companies,
                    ProductRepository.DATABASE_ENTRY_PATTERN, products,
                    FriendshipRegistration.DATABASE_ENTRY_PATTERN, new FriendshipRegistration(people),
                    PurchaseRegistration.DATABASE_ENTRY_PATTERN, new PurchaseRegistration(people, products),
                    ManufactureRegistration.DATABASE_ENTRY_PATTERN, new ManufactureRegistration(companies, products)
            ));
        } catch (IOException e) {
            System.err.printf("Could not read main.database file %s.%n", database.getName());
        }

        final String key = (String) arguments.keySet().toArray()[0];

        switch (key) {
            case "personensuche" -> System.out.println(people.findByName(arguments.get(key)));
            case "produktsuche" -> System.out.println(products.findByName(arguments.get(key)));
            case "firmensuche" -> System.out.println(companies.findByName(arguments.get(key)));
            case "produktnetzwerk" -> System.out.println(String.join(",",
                    people.getById(Integer.parseInt(arguments.get(key)))
                            .productNetwork()
                            .stream()
                            .map(product -> product.name)
                            .sorted(Comparator.comparing(String::toLowerCase))
                            .toList()
            ));
            case "firmennetzwerk" -> System.out.println(String.join(",",
                    people.getById(Integer.parseInt(arguments.get(key)))
                            .companyNetwork(companies)
                            .stream()
                            .map(company -> company.name)
                            .sorted(Comparator.comparing(String::toLowerCase))
                            .toList()
            ));
        }
    }
}