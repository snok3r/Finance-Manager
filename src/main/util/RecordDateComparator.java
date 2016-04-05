package main.util;

import main.Record;

import java.util.Comparator;

public class RecordDateComparator implements Comparator<Record> {
    @Override
    public int compare(Record o1, Record o2) {
        if (o1.getDate().before(o2.getDate()))
            return -1;
        else if (o1.getDate().after(o2.getDate()))
            return 1;
        else
            return 0;
    }
}