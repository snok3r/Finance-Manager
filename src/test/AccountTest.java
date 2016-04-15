import main.Account;
import main.Record;
import main.util.RecordType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {

    private Account account;
    private Record recordD, recordW;
    private float depositAmount, withdrawAmount;
    private float[] amounts = new float[]{1000, 2000, 5000, 10000};
    private float delta = 0.1f;

    @Before
    public void setUp() throws Exception {
        account = new Account("Test account");

        depositAmount = amounts[new java.util.Random().nextInt(amounts.length)];
        withdrawAmount = amounts[new java.util.Random().nextInt(amounts.length)];

        recordD = new Record(new java.util.Date().getTime(), depositAmount, RecordType.DEPOSIT, "deposit 2000");
        recordW = new Record(new java.util.Date().getTime(), withdrawAmount, RecordType.WITHDRAW, "Withdraw 1000");
    }

    @Test
    public void addRecord() throws Exception {
        float balanceAfterDeposit = account.getBalance() + depositAmount;
        float balanceAfterWithdraw = balanceAfterDeposit - withdrawAmount;

        // checking that account has no records
        assertEquals(account.getNumOfRecords(), 0);

        // performing deposit (2000)
        account.addRecord(recordD);
        // checking how many records account has
        assertEquals(account.getNumOfRecords(), 1);
        // checking balance after the operation
        assertEquals(account.getBalance(), balanceAfterDeposit, delta);

        // adding record that already in the Set
        account.addRecord(recordD);
        // checking how many records account has (shouldn't change)
        assertEquals(account.getNumOfRecords(), 1);
        // checking balance after the operation (shouldn't change)
        assertEquals(account.getBalance(), balanceAfterDeposit, delta);

        // performing withdraw (1000)
        account.addRecord(recordW);
        // checking how many records account has
        assertEquals(account.getNumOfRecords(), 2);
        // checking balance after the operation
        assertEquals(account.getBalance(), balanceAfterWithdraw, delta);
    }

    @Test
    public void removeRecord() throws Exception {
        // adding deposit record
        account.addRecord(recordD);
        // checking how many records account has
        assertEquals(account.getNumOfRecords(), 1);
        // checking deleting of not existing record (expect null)
        assertNull(account.removeRecord(recordW));
        // checking how many records account has (it shouldn't change)
        assertEquals(account.getNumOfRecords(), 1);
        // checking balance after the operation
        assertEquals(account.getBalance(), recordD.getAmount(), delta);

        // adding withdraw record
        account.addRecord(recordW);
        // checking how many records account has
        assertEquals(account.getNumOfRecords(), 2);
        // checking deleting of existing record
        assertNotNull(account.removeRecord(recordD));
        // checking how many records account has
        assertEquals(account.getNumOfRecords(), 1);
        // checking balance after the operation
        assertEquals(account.getBalance(), -recordW.getAmount(), delta);


        // checking deleting of existing record
        assertNotNull(account.removeRecord(recordW));
        // checking how many records account has
        assertEquals(account.getNumOfRecords(), 0);
        // checking balance after the operation
        assertEquals(account.getBalance(), 0, delta);


        // deleting from empty account, expecting null
        assertNull(account.removeRecord(recordD));
        // deleting from empty account, expecting null
        assertNull(account.removeRecord(recordW));
        // checking that balance left 0
        assertEquals(account.getBalance(), 0, delta);
    }
}