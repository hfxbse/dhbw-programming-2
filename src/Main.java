import database.DatabaseParser;
import database.companys.CompanyRepository;
import database.companys.ManufactureRegistration;
import database.people.FriendshipRegistration;
import database.products.ProductRepository;
import database.people.PeopleRepository;
import database.people.PurchaseRegistration;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.io.File;

public class Main {
    static final Map<String, String> arguments = Map.of(
            "--personensuche=.+", "person name",
            "--produktsuche=.+", "product name",
            "--firmensuche=.+", "company name",
            "--produktnetzwerk=\\d+", "person ID",
            "--firmennetzwerk=\\d+", "person ID",
            "--database=.+", "file"
    );

    static final ArgumentValidator argumentValidator = new ArgumentValidator(arguments.keySet().toArray(new String[0]));

    static String getArgumentDescription() {
        return String.join("\n", arguments.entrySet().stream().map(argument -> String.format(
                "%s=<%s>",
                argument.getKey().substring(0, argument.getKey().indexOf('=')),
                argument.getValue()
        )).toList());
    }

    static String getArgumentName(String argument) {
        return argument.split("=")[0].substring(2);
    }

    static String getArgumentValue(String argument) {
        return argument.substring(argument.indexOf("=") + 1).trim();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.printf("Expected exactly two argument. Valid options are:%n%s%n", getArgumentDescription());
            System.exit(1);
        }

        File database = null;
        String argumentName = null, argumentValue = "";

        for (String arg : args) {
            String argumentPattern = argumentValidator.findPattern(args[0]);
            if (argumentPattern == null) {
                System.err.printf("Unexpected argument %s. Valid options are:%n%s%n", args[0], getArgumentDescription());
                System.exit(2);
            }

            if (getArgumentName(arg).equals("database")) {
                database = new File(getArgumentValue(arg));
            } else {
                argumentName = getArgumentName(arg);
                argumentValue = getArgumentValue(arg);
            }
        }

        if (database == null) {
            System.err.printf(
                    "Expected one argument to be the database file. Available parameters: %n%s%n",
                    getArgumentDescription()
            );
            System.exit(3);
        }

        if (argumentName == null) {
            System.err.printf(
                    "Expected one argument to be a query. Available parameters: %n%s%n",
                    getArgumentDescription()
            );
            System.exit(4);
        }


        PeopleRepository people = new PeopleRepository();
        CompanyRepository companies = new CompanyRepository();
        ProductRepository products = new ProductRepository();

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
            System.err.printf("Could not read database file %s.%n", database.getName());
        }

        switch (argumentName) {
            case "personensuche" -> System.out.println(people.findByName(argumentValue));
            case "produktsuche" -> System.out.println(products.findByName(argumentValue));
            case "firmensuche" -> System.out.println(companies.findByName(argumentValue));
            case "produktnetzwerk" -> System.out.println(String.join(",",
                    people.getById(Integer.parseInt(argumentValue))
                            .productNetwork()
                            .stream()
                            .map(product -> product.name)
                            .sorted(Comparator.comparing(String::toLowerCase))
                            .toList()
            ));
            case "firmennetzwerk" -> System.out.println(String.join(",",
                    people.getById(Integer.parseInt(argumentValue))
                            .companyNetwork(companies)
                            .stream()
                            .map(company -> company.name)
                            .sorted(Comparator.comparing(String::toLowerCase))
                            .toList()
            ));
        }
    }
}