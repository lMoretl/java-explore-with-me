CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
    id BIGSERIAL PRIMARY KEY,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    title VARCHAR(120) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    created_on TIMESTAMP,
    published_on TIMESTAMP,

    initiator_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,

    FOREIGN KEY (initiator_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);