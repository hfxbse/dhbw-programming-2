package database.companys;

import database.EntryRepository;

public class CompanyRepository extends EntryRepository<Company> {
    public static final String DATABASE_ENTRY_PATTERN = "\"company_id\",\"company_name\"";

    @Override
    public Company createEntry(Integer id, String[] data) {
        return new Company(id, data[1]);
    }
}
