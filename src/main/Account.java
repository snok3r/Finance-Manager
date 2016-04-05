package main;

import main.util.RecordType;

import java.util.LinkedHashSet;
import java.util.Set;

public class Account {

    private String description;
    private float balance;
    private Set<Record> records;

    /**
     * Initialize account with description and zero balance
     *
     * @param description description of the account
     */
    public Account(String description) {
        this.description = description;
        this.balance = 0.0f;
        this.records = new LinkedHashSet<>();
    }

    /**
     * Initialize account with start balance and description
     *
     * @param balance     starting balance
     * @param description description of the account
     */
    public Account(float balance, String description) {
        this(description);
        this.balance = balance;
    }

    /**
     * @return balance of the account
     */
    public float getBalance() {
        return balance;
    }

    /**
     * @return description of the account
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method to get all the records, returns new allocated Set
     * to prevent all sort of violations
     *
     * @return Set of account's records (new allocated)
     */
    public Set<Record> getRecords() {
        return new LinkedHashSet<>(records);
    }

    public static void main(String[] args) {
        Account account = new Account("Test account");
        Set<Record> records = account.getRecords();
        records.add(new Record(new java.sql.Date(new java.util.Date().getTime()), 1000, RecordType.WITHDRAW, "test"));
    }
}
