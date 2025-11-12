CREATE TABLE type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE value (
    id SERIAL PRIMARY KEY,
    field VARCHAR(100) NOT NULL,
    value TEXT,
    type_id INT REFERENCES type(id)
);
