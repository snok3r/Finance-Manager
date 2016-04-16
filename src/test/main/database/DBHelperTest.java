package main.database;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DBHelperTest {
    DBHelper db;

    @Before
    public void setUp() throws Exception {
        db = DBHelper.getInstance();
    }

    @Test
    public void executeSQL() throws Exception {
    }
}