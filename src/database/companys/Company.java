package database.companys;

public class Company {
    public Integer id;
    public String name;

    public Company(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Company{id=%d,name=\"%s\"}", id, name);
    }
}