CREATE KEYSPACE IF NOT EXISTS url_shortener
    WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE url_shortener;

CREATE TABLE IF NOT EXISTS url (
    short_code TEXT PRIMARY KEY,
    long_url   TEXT,
    created_at TIMESTAMP
);
