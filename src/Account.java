import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedHashSet;
import java.util.Set;

public class Account {

    private String description;
    private float balance;
    private Set<Record> records;
    private int hash;

    public Account(String description) {
        this.description = description;
        this.balance = 0.0f;
        this.records = new LinkedHashSet<>();
    }

    public Account(String description, float balance) {
        this(description);
        this.balance = balance;
    }

    public float getBalance() {
        return balance;
    }

    public String getDescription() {
        return description;
    }

    public Set<Record> getRecords() {
        return new LinkedHashSet<>(records);
    }

    @Override
    public boolean equals(Object obj) {
        throw new NotImplementedException();
    }

    @Override
    public int hashCode() {
        throw new NotImplementedException();
    }

    public static void main(String[] args) {
        Account account = new Account("Test account");
        Set<Record> records = account.getRecords();
        records.add(new Record());
    }
}
