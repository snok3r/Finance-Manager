package main.java;

import main.util.RecordType;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Account implements Serializable {

    private String description;
    private float balance;
    private final Set<Record> records;

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
     * Adds <tt>record</tt> to Set of records,
     * changing the balance with record.amount
     * depending on transaction type
     * (withdraw subtract money, deposit adds money)
     *
     * @param record record to add
     */
    public void addRecord(Record record) {
        synchronized (records) {
            if (records.add(record)) {
                // if record has been added, then changing balance
                if (record.getType() == RecordType.WITHDRAW)
                    balance -= record.getAmount();
                else if (record.getType() == RecordType.DEPOSIT)
                    balance += record.getAmount();
            }
        }
    }

    /**
     * Deletes <tt>record</tt> from Set of records
     * changing the balance with record.amount
     * depending on transaction type:
     * (withdraw adds money, deposit subtract money)
     *
     * @param record record to delete
     * @return deleted <tt>record</tt> (or null if not found)
     */
    public Record removeRecord(Record record) {
        Record toRet = null;

        synchronized (records) {
            if (records.contains(record)) {
                for (Record rec : records) {
                    if (rec.equals(record)) {
                        toRet = rec;

                        if (rec.getType() == RecordType.WITHDRAW)
                            balance += rec.getAmount();
                        else if (rec.getType() == RecordType.DEPOSIT)
                            balance -= rec.getAmount();

                        records.remove(rec);
                        break;
                    }
                }
            }
        }

        return toRet;
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
     * Method to get all the records
     *
     * @return Set of account's records
     */
    public Set<Record> getRecords() {
        /*
        returns newly allocated Set
        to prevent sort of violations,
        like adding something to the returned value
        */
        return new LinkedHashSet<>(records);
    }

    /**
     * Method to get number of records
     *
     * @return number of records in the account
     */
    public int getNumOfRecords() {
        return records.size();
    }

    @Override
    public String toString() {
        return String.format("%s. Balance: %.2f", description, balance);
    }
}
