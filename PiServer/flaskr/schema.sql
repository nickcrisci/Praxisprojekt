DROP TABLE IF EXISTS log;

CREATE TABLE log (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    category TEXT CHECK(category IN ('Info', 'Error', 'Debug', 'Warning', 'Other')) NOT NULL DEFAULT 'Other',
    title TEXT NOT NULL,
    body TEXT NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)