package main;

import java.sql.Date;
import java.util.UUID;

public class Record {

    private final Date date;
    private float amount;
    private boolean withdraw;
    private final String description;
    private int hash;

    public Record(Date date, float amount, boolean withdraw, String description) {
        this.date = date;
        this.amount = amount;
        this.withdraw = withdraw;
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public float getAmount() {
        return amount;
    }

    public boolean isWithdraw() {
        return withdraw;
    }

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
            String uuid = String.format("%d%f%s%s", date.getTime(), amount, withdraw, description);
            UUID key = UUID.nameUUIDFromBytes(uuid.getBytes());
            hash = key.hashCode();
        }
        return hash;
    }

    public static void main(String[] args) {
        Record r1 = new Record(new Date(new java.util.Date().getTime()), 1000, true, "test");
        Record r2 = new Record(new Date(new java.util.Date().getTime() - 1), 1000, true, "test");

        System.out.println(r1.equals(r2)); // false
    }
}
