package com.finance.model;

import com.finance.util.Category;
import com.finance.util.MD5;
import com.finance.util.RecordType;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Record implements Serializable {

    private final long date;
    private float amount;
    private final RecordType type;
    private final Category category;
    private final String description;

    private final static SimpleDateFormat simpleDate
            = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSSXXX");
    private int hash;

    /**
     * Initialize RecordAdd with date (long number), transaction amount,
     * transaction type and transaction description
     *
     * @param date        long number date of transaction
     * @param amount      transaction amount
     * @param type        transaction type (RecordType.WITHDRAW or RecordType.DEPOSIT)
     * @param category    transaction category (eg. Category.Health, Category.Travel, etc.)
     * @param description transaction description
     */
    public Record(long date, float amount, RecordType type, Category category, String description) {
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
    }

    /**
     * Initialize RecordAdd with transaction amount,
     * transaction type and transaction description.
     * Date of the transaction sets automatically (current date).
     *
     * @param amount      transaction amount
     * @param type        transaction type (RecordType.WITHDRAW or RecordType.DEPOSIT)
     * @param category    transaction category (eg. Category.Health, Category.Travel, etc.)
     * @param description transaction description
     */
    public Record(float amount, RecordType type, Category category, String description) {
        this(new java.util.Date().getTime(), amount, type, category, description);
    }

    /**
     * Initialize RecordAdd with date, transaction amount,
     * transaction type and transaction description
     *
     * @param date        java.main.sql.Date of transaction
     * @param amount      transaction amount
     * @param type        transaction type (RecordType.WITHDRAW or RecordType.DEPOSIT)
     * @param category    transaction category (eg. Category.Health, Category.Travel, etc.)
     * @param description transaction description
     */
    public Record(Date date, float amount, RecordType type, Category category, String description) {
        this(date.getTime(), amount, type, category, description);
    }

    /**
     * Initialize RecordAdd with date, transaction amount,
     * transaction type and transaction description
     *
     * @param date        java.util.Date of transaction
     * @param amount      transaction amount
     * @param type        transaction type (RecordType.WITHDRAW or RecordType.DEPOSIT)
     * @param category    transaction category (eg. Category.Health, Category.Travel, etc.)
     * @param description transaction description
     */
    public Record(java.util.Date date, float amount, RecordType type, Category category, String description) {
        this(date.getTime(), amount, type, category, description);
    }

    /**
     * @return java.main.sql.Date transaction date
     */
    public Date getDate() {
        return new Date(date);
    }

    /**
     * @return long representation of transaction date
     */
    public long getLongDate() {
        return date;
    }

    /**
     * @return transaction amount
     */
    public float getAmount() {
        return amount;
    }

    /**
     * @return transaction type (WITHDRAW or DEPOSIT)
     */
    public RecordType getType() {
        return type;
    }

    /**
     * @return transaction category (eg. Health, Travel, etc.)
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @return transaction description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof Record)) return false;

        Record record = (Record) obj;
        boolean res = date == record.date &&
                amount == record.amount &&
                type == record.type &&
                description.equals(record.description);
        return res;
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            String str = String.format("%d%f%d%d%s", date, amount, type.ordinal(), category.ordinal(), description);
            hash = MD5.getHash(str).hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%s. %s. %s %.2f %s", category, description, simpleDate.format(getDate()), amount, type);
    }
}
