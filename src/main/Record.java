package main;

import main.util.MD5;
import main.util.RecordType;

import java.io.Serializable;
import java.sql.Date;

public class Record implements Serializable {

    private final Date date;
    private float amount;
    private RecordType type;
    private final String description;
    private int hash;

    /**
     * Initialize Record with date, transaction amount,
     * transaction type and transaction description
     *
     * @param date        java.sql.Date of the record
     * @param amount      transaction amount
     * @param type        transaction type (RecordType.WITHDRAW or RecordType.DEPOSIT)
     * @param description transaction description
     */
    public Record(Date date, float amount, RecordType type, String description) {
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    /**
     * @return java.sql.Date transaction date
     */
    public Date getDate() {
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
        boolean res = date.equals(record.date) &&
                amount == record.amount &&
                type == record.type &&
                description.equals(record.description);
        return res;
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            String str = String.format("%d%f%s%s", date.getTime(), amount, type.ordinal(), description);
            hash = MD5.getHash(str).hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%s. %s %.2f %s", description, new java.util.Date(date.getTime()), amount, type);
    }
}
