package com.finance.util;

import com.finance.model.Record;

import java.util.Comparator;

public class RecordDateComparator implements Comparator<Record> {
    @Override
    public int compare(Record o1, Record o2) {
        if (o1.getLongDate() < o2.getLongDate())
            return -1;
        else if (o1.getLongDate() > o2.getLongDate())
            return 1;
        else
            return 0;
    }
}