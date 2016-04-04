import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedHashSet;
import java.util.Set;

public class Account {

    private String description;
    private float balance;
    private Set<Record> records;
    private int hash;

    /**
     * Initialize account with description and zero balance
     * @param description description of the account
     * */
    public Account(String description) {
        this.description = description;
        this.balance = 0.0f;
        this.records = new LinkedHashSet<>();
    }

    /**
     * Initialize account with description and start balance
     * @param description description of the account
     * @param balance starting balance
     * */
    public Account(String description, float balance) {
        this(description);
        this.balance = balance;
    }

    /**
     * @return balance of the account
     * */
    public float getBalance() {
        return balance;
    }

    /**
     * @return description of the account
     * */
    public String getDescription() {
        return description;
    }

    /**
     * Method to get all the records, returns new allocated Set
     * to prevent all sort of violations
     * @return Set of account's records (new allocated)
     * */
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
