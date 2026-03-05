CREATE TABLE IF NOT EXISTS doctor (
    id          BIGSERIAL PRIMARY KEY,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    specialty   VARCHAR(150) NOT NULL,
    address     TEXT,
    latitude    NUMERIC(9,6),
    longitude   NUMERIC(9,6)
);
