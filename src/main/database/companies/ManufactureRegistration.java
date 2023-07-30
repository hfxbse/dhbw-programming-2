package main.database.companies;

import main.database.EntryRegistration;
import main.database.products.ProductRepository;

public class ManufactureRegistration implements EntryRegistration {
    public static final String DATABASE_ENTRY_PATTERN = "\"product_id\",\"company_id\"";

    public CompanyRepository companies;
    public ProductRepository products;

    public ManufactureRegistration(CompanyRepository companies, ProductRepository products) {
        this.companies = companies;
        this.products = products;
    }

    /**
     * Adds a product to the manufacture.
     *
     * @param data Product ID and manufacture ID as a list of strings.
     */
    @Override
    public void registerEntry(String[] data) {
        Integer product = Integer.parseInt(data[0]);
        Integer company = Integer.parseInt(data[1]);

        try {
            companies.getById(company).products.add(products.getById(product));
        } catch (NullPointerException e) {
            throw new RuntimeException("Either company or product or both do not exist at this point.", e);
        }
    }
}
