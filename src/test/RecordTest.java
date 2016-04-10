import main.Record;
import main.util.RecordType;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;

public class RecordTest {
    private Record r1, r2;

    @Before
    public void setUp() throws Exception {
        r1 = new Record(new Date(new java.util.Date().getTime()), 1000, RecordType.WITHDRAW, "test");
        r2 = new Record(new Date(new java.util.Date().getTime() - 1), 2000, RecordType.WITHDRAW, "test");
    }

    @Test
    public void getDate() throws Exception {

    }

    @Test
    public void getAmount() throws Exception {

    }

    @Test
    public void getType() throws Exception {

    }

    @Test
    public void getDescription() throws Exception {

    }

    @Test
    public void equals() throws Exception {

    }
}