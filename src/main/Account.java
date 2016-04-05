package main;

import main.util.RecordType;

import java.util.Iterator;
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
     * Adds <tt>record</tt> to Set of records,
     * changing the balance with record.amount
     * depending on transaction type
     * (withdraw subtract money, deposit adds money)
     *
     * @param record record to add
     */
    public void addRecord(Record record) {
        records.add(record);
        if (record.getType() == RecordType.WITHDRAW)
            balance -= record.getAmount();
        else if (record.getType() == RecordType.DEPOSIT)
            balance += record.getAmount();
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

        for (Iterator<Record> it = records.iterator(); it.hasNext(); ) {
            Record r = it.next();
            if (r.equals(record)) {
                toRet = r;

                if (r.getType() == RecordType.WITHDRAW)
                    balance += record.getAmount();
                else if (r.getType() == RecordType.DEPOSIT)
                    balance -= record.getAmount();

                records.remove(r);
                break;
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
     * Method to get all the records, returns new allocated Set
     * to prevent all sort of violations
     *
     * @return Set of account's records (new allocated)
     */
    public Set<Record> getRecords() {
        return new LinkedHashSet<>(records);
    }

    @Override
    public String toString() {
        return String.format("%s. Balance: %.2f", description, balance);
    }

    public static void main(String[] args) {
        Account account = new Account("Test account");
        Record record = new Record(new java.sql.Date(new java.util.Date().getTime()), 1000, RecordType.WITHDRAW, "test");
        account.addRecord(record);
        System.out.println(account);
        account.removeRecord(record);
        System.out.println(account);
    }
}
