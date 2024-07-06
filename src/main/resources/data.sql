CREATE TABLE operation (
    id UUID PRIMARY KEY,
    type VARCHAR(10) NOT NULL,
    unit_cost DECIMAL(19, 2) NOT NULL,
    quantity INTEGER NOT NULL
);