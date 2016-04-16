import main.java.Record;
import main.util.RecordType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTest {
    private Record recordDif1, recordDif2, recordEq1, recordEq2;
    private long date;
    private float[] amounts = new float[]{1000, 2000, 5000, 10000};
    private float amountDif1, amountDif2, amountEq;

    @Before
    public void setUp() throws Exception {
        amountDif1 = new java.util.Random().nextInt(amounts.length);
        amountDif2 = new java.util.Random().nextInt(amounts.length);

        amountEq = 3000;

        date = new java.util.Date().getTime();

        recordDif1 = new Record(amountDif1, RecordType.WITHDRAW, "test");
        recordDif2 = new Record(amountDif2, RecordType.DEPOSIT, "test");

        recordEq1 = new Record(date, amountEq, RecordType.WITHDRAW, "test");
        recordEq2 = new Record(date, amountEq, RecordType.WITHDRAW, "test");
    }

    @Test
    public void getAmount() throws Exception {
        float expResultEq = amountEq;
        float resultEq = recordEq1.getAmount();
        assertEquals(expResultEq, resultEq, 0.1);
    }

    @Test
    public void equals() throws Exception {
        assertEquals(recordEq1, recordEq2);
        assertEquals(recordEq2, recordEq1);

        assertNotEquals(recordDif1, recordDif2);
        assertNotEquals(recordDif1, recordEq1);
        assertNotEquals(recordDif1, recordEq2);
        assertNotEquals(recordDif2, recordEq1);
        assertNotEquals(recordDif2, recordEq2);

    }
}