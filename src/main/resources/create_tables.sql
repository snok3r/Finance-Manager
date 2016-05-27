CREATE TABLE IF NOT EXISTS users (
    USER_ID  INTEGER      PRIMARY KEY AUTOINCREMENT
                          NOT NULL,
    LOGIN    STRING       UNIQUE
                          NOT NULL,
    PASSWORD VARCHAR (32) NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
    ACCOUNT_ID  STRING  PRIMARY KEY
                        NOT NULL
                        UNIQUE,
    BALANCE     REAL    NOT NULL,
    DESCRIPTION TEXT    NOT NULL,
    USER_ID     INTEGER REFERENCES users (USER_ID) 
                        NOT NULL
);

CREATE TABLE IF NOT EXISTS records (
    RECORD_ID   STRING     PRIMARY KEY
                           NOT NULL
                           UNIQUE,
    RECORD_DATE DATE       NOT NULL,
    AMOUNT      REAL       NOT NULL,
    RECORD_TYPE STRING (9) NOT NULL,
    CATEGORY    STRING     NOT NULL,
    DESCRIPTION TEXT       NOT NULL,
    ACCOUNT_ID             REFERENCES accounts (ACCOUNT_ID) 
);
