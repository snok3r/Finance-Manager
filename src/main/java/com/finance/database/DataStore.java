package com.finance.database;

import com.finance.model.Account;
import com.finance.model.Record;
import com.finance.model.User;

import java.sql.SQLException;
import java.util.Set;

public interface DataStore {

    void connect() throws SQLException;

    void close();

    // return null if no such user
    User acquireUser(String username, String password);

    // If no users, return empty collection (not null)
    Set<String> getUserNames();

    // If no accounts, return empty collection (not null)
    Set<Account> getAccounts(User owner);

    // If no records, return empty collection (not null)
    Set<Record> getRecords(Account account);

    void addUser(User user);

    void addAccount(User user, Account account);

    void addRecord(Account account, Record record);

    // return removed model.User or null if no such user
    User removeUser(String name);

    // return null if no such account
    Account removeAccount(User owner, Account account);

    // return null if no such record
    Record removeRecord(Account from, Record record);
}