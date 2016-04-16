package main.java;

import main.util.MD5;
import main.util.RecordType;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Account implements Serializable {

    private User owner;
    private String description;
    private float balance;
    private final Set<Record> records;
    private int hash;

    /**
     * Initialize account with User owner,
     * description and zero balance
     *
     * @param owner       owner of the account
     * @param description description of the account
     */
    public Account(User owner, String description) {
        this.owner = owner;
        this.description = description;
        this.balance = 0.0f;
        this.records = new LinkedHashSet<>();
    }

    /**
     * Initialize account with User owner,
     * start balance and description
     *
     * @param owner       owner of the account
     * @param balance     starting balance
     * @param description description of the account
     */
    public Account(User owner, float balance, String description) {
        this(owner, description);
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
     * @return account owner
     */
    public User getOwner() {
        return owner;
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
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof Account)) return false;

        Account account = (Account) obj;
        return (owner + description).equals(account.owner + account.description);
    }

    /**
     * Making hash with <tt>owner</tt>
     */
    @Override
    public int hashCode() {
        if (hash == 0)
            hash = MD5.getHash(owner.getLogin() + description).hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%s. Balance: %.2f", description, balance);
    }
}
