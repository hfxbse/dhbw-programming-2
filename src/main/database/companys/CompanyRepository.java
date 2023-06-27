package main.database.companys;

import main.database.EntryRepository;
import main.database.products.Product;

public class CompanyRepository extends EntryRepository<Company> {
    public static final String DATABASE_ENTRY_PATTERN = "\"company_id\",\"company_name\"";

    /**
     * Finds the manufacture for the given product.
     *
     * @param product The product for which the manufacture should be found.
     * @return The manufacture for the given product. Null if manufacture could be found.
     */
    public Company findManufacture(Product product) {
        for (Company company : entries.values()) {
            if (company.products.contains(product)) return company;
        }

        return null;
    }

    @Override
    public Company createEntry(Integer id, String[] data) {
        return new Company(id, data[1]);
    }
}
