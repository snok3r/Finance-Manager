package main;

import main.util.RecordType;

import java.sql.Date;
import java.util.UUID;

public class Record {

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
        return this.hashCode() == record.hashCode();
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            String uuid = String.format("%d%f%s%s", date.getTime(), amount, type.ordinal(), description);
            UUID key = UUID.nameUUIDFromBytes(uuid.getBytes());
            hash = key.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f %s", new java.util.Date(date.getTime()), amount, type);
    }

    public static void main(String[] args) {
        Record r1 = new Record(new Date(new java.util.Date().getTime()), 1000, RecordType.WITHDRAW, "test");
        Record r2 = new Record(new Date(new java.util.Date().getTime() - 1), 2000, RecordType.WITHDRAW, "test");

        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r1.equals(r2)); // false
    }
}
