CREATE TABLE AUTHOR(
    ID INT(10) NOT NULL AUTO_INCREMENT,
    FIRST_NAME VARCHAR(255) NOT NULL,
    LAST_NAME VARCHAR(255),
    CREATE_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATE_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE BOOK(
    ID INT(10) NOT NULL AUTO_INCREMENT,
    TITLE VARCHAR(255) NOT NULL,
    VERSION INT NOT NULL,
    PRICE DOUBLE NOT NULL,
    PUBLISHING_DATE DATE,
    CREATE_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATE_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE AUTHOR_BOOK(
    BOOK_ID INT(10) NOT NULL,
    AUTHOR_ID INT(10) NOT NULL,
    PRIMARY KEY(BOOK_ID, AUTHOR_ID),
    KEY FK_AUTHORBOOK_BOOK_IDX (BOOK_ID),
    CONSTRAINT FK_AUTHORBOOK_BOOK FOREIGN KEY (BOOK_ID) REFERENCES BOOK (ID),
    CONSTRAINT FK_AUTHORBOOK_AUTHOR FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHOR (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
