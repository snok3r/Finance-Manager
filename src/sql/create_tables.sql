CREATE TABLE IF NOT EXISTS users (
    USER_ID  INTEGER      PRIMARY KEY AUTOINCREMENT
                          NOT NULL,
    LOGIN    STRING       UNIQUE
                          NOT NULL,
    PASSWORD VARCHAR (32) NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
    ACCOUNT_ID INTEGER PRIMARY KEY AUTOINCREMENT
                       NOT NULL,
    BALANCE    REAL    NOT NULL,
    DESCR      TEXT    NOT NULL,
    USER_ID    INTEGER REFERENCES users (USER_ID) 
                       NOT NULL
);

CREATE TABLE IF NOT EXISTS records (
    RECORD_ID  INTEGER    PRIMARY KEY
                          NOT NULL,
    DATE       DATE       NOT NULL,
    AMOUNT     REAL       NOT NULL,
    TYPE       STRING (9) NOT NULL,
    CATEGORY   STRING     NOT NULL,
    [DESC]     TEXT       NOT NULL,
    ACCOUNT_ID            REFERENCES accounts (ACCOUNT_ID) 
);
