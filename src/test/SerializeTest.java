import main.Account;
import main.Record;
import main.User;
import main.util.RecordType;
import main.util.Serialize;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SerializeTest {

    private List<User> usersToSerialize;
    private List<User> usersToDeserialize;
    private final String fileName = "test.dat";
    private final Serialize<User> serializator = new Serialize<>();

    @Before
    public void setUp() throws Exception {
        Date time = new java.sql.Date(new java.util.Date().getTime());

        usersToSerialize = new ArrayList<>(3);
        usersToDeserialize = new ArrayList<>(3);

        User user = new User("AlexeyIgorevich", "ja-#*d2d");
        Account account = new Account("RUB account");
        account.addRecord(new Record(time, 40000, RecordType.DEPOSIT, "Month Pay"));
        account.addRecord(new Record(time, 1000, RecordType.WITHDRAW, "Flower Shop"));
        account.addRecord(new Record(time, 4000, RecordType.WITHDRAW, "Candy Shop"));
        account.addRecord(new Record(time, 6000, RecordType.WITHDRAW, "Restaurant"));
        user.addAccount(account);
        usersToSerialize.add(user);

        user = new User("EgorMaximych", "a&+s32l");
        account = new Account("EUR account");
        account.addRecord(new Record(time, 3000, RecordType.DEPOSIT, "Month Pay"));
        account.addRecord(new Record(time, 100, RecordType.WITHDRAW, "Cable"));
        account.addRecord(new Record(time, 50, RecordType.WITHDRAW, "Supermarket"));
        user.addAccount(account);
        account = new Account("USD account");
        account.addRecord(new Record(time, 1000, RecordType.DEPOSIT, "Month Pay"));
        account.addRecord(new Record(time, 10, RecordType.WITHDRAW, "Netflix"));
        user.addAccount(account);
        usersToSerialize.add(user);

        user = new User("FelixDzerzhinskyy", "daklj@#fa");
        account = new Account("USD account");
        account.addRecord(new Record(time, 5000, RecordType.DEPOSIT, "Month Pay"));
        account.addRecord(new Record(time, 200, RecordType.WITHDRAW, "Book Store"));
        account.addRecord(new Record(time, 200, RecordType.WITHDRAW, "Supermarket"));
        user.addAccount(account);
        usersToSerialize.add(user);
    }

    @Test
    public void testEquals() throws Exception {
        serializator.serialize(fileName, usersToSerialize);
        serializator.deserialize(fileName, usersToDeserialize, true);

        assertEquals(usersToSerialize, usersToDeserialize);
        assertEquals(usersToDeserialize, usersToSerialize);
    }
}