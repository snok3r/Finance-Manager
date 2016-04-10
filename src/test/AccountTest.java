import main.Account;
import main.Record;
import main.util.RecordType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {

    private Account account;
    private Record record;

    @Before
    public void setUp() throws Exception {
        account = new Account("Test account");
        record = new Record(new java.sql.Date(new java.util.Date().getTime()), 1000, RecordType.WITHDRAW, "test");
    }

    @Test
    public void addRecord() throws Exception {

    }

    @Test
    public void removeRecord() throws Exception {

    }

    @Test
    public void getBalance() throws Exception {

    }

    @Test
    public void getDescription() throws Exception {

    }

}