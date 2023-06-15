DROP TABLE IF EXISTS PUBLIC.REQUESTS CASCADE;
DROP TABLE IF EXISTS PUBLIC.BOOKINGS CASCADE;
DROP TABLE IF EXISTS PUBLIC.ITEMS CASCADE;
DROP TABLE IF EXISTS PUBLIC.USERS CASCADE;
DROP TABLE IF EXISTS PUBLIC.COMMENTS CASCADE;

CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    NAME VARCHAR(100),
    EMAIL VARCHAR(100) UNIQUE
);

CREATE TABLE IF NOT EXISTS PUBLIC.REQUESTS (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    DESCRIPTION VARCHAR(255) NOT NULL,
    REQUESTER_ID INTEGER NOT NULL,
    CREATED TIMESTAMP WITHOUT TIME ZONE NULL,
    CONSTRAINT FK_REQUESTS_USERS FOREIGN KEY (REQUESTER_ID) REFERENCES PUBLIC.USERS (ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.ITEMS (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    DESCRIPTION VARCHAR(255) NOT NULL,
    OWNER_ID INTEGER NOT NULL,
    REQUEST_ID INTEGER,
    AVAILABLE BOOLEAN NOT NULL,
    CONSTRAINT FK_ITEMS_USERS FOREIGN KEY (OWNER_ID) REFERENCES PUBLIC.USERS (ID),
    CONSTRAINT FK_ITEMS_REQUESTS FOREIGN KEY (REQUEST_ID) REFERENCES PUBLIC.REQUESTS (ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.BOOKINGS (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    START_TIME TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    END_TIME TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ITEM_ID INTEGER NOT NULL,
    BOOKER_ID INTEGER NOT NULL,
    STATUS VARCHAR(20) NOT NULL,
    CONSTRAINT FK_BOOKINGS_ITEMS FOREIGN KEY (ITEM_ID) REFERENCES PUBLIC.ITEMS (ID) ON DELETE CASCADE,
    CONSTRAINT FK_BOOKINGS_USERS FOREIGN KEY (BOOKER_ID) REFERENCES PUBLIC.USERS (ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.COMMENTS (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    TEXT VARCHAR(100) NOT NULL,
    ITEM_ID INTEGER NOT NULL,
    AUTHOR_ID INTEGER NOT NULL,
    CREATED TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT FK_COMMENTS_ITEMS FOREIGN KEY (ITEM_ID) REFERENCES PUBLIC.ITEMS (ID) ON DELETE CASCADE,
    CONSTRAINT FK_COMMENTS_USERS FOREIGN KEY (AUTHOR_ID) REFERENCES PUBLIC.USERS (ID) ON DELETE CASCADE
);