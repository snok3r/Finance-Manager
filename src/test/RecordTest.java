import main.Record;
import main.util.RecordType;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;

public class RecordTest {
    private Record recordDif1, recordDif2, recordEq1, recordEq2;
    private Date date;
    private float amountDif1, amountDif2, amountEq;

    @Before
    public void setUp() throws Exception {
        amountDif1 = 1000;
        amountDif2 = 2000;

        amountEq = 3000;

        date = new Date(new java.util.Date().getTime());

        recordDif1 = new Record(date, amountDif1, RecordType.WITHDRAW, "test");
        recordDif2 = new Record(date, amountDif2, RecordType.DEPOSIT, "test");

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
        assertNotNull(recordDif1);
        assertNotNull(recordDif2);
        assertNotNull(recordEq1);
        assertNotNull(recordEq2);

        assertEquals(recordEq1, recordEq2);
        assertEquals(recordEq2, recordEq1);
        
        assertNotEquals(recordDif1, recordDif2);
        assertNotEquals(recordDif1, recordEq1);
        assertNotEquals(recordDif1, recordEq2);
        assertNotEquals(recordDif2, recordEq1);
        assertNotEquals(recordDif2, recordEq2);

    }
}